package websocket.messages;

public class NotificationMessage extends ServerMessage{
    String message;
    public NotificationMessage(ServerMessageType type, String messagejson) {
        super(type, messagejson);
        message = messagejson;

    }
    public String getMessage(){
        return message;
    }
}
