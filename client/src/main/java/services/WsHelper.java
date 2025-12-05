package services;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import modules.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WsHelper extends Endpoint {
    public ChessBoard board = new ChessBoard();
    public Session session;
    private static Gson gson;
    public boolean hasRecivedMessage = false;
    private static String username;
    private boolean isOver =false;
    public GameData gameData;
    public static void main(String[] args) throws Exception {

        WsHelper client = new WsHelper(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"123","bob",123),true);
        WsHelper client2 = new WsHelper(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"4444123","bob2",123),true);

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
        URI uri = new URI("ws://localhost:"+portString+"/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this,uri);
        String connectCommand = gson.toJson(command);
        send(connectCommand);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                messageHandler(message);
//                System.out.println(message);
                if (!isOver) {
                    System.out.print("PLAY GAME>>>");
                }
                else{
                    System.out.print("Press Enter");
                }
                hasRecivedMessage = true;
            }
        });
    }
    public void loadBoard(GameData game){
        System.out.println();
        if(Objects.equals(username, game.blackUsername())){
            //print from the black side

            BoardPrinter.printBoard(game.game().getBoard(), ChessGame.TeamColor.BLACK );
        }
        else{
            BoardPrinter.printBoard((game.game().getBoard()), ChessGame.TeamColor.WHITE);
        }

    }

    private void messageHandler(String message){
       var jsonMessage = gson.fromJson(message, ServerMessage.class);
       if(jsonMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
           var loadjsonMessage = gson.fromJson(message, LoadGameMessage.class);
           gameData = gson.fromJson(loadjsonMessage.getGame(), GameData.class);
           this.loadBoard(gameData);

       }
       else if(jsonMessage.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){
           var errorjsonMessage = gson.fromJson(message, ErrorMessage.class);
           String notificaitonMessage = gson.fromJson(errorjsonMessage.getErrorMessage(),Map.class).get("error").toString();
           System.out.println("Error: "+notificaitonMessage);
       }
       else if(jsonMessage.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){
           var notificationjsonMessage = gson.fromJson(message, NotificationMessage.class);
           String notificaitonMessage = gson.fromJson(notificationjsonMessage.getMessage(),Map.class).get("notification").toString();
           Pattern p = Pattern.compile("^(.+)\\s+has won the game!!!!$");
           Matcher m = p.matcher(notificaitonMessage);
           System.out.println("Notification: "+notificaitonMessage);
           if(m.matches()){
               isOver = true;
           }
       }
       else{
           System.out.println("There was a problem with the server");
       }
    }
    boolean getIsOver(){
        return isOver;
    }

    public void send(String message/*this should be a json'ed userGameCommand*/) throws IOException {
        session.getBasicRemote().sendText(message);
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
    public void close() throws IOException {
        session.close();
    }
}
