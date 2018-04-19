package ananda.com.claimer;

import java.util.ArrayList;

/**
 * Created by anan_ on 4/17/2018.
 */

public class DataAccessLayer {
    public static String imagePath;
    private static ArrayList<Items> FirebaseDataItems = new ArrayList<Items>();
    public static String userId;
    public static Integer count;

    public static void AddItems(Items itm){

        FirebaseDataItems.add(itm);
    }

    public static ArrayList<Items> GetAllItems(){
        return FirebaseDataItems;
    }


}
