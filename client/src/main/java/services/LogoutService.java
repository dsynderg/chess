package services;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import modules.User;
import server.Server;

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
        System.exit(-1);
    }

    private void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("does login logic");

    }

    public AbstractMap.SimpleEntry<AuthData, User> register() {
//        server = new Server();
        Scanner scanner = new Scanner(System.in);
        final String[] MESSAGES = {"What is your username?", "What is your password?", "What is your email?"};
        final String[] USERHEADINGS = {"username", "password", "email"};
        Map<String, String> userData = new HashMap<>();

        System.out.println("does register logic");
        for (int i = 0; i < 3; i++) {
            System.out.println(MESSAGES[i]);
            System.out.print(">>>");
            String line = scanner.nextLine().trim().toLowerCase();
            userData.put(USERHEADINGS[i], line);
        }
        User user = new User(userData.get("username"),userData.get("password"),userData.get("email"));
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        try {
            var response = httpHelper.requestMaker(RequestType.post,"user",json,null);
            Map<String,String> authmap  = gson.fromJson(response.body(),Map.class);
            AuthData authData = new AuthData(authmap.get("authToken"),authmap.get("username"));
            return new AbstractMap.SimpleEntry<>(authData,user);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                login();
                return null;
            }
            if (Objects.equals(line, "register")) {
                return register();
            }
            return null;
        }

    }
}