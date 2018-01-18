package lugassi.wallach.client_android5778_2638_6575.model.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.DataBaseConstants.CREATE_DB_TABLE;
import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.DataBaseConstants.DATABASE_NAME;
import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.DataBaseConstants.DATABASE_VERSION;
import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.DataBaseConstants.TABLE_NAME;

/**
 * Created by Michael on 18/01/2018.
 */

public class CarModelsDataBaseHelper extends SQLiteOpenHelper {
    public CarModelsDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
