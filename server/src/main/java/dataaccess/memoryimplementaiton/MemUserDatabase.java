package dataaccess.memoryimplementaiton;

import modules.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Objects;

public class MemUserDatabase implements MemDataBaseAccessor<User> {
    ArrayList<User> database = new ArrayList<>();

    public boolean inDatabase(String username) {
        for (User user : database) {
            if (Objects.equals(user.username(), username)) {
                return true;
            }
        }
        return false;
    }

    public boolean passwordUsernameMatch(String password, String username) {
        for (User user : database) {
            if (Objects.equals(user.username(), username)) {
                return BCrypt.checkpw(password, user.password());
            }
        }
        return false;
    }

    @Override
    public boolean removeFromDatabase(User removeObject) {
        if (!inDatabase(removeObject.username())) {
            return false;
        }
        database.remove(removeObject);
        return true;
    }


    @Override
    public boolean addToDatabase(User addObject) {
        if (inDatabase(addObject.username())) {
            return false;
        }
        database.add(addObject);
        return true;
    }

    @Override
    public boolean deleteall() {
        database.clear();
        return database.size() <= 0;
    }
}
