package services;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import modules.User;

import java.util.*;

public class LogoutService {
    private HttpHelper httpHelper = new HttpHelper();

    private void help() {
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("LOGIN: Allows you to access a previously regestered accout");
        System.out.println("REGISTER: Creates a new accout");
        System.out.println("QUIT: Ends your session");
    }

    private void quit() {
        System.exit(1);
    }

    private  AbstractMap.SimpleEntry<AuthData, User> login() {
        Scanner scanner = new Scanner(System.in);
        final String[] messages = {"What is your username?", "What is your password?"};
        final String[] userHeadings = {"username", "password"};
        Map<String, String> userData = new HashMap<>();

        for (int i = 0; i < 2; i++) {
            System.out.println(messages[i]);
            System.out.print(">>>");
            String line = scanner.nextLine().trim().toLowerCase();
            userData.put(userHeadings[i], line);
        }
        User user = new User(userData.get("username"),userData.get("password"),null);
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        try {
            var response = httpHelper.requestMaker(RequestType.post,"session",json,null);
            if(response.statusCode()==200){
            Map<String,String> authmap  = gson.fromJson(response.body(),Map.class);
            AuthData authData = new AuthData(authmap.get("authToken"),authmap.get("username"));
            return new AbstractMap.SimpleEntry<>(authData,user);}
            return null;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            System.out.println("There was an error logging in, please try again");
            return null;
        }
    }

    public AbstractMap.SimpleEntry<AuthData, User> register() {
//        server = new Server();
        Scanner scanner = new Scanner(System.in);
        final String[] messages = {"What is your username?", "What is your password?", "What is your email?"};
        final String[] userheadings = {"username", "password", "email"};
        Map<String, String> userData = new HashMap<>();

        for (int i = 0; i < 3; i++) {
            System.out.println(messages[i]);
            System.out.print(">>>");
            String line = scanner.nextLine().trim().toLowerCase();
            userData.put(userheadings[i], line);
        }
        User user = new User(userData.get("username"),userData.get("password"),userData.get("email"));
        return registerLogic(user);



    }
    public AbstractMap.SimpleEntry<AuthData,User> registerLogic(User user ){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        try {
            var response = httpHelper.requestMaker(RequestType.post,"user",json,null);
            Map<String,String> authmap  = gson.fromJson(response.body(),Map.class);
            AuthData authData = new AuthData(authmap.get("authToken"),authmap.get("username"));
            return new AbstractMap.SimpleEntry<>(authData,user);
        } catch (Exception e) {
            System.out.println("There was a problem registering, please try again");
            return null;
        }
    }

    public AbstractMap.SimpleEntry<AuthData,User> isLoggedout() {
        //will eventually return a user object
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("LOGGED_OUT>>>");
            String line = scanner.nextLine().trim().toLowerCase();
            if (Objects.equals(line, "help")) {
                help();
            }
            if (Objects.equals(line, "quit")) {
                quit();
            }
            if (Objects.equals(line, "login")) {
                var authtoken = login();
                if(authtoken!=null) {return authtoken;}
            }
            if (Objects.equals(line, "register")) {
                var authtoken = register();
                if(authtoken!=null) {return authtoken;}
            }
        }

    }

}