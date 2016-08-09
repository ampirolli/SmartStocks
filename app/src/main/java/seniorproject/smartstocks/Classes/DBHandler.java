package seniorproject.smartstocks.Classes;

/**
 * Created by Ampirollli on 8/8/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.Key;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    public static final String Table_Local_User = "Local_User";
    public static final String Key_Local_ID = "local_id";
    public static final String Key_User_ID = "user_id";
    public static final String Key_hasPin = "hasPin";
    public static final String Key_Pin = "pin";
    public static final String Key_IsLoggedIn = "isLoggedIn";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + Table_Local_User + " ( " +
            Key_Local_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Key_User_ID + " INTEGER NOT NULL, " +
            Key_hasPin + " INTEGER NOT NULL, " + Key_Pin + " INTEGER NOT NULL, " + Key_IsLoggedIn + " INTEGER NOT NULL "
            + ");";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
        }catch (SQLException e) {
            String exception = e.toString();
            String ex = exception;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Table_Local_User);
        onCreate(db);
    }

    public void Login(String userId) {

        if(doesUserExistQuery(userId) == true) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Key_User_ID, userId);
            values.put(Key_hasPin, false);
            values.put(Key_Pin, 1234);
            values.put(Key_IsLoggedIn, true);

            try {
                db.insert(Table_Local_User, null, values);

            } catch (SQLException e) {
                String exception = e.toString();
                String ex = exception;
            }
        }else{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Key_IsLoggedIn, true);

            try {
                db.update(Table_Local_User, values, (Key_User_ID +" = " + userId), null );

            } catch (SQLException e) {
                String exception = e.toString();
                String ex = exception;
            }
        }

    }

    public void Logoff(String userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_IsLoggedIn, false);

        try {
            db.update(Table_Local_User, values, (Key_User_ID +" = " + userId), null );

        } catch (SQLException e) {
            String exception = e.toString();
            String ex = exception;
        }
    }

    public boolean doesUserExistQuery(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select 1 from " + Table_Local_User + " where " + Key_User_ID + " = %s;",  new String[] { userId });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
