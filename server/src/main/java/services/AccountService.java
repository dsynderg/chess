package services;

import dataaccess.MemAuthDatabase;
import dataaccess.MemDatabaseRegistry;
import dataaccess.MemUserDatabase;
import modules.AuthData;
import modules.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AccountService {
    MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    boolean isMemoryimplemtation = DatabaseConfigLoader.memoryimplementation();

    public boolean creatAccont(User userdata) {
        if(isMemoryimplemtation) {
            if (userdata.username() == null || userdata.email() == null || userdata.password() == null) {
                return false;
            }
            return userdatabase.addToDatabase(userdata);
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
        if(isMemoryimplemtation) {
            UUID uuid = UUID.randomUUID();
            String authToken = uuid.toString();
            AuthData authdata = new AuthData(authToken, username);
            authdatabase.addToDatabase(authdata);
            return authdata;
        }
        return null;

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
        return false;
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
        return false;
    }

    public String getUsernameFromAuth(String authToken) {
        if(isMemoryimplemtation){
        return authdatabase.getUsername(authToken);
    }
        return null;
    }



}
