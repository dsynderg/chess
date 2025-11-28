package services;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Scanner;

public class WsObserver extends Endpoint {
    public Session session;
    private static Gson gson;
    public boolean hasRecivedMessage = false;
    public static void main(String[] args) throws Exception {

        WsObserver client = new WsObserver(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"123","bob",123));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo:");
        while(true) {
            client.send(scanner.nextLine());
        }
    }
    public WsObserver(int port, UserGameCommand command) throws Exception{
        String portString = Integer.toString(port);
        gson = new Gson();
        URI uri = new URI("ws://localhost:"+portString+"/game/"+command.getGameID().toString()+"/"+ command.getUsername());
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this,uri);

        session.getBasicRemote().sendText("logged in");
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
//                printmessage(message);
                System.out.println(message);
                hasRecivedMessage = true;
            }
        });
    }
    private static void printmessage(String message){
       var jsonMessage = gson.fromJson(message, Map.class);
       if(jsonMessage.get("messagetype")== ServerMessage.ServerMessageType.LOAD_GAME){
           if(jsonMessage.get("gameboard")==null){return;}
           ChessGame.TeamColor color = gson.fromJson((String)jsonMessage.get("teamColor"),ChessGame.TeamColor.class);
           ChessBoard board = gson.fromJson((String) jsonMessage.get("gameboard"),ChessBoard.class);
           BoardPrinter.printBoard(board,color);

       }
       if(jsonMessage.get("messagetype")== ServerMessage.ServerMessageType.ERROR){
           System.out.println((String)jsonMessage.get("error"));
       }
       if(jsonMessage.get("messagetype")== ServerMessage.ServerMessageType.NOTIFICATION){
           System.out.println((String) jsonMessage.get("notification"));
       }
       else{
           System.out.println("There was a problem with the server");
       }
    }
    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
