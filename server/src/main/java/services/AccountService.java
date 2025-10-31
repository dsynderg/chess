package services;

import dataaccess.*;
import dataaccess.memoryImplementaiton.MemAuthDatabase;
import dataaccess.memoryImplementaiton.MemDatabaseRegistry;
import dataaccess.memoryImplementaiton.MemUserDatabase;
import modules.AuthData;
import modules.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AccountService {
    MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    boolean isMemoryimplemtation = DatabaseConfigLoader.memoryimplementation();



    public boolean creatAccont(User userdata) {
        if (userdata.username() == null || userdata.email() == null || userdata.password() == null) {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(userdata.password(), BCrypt.gensalt());
        User userdataWPassword = new User(userdata.username(),hashedPassword, userdata.email());
        if(isMemoryimplemtation) {

            return userdatabase.addToDatabase(userdataWPassword);
        }
        //fix this
        return false;
    }

    public boolean checkUsername(String username) {
        if(isMemoryimplemtation) {
            return userdatabase.inDatabase(username);
        }
        return false;
    }

    public AuthData authDataGenorator(String username) {
        UUID uuid = UUID.randomUUID();
        String authToken = uuid.toString();
        AuthData authdata = new AuthData(authToken, username);
        if(isMemoryimplemtation) {

            authdatabase.addToDatabase(authdata);
        }
        else{
            SQLAuthDatabase.addToDatabase(authdata);
        }
        return authdata;

    }

    public boolean checkPassword(String password, User userObject) {

        if(isMemoryimplemtation) {
            return userdatabase.passwordUsernameMatch(password, userObject.username());
        }
        return false;
    }

    public boolean checkAuth(String auth) {
        if(isMemoryimplemtation) {
            return authdatabase.inDatabase(auth);
        }
        else{
            return SQLAuthDatabase.inDatabase(auth);
        }

    }

    public boolean removeAuth(String auth) {
        if(isMemoryimplemtation) {
            ArrayList<AuthData> database = authdatabase.getDatabase();
            for (AuthData data : database) {
                if (Objects.equals(data.authToken(), auth)) {
                    return authdatabase.removeFromDatabase(data);

                }
            }
            return false;
        }
        else{
            var authData = new AuthData(auth,SQLAuthDatabase.getUsername(auth));
            return SQLAuthDatabase.removeFromDatabase(authData);
        }
    }

    public String getUsernameFromAuth(String authToken) {
        if(isMemoryimplemtation){
            return authdatabase.getUsername(authToken);
        }
        else{
            return SQLAuthDatabase.getUsername(authToken);
        }
    }



}
