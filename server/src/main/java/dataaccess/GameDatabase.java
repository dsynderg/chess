package dataaccess;

public class GameDatabase implements dataBaseAccessor  {

    public boolean inDatabase(Object searchObject) {
        return false;
    }

    @Override
    public boolean addToDatabase(Object addObject) {
        return false;
    }

    @Override
    public boolean removeFromDatabase(Object removeObject) {
        return false;
    }
    @Override
    public boolean deleteall() {
        database.clear();
        if(database.size()>0){
            return false;
        }
        return true;
    }
}
