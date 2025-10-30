package service;

import dataaccess.SQLDeleteDataBase;
import org.junit.jupiter.api.Test;

public class DatabaseTests {
    @Test
    void deleteDatabase(){
        assert SQLDeleteDataBase.deleteAll();
    }
}
