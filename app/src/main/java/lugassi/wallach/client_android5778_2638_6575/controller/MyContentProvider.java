package lugassi.wallach.client_android5778_2638_6575.controller;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import lugassi.wallach.client_android5778_2638_6575.model.backend.CarModelsDataBaseHelper;
import lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst;

import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.ContentProviderConstants.uriMatcher;
import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.DataBaseConstants.MODEL_CODE;
import static lugassi.wallach.client_android5778_2638_6575.model.datasource.CarRentConst.DataBaseConstants.TABLE_NAME;

public class MyContentProvider extends ContentProvider {
    CarModelsDataBaseHelper carModelsDataBaseHelper = null;
    protected SQLiteDatabase db;
    private static HashMap<String, String> PROJECTION;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        carModelsDataBaseHelper = new CarModelsDataBaseHelper(context);

        //-- Open DataBase Connection
        db = carModelsDataBaseHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CarRentConst.ContentProviderConstants.ALL:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case CarRentConst.ContentProviderConstants.SINGLE:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME, CarRentConst.DataBaseConstants.MODEL_CODE + " = " + id +
                        (!TextUtils.isEmpty(selection) ? "  AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CarRentConst.ContentProviderConstants.ALL:
                return "vnd.android.cursor.dir/vnd.lugassi.wallach.carmodels";
            case CarRentConst.ContentProviderConstants.SINGLE:
                return "vnd.android.cursor.item/vnd.lugassi.wallach.carmodels";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CarRentConst.ContentProviderConstants.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case CarRentConst.ContentProviderConstants.ALL:
                qb.setProjectionMap(PROJECTION);
                break;

            case CarRentConst.ContentProviderConstants.SINGLE:
                qb.appendWhere(CarRentConst.DataBaseConstants.MODEL_CODE + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    public boolean Exists(String _id) {
        Cursor cursor = db.rawQuery("select " + MODEL_CODE + " from " + TABLE_NAME + " where " + MODEL_CODE + "_id=%s",
                new String[]{_id});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CarRentConst.ContentProviderConstants.ALL:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;

            case CarRentConst.ContentProviderConstants.SINGLE:
                count = db.update(TABLE_NAME, values,
                        CarRentConst.DataBaseConstants.MODEL_CODE + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
