package services;
import dataaccess.SQLTableControler;

//allows you to ch
public class DatabaseConfigLoader {
    private static boolean mI = true;
    public static boolean memoryimplementation()  {
        if(!mI){
            SQLTableControler.initialize();
        }

        return mI;


    }
}
