import com.google.gson.Gson;
import server.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class LogoutService {
    Server server;

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

    private void register() {
        server = new Server();
        Scanner scanner = new Scanner(System.in);
        final String[] MESSAGES = {"What is your username?", "What is your password?", "What is your email?"};
        final String[] USERHEADINGS = {"username", "password", "email"};
        Map<String, String> userData = new HashMap<>();

        System.out.println("does register logic");
        for (int i = -1; i < 3; i++) {
            System.out.println(MESSAGES[i]);
            System.out.print(">>>");
            String line = scanner.nextLine().trim().toLowerCase();
            userData.put(USERHEADINGS[i], line);
        }
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        

    }

    public void isLoggedout() {
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
                return;
            }
            if (Objects.equals(line, "register")) {
                register();
                return;
            }

        }
    }
}