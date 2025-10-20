package Services;

import dataaccess.DatabaseRegistry;
import dataaccess.UserDatabase;
import modules.AuthData;
import dataaccess.AuthDatabase;
import modules.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AccountService {
    UserDatabase userdatabase = DatabaseRegistry.getUserDB();
    AuthDatabase authdatabase = DatabaseRegistry.getAuthDB();
    public boolean creatAccont(User userdata){
        return userdatabase.addToDatabase(userdata);

    }
    public boolean checkUsername(String username){

        return userdatabase.inDatabase(username);
    }
    public AuthData authDataGenorator(String username){
        UUID uuid = UUID.randomUUID();
        String authToken = uuid.toString();
        AuthData authdata = new AuthData(authToken,username);
        authdatabase.addToDatabase(authdata);
        return authdata;

    }
    public boolean checkPassword(String password, User userObject){
        return userdatabase.passwordUsernameMatch(password,userObject.username());
    }
    public boolean checkAuth(String auth){
        return authdatabase.inDatabase(auth);

    }
    public boolean removeAuth(String auth){
        ArrayList<AuthData> database = authdatabase.getDatabase();
        for(AuthData data:database){
            if (Objects.equals(data.authToken(), auth)){
                return authdatabase.removeFromDatabase(data);

            }
        }
        return false;
    }

}
