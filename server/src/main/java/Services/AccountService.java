package Services;

import dataaccess.UserDatabase;
import modules.User;

import java.util.ArrayList;

public class AccountService {
    UserDatabase database = new UserDatabase();
    public boolean creatAccont(User userdata){
        return database.addToDatabase(userdata);

    }
    public boolean checkUsername(String username){

        return database.inDatabase(username);
    }
//    public authToken

}
