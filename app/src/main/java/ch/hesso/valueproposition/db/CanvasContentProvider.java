package ch.hesso.valueproposition.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.HashMap;

public class CanvasContentProvider extends ContentProvider {

    // Used for debugging and logging
    @SuppressWarnings("unused")
    private static final String				TAG					= CanvasContentProvider.class.getSimpleName();

    /**
     * The database that the provider uses as its underlying data store
     */
    private static final String				DATABASE_NAME		= "canvas.db";

    /**
     * The database version
     */
    private static final int				DATABASE_VERSION	= 1;

    /*
     * Constants used by the Uri matcher to choose an action based on the pattern of the incoming URI
	 */
    private static final int CANVAS = 1;
    private static final int CANVAS_ID = 2;

    private static final int QUESTIONS = 11;
    private static final int QUESTION_ID = 12;

    private static final int IDEAS = 21;
    private static final int IDEA_ID = 22;

    /**
     * projection map used to select columns from the database
     */
    private static HashMap<String, String> sCanvasProjectionMap;
    private static HashMap<String, String> sQuestionsProjectionMap;
    private static HashMap<String, String> sIdeasProjectionMap;

    private static final UriMatcher sUriMatcher;

    static {
        // Creates and initializes the URI matcher
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DbObjects.AUTHORITY, "canvas", CANVAS);
        sUriMatcher.addURI(DbObjects.AUTHORITY, "canvas/#", CANVAS_ID);

        sUriMatcher.addURI(DbObjects.AUTHORITY, "questions", QUESTIONS);
        sUriMatcher.addURI(DbObjects.AUTHORITY, "questions/#", QUESTION_ID);

        sUriMatcher.addURI(DbObjects.AUTHORITY, "ideas", IDEAS);
        sUriMatcher.addURI(DbObjects.AUTHORITY, "ideas/#", IDEA_ID);

        // Creates new projection map instances. The map returns a column name given a string. The two are usually equal.
        sCanvasProjectionMap = new HashMap<String, String>();
        sCanvasProjectionMap.put(DbObjects.Canvas._ID, DbObjects.Canvas._ID);
        sCanvasProjectionMap.put(DbObjects.Canvas.COL_TITLE, DbObjects.Canvas.COL_TITLE);
        sCanvasProjectionMap.put(DbObjects.Canvas.COL_DESC, DbObjects.Canvas.COL_DESC);
        sCanvasProjectionMap.put(DbObjects.Canvas.COL_CREATED_AT, DbObjects.Canvas.COL_CREATED_AT);

        sQuestionsProjectionMap = new HashMap<String, String>();
        sQuestionsProjectionMap.put(DbObjects.Questions._ID, DbObjects.Questions._ID);
        sQuestionsProjectionMap.put(DbObjects.Questions.COL_DESC, DbObjects.Questions.COL_DESC);
        sQuestionsProjectionMap.put(DbObjects.Questions.COL_ELEMENT, DbObjects.Questions.COL_ELEMENT);
        sQuestionsProjectionMap.put(DbObjects.Questions.COL_CREATED_AT, DbObjects.Questions.COL_CREATED_AT);

        sIdeasProjectionMap = new HashMap<String, String>();
        sIdeasProjectionMap.put(DbObjects.Ideas._ID, DbObjects.Canvas._ID);
        sIdeasProjectionMap.put(DbObjects.Ideas.COL_DESC, DbObjects.Ideas.COL_DESC);
        sIdeasProjectionMap.put(DbObjects.Ideas.COL_CANVAS, DbObjects.Ideas.COL_CANVAS);
        sIdeasProjectionMap.put(DbObjects.Ideas.COL_ELEMENT, DbObjects.Ideas.COL_ELEMENT);
        sIdeasProjectionMap.put(DbObjects.Ideas.COL_CREATED_AT, DbObjects.Ideas.COL_CREATED_AT);
    }

    private DbHelper mDbHelper;

    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL("CREATE TABLE " + DbObjects.Canvas.TABLE + "(" +
                    DbObjects.Canvas._ID + " INTEGER PRIMARY KEY, " +
                    DbObjects.Canvas.COL_TITLE + " TEXT, " +
                    DbObjects.Canvas.COL_DESC + " TEXT, " +
                    DbObjects.Canvas.COL_CREATED_AT + " INTEGER);");
            db.execSQL("CREATE TABLE " + DbObjects.Questions.TABLE + "(" +
                    DbObjects.Questions._ID + " INTEGER PRIMARY KEY, " +
                    DbObjects.Questions.COL_DESC + " TEXT, " +
                    DbObjects.Questions.COL_ELEMENT + " INTEGER, " +
                    DbObjects.Questions.COL_CREATED_AT + " INTEGER);");
            db.execSQL("CREATE TABLE " + DbObjects.Ideas.TABLE + "(" +
                    DbObjects.Ideas._ID + " INTEGER PRIMARY KEY, " +
                    DbObjects.Ideas.COL_DESC + " TEXT, " +
                    DbObjects.Ideas.COL_CANVAS + " INTEGER, " +
                    DbObjects.Ideas.COL_ELEMENT + " INTEGER, " +
                    DbObjects.Ideas.COL_CREATED_AT + " INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS '" + DbObjects.Canvas.TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + DbObjects.Questions.TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + DbObjects.Ideas.TABLE + "'");
            onCreate(db);
        }
    }

    public CanvasContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CANVAS:
                return DbObjects.Canvas.CONTENT_TYPE;
            case CANVAS_ID:
                return DbObjects.Canvas.CONTENT_ITEM_TYPE;

            case QUESTIONS:
                return DbObjects.Questions.CONTENT_TYPE;
            case QUESTION_ID:
                return  DbObjects.Questions.CONTENT_ITEM_TYPE;

            case IDEAS:
                return DbObjects.Ideas.CONTENT_TYPE;
            case IDEA_ID:
                return DbObjects.Ideas.CONTENT_ITEM_TYPE;

            // If the URI pattern doesn't match any permitted patterns, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
