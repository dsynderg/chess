package services;

import dataaccess.*;
import dataaccess.memoryImplementaiton.MemAuthDatabase;
import dataaccess.memoryImplementaiton.MemDatabaseRegistry;
import dataaccess.memoryImplementaiton.MemUserDatabase;
import modules.AuthData;
import modules.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AccountService {
    MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    boolean isMemoryimplemtation;

    {
        try {
            isMemoryimplemtation = DatabaseConfigLoader.memoryimplementation();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean creatAccont(User userdata) throws SQLException, DataAccessException {
        if (userdata.username() == null || userdata.email() == null || userdata.password() == null) {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(userdata.password(), BCrypt.gensalt());
        User userdataWPassword = new User(userdata.username(), hashedPassword, userdata.email());

        SQLUserDatabase.addToDatabase(userdataWPassword);
        return userdatabase.addToDatabase(userdataWPassword);


    }


    public boolean checkUsername(String username) throws SQLException, DataAccessException {

            return userdatabase.inDatabase(username) || SQLUserDatabase.inDatabase(username);
    }

    public AuthData authDataGenorator(String username) throws SQLException, DataAccessException {
        UUID uuid = UUID.randomUUID();
        String authToken = uuid.toString();
        AuthData authdata = new AuthData(authToken, username);
        if(isMemoryimplemtation) {
            SQLAuthDatabase.addToDatabase(authdata);
            authdatabase.addToDatabase(authdata);
        }

        return authdata;

    }

    public boolean checkPassword(String password, User userObject) throws SQLException, DataAccessException {

            return userdatabase.passwordUsernameMatch(password, userObject.username()) ||SQLUserDatabase.passwordUsernameMatch(password,userObject.username());


    }

    public boolean checkAuth(String auth) throws SQLException, DataAccessException {

            return authdatabase.inDatabase(auth) || SQLAuthDatabase.inDatabase(auth);



    }

    public boolean removeAuth(String auth) throws SQLException, DataAccessException {

            ArrayList<AuthData> database = authdatabase.getDatabase();
            for (AuthData data : database) {
                if (Objects.equals(data.authToken(), auth)) {
                    authdatabase.removeFromDatabase(data);

                }
            }
            var authData = new AuthData(auth,SQLAuthDatabase.getUsername(auth));
            return SQLAuthDatabase.removeFromDatabase(authData);

    }

    public String getUsernameFromAuth(String authToken) throws SQLException, DataAccessException {

            String username = authdatabase.getUsername(authToken);
            if (username != null){
                return username;
            }
            return SQLAuthDatabase.getUsername(authToken);


    }



}
