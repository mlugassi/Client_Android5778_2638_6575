package lugassi.wallach.client_android5778_2638_6575.model.backend;

/**
 * Created by Netanel on 21/11/2017.
 */

public class DBManagerFactory {
    static DB_manager manager = null;

    public static DB_manager getManager() {
        if (manager == null)
            // manager = new DB_List();
            manager = new DB_SQL();
        return manager;
    }
}
