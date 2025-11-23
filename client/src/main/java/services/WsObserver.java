package services;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import modules.AuthData;
import modules.ServerMessage;
import server.Server;

import javax.swing.*;
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
       if(jsonMessage.get("messagetype")== ServerMessage.LOAD_GAME){

       }
       if(jsonMessage.get("messagetype")== ServerMessage.NOTIFICATION){

       }
       if(jsonMessage.get("messagetype")== ServerMessage.ERROR){

       }
       else{

       }
    }
}
