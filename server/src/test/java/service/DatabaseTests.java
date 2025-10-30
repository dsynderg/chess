package service;

import dataaccess.SQLAuthDatabase;
import dataaccess.SQLDeleteDataBase;
import modules.AuthData;
import org.junit.jupiter.api.Test;

public class DatabaseTests {
    @Test
    void deleteDatabase(){
        assert SQLDeleteDataBase.deleteAll();
    }
    @Test
    void addToAuth(){
        AuthData auth = new AuthData("123", "myname is jeff");
        assert SQLAuthDatabase.addToDatabase(auth);
    }
}
