package services;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class WsObserver {
    public Session session;
    private static Gson gson;
    public WsObserver(int port, String path,String game) throws Exception{
        String portString = Integer.toString(port);
        gson = new Gson();
        URI uri = new URI("ws://localhost:"+portString+"/"+path+"/"+game);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this,uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                printmessage(message);
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
}
