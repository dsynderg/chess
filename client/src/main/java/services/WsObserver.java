package services;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import modules.AuthData;

import java.net.URI;

public class WsObserver {
    public Session session;
    public WsObserver(int port, String path) throws Exception{
        String portString = Integer.toString(port);
        URI uri = new URI("ws://localhost:"+portString+"/"+path);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this,uri);
    }
}
