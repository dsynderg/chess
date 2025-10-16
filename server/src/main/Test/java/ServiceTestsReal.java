import Services.AccountService;
import Services.DeleteService;
import modules.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServiceTestsReal {
    AccountService aS = new AccountService();

    @Test
    @DisplayName("clear database")
    void main() {
        User user1 = new User("joe",";alsdkfj","sdfl@sdf");
        assert aS.creatAccont(user1);
        assert DeleteService.deleteAll();
        assert !aS.checkUsername(user1.username());
        return;
    }
}
