package service;

import services.AccountService;
import services.DeleteService;
import modules.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.GameService;

public class ServiceTests {
    AccountService aS = new AccountService();
    GameService gS = new GameService();

    @Test
    @DisplayName("clear database")
    void clearAll() {
        User user1 = new User("joe", ";alsdkfj", "sdfl@sdf");
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
        assert DeleteService.deleteAll();
        assert !aS.checkUsername(user1.username());
    }
    @Test
    void clearAllFail(){
        User user1 = new User("joe", ";alsdkfj", "sdfl@sdf");
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
        assert DeleteService.deleteAll();
        assert aS.creatAccont(user1);

        assert aS.checkUsername(user1.username());
    }

    @Test
    void repeteAccounts() {
        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        assert !aS.creatAccont(dummyUser);
    }
    @Test
    void loginTest(){

        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        assert !aS.creatAccont(dummyUser);
//        assert aS.
    }
}
