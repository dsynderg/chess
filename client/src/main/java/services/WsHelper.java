package services;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import modules.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class WsHelper extends Endpoint {
    public ChessBoard board = new ChessBoard();
    public Session session;
    private static Gson gson;
    public boolean hasRecivedMessage = false;
    private static String username;
    public static void main(String[] args) throws Exception {

        WsHelper client = new WsHelper(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"123","bob",123),true);

        Scanner scanner = new Scanner(System.in);

//        System.out.println("Enter a message you want to echo:");
        while(true) {
            var bob = scanner.nextLine();
        }
    }

    public WsHelper(int port, UserGameCommand command,boolean isPlayer) throws Exception{
        String portString = Integer.toString(port);
        gson = new Gson();
        username = command.getUsername();
        URI uri = new URI("ws://localhost:"+portString+"/game/"+command.getGameID().toString()+"/"+ command.getUsername());
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this,uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                messageHandler(message);
                System.out.println(message);
                hasRecivedMessage = true;
            }
        });
    }

    private static void messageHandler(String message){
       var jsonMessage = gson.fromJson(message, ServerMessage.class);
       if(jsonMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
           var game = gson.fromJson(jsonMessage.getMessage(), GameData.class);
           System.out.println();
           if(Objects.equals(username, game.blackUsername())){
               //print from the black side

               BoardPrinter.printBoard(game.game().getBoard(), ChessGame.TeamColor.BLACK );
           }
           else{
               BoardPrinter.printBoard((game.game().getBoard()), ChessGame.TeamColor.WHITE);
           }

       }
       if(jsonMessage.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){
           System.out.println("Error: "+jsonMessage.getMessage());
       }
       if(jsonMessage.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){
           System.out.println("Notification: "+ jsonMessage.getMessage());
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
    public void close() throws IOException {
        session.close();
    }
}
