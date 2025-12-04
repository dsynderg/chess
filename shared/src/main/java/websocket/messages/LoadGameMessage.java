package websocket.messages;

public class LoadGameMessage extends ServerMessage{
    String game;
    public LoadGameMessage(ServerMessageType type, String messagejson) {
        super(type, messagejson);
        game = messagejson;
    }
    public String getGame(){return game;}

}
