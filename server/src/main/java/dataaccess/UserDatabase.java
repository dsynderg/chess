package dataaccess;

import modules.User;

import java.util.ArrayList;
import java.util.Objects;

public class UserDatabase implements DataBaseAccessor<User> {
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
                return Objects.equals(user.password(), password);
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
