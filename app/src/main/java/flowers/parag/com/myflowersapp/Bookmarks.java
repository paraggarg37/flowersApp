package flowers.parag.com.myflowersapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Parag on 09/04/16.
 */
public class Bookmarks extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bookmark.db";
    private static final int DATABASE_VERSION = 1;
    public static final String BOOKMARK_TABLE_NAME = "FlowersBookmark";
    public static final String BOOKMARK_COLUMN_NAME = "name";
    public static final String BOOKMARK_COLUMN_URL = "url";
    public static  final String TAG = "bookamrks";


    public Bookmarks(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    private static Bookmarks sInstance;

    // ...

    public static synchronized Bookmarks getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new Bookmarks(context.getApplicationContext());
        }
        return sInstance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG,"oncreate called");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BOOKMARK_TABLE_NAME + "(" +
                BOOKMARK_COLUMN_NAME + " TEXT PRIMARY KEY, " +
                BOOKMARK_COLUMN_URL + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOKMARK_TABLE_NAME);
        onCreate(db);
    }

    public void insertBookmark(String name, String url) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BOOKMARK_COLUMN_NAME, name);
            contentValues.put(BOOKMARK_COLUMN_URL, url);

            db.insertOrThrow(BOOKMARK_TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }

    }

    public synchronized int checkBookmarkExists(String name) {
        SQLiteDatabase db = getReadableDatabase();

        int count =  (int)DatabaseUtils.queryNumEntries(db, BOOKMARK_TABLE_NAME,
                BOOKMARK_COLUMN_NAME + "=?", new String[] {name});
        db.close();
        return count;

    }

    public Integer deleteBookmark(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BOOKMARK_TABLE_NAME,
                BOOKMARK_COLUMN_NAME + " = ? ",
                new String[]{name});
    }
}
