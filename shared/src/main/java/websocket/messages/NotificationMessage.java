package websocket.messages;

public class NotificationMessage extends ServerMessage{
    String message;
    public NotificationMessage(ServerMessageType type, String messagejson) {
        message = messagejson;
        super(type, messagejson);
    }
    public String getMessage(){
        return message;
    }
}
