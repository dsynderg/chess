package passoff.server;

import services.AccountService;
import services.DeleteService;
import modules.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServiceTestsReal {
    AccountService aS = new AccountService();

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
    void repeteAccounts() {
        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        assert !aS.creatAccont(dummyUser);
    }
}
