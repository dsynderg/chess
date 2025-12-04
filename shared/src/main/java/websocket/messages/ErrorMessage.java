package websocket.messages;

public class ErrorMessage extends ServerMessage{
    String errorMessage;
    public ErrorMessage(ServerMessageType type, String messagejson) {
        super(type, messagejson);
        errorMessage = messagejson;
    }
    public String getErrorMessage(){return errorMessage;}
}
