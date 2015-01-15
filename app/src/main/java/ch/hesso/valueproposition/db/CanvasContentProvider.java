package ch.hesso.valueproposition.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.utils.Constants;

public class CanvasContentProvider extends ContentProvider {

    // Used for debugging and logging
    @SuppressWarnings("unused")
    private static final String TAG = CanvasContentProvider.class.getSimpleName();

    /**
     * The database that the provider uses as its underlying data store
     */
    private static final String DATABASE_NAME = "canvas.db";

    /**
     * The database version
     */
    private static final int DATABASE_VERSION = 1;

    /*
     * Constants used by the Uri matcher to choose an action based on the pattern of the incoming URI
	 */
    private static final int CANVAS    = 1;
    private static final int CANVAS_ID = 2;

    private static final int QUESTIONS   = 11;
    private static final int QUESTION_ID = 12;

    private static final int IDEAS   = 21;
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
        private Context context;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
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
            importQuestions(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS '" + DbObjects.Canvas.TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + DbObjects.Questions.TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + DbObjects.Ideas.TABLE + "'");
            onCreate(db);
        }

        private void importQuestions(SQLiteDatabase db) {
            try {
                db.beginTransaction();
                final String[] productServices = context.getResources().getStringArray(R.array.product_services);
                final String[] gainCreators = context.getResources().getStringArray(R.array.gain_creators);
                final String[] painRelievers = context.getResources().getStringArray(R.array.pain_relievers);
                final String[] customerJobs = context.getResources().getStringArray(R.array.customer_jobs);
                final String[] customerGains = context.getResources().getStringArray(R.array.customer_gains);
                final String[] customerPains = context.getResources().getStringArray(R.array.customer_pains);

                importQuestionsForElement(db, productServices, Constants.Elements.PRODUCTS_SERVICES);
                importQuestionsForElement(db, gainCreators, Constants.Elements.GAIN_CREATORS);
                importQuestionsForElement(db, painRelievers, Constants.Elements.PAIN_RELIEVERS);
                importQuestionsForElement(db, customerJobs, Constants.Elements.CUSTOMERS_JOBS);
                importQuestionsForElement(db, customerGains, Constants.Elements.CUSTOMER_GAINS);
                importQuestionsForElement(db, customerPains, Constants.Elements.CUSTOMER_PAINS);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, "Error while inserting questions");
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        private void importQuestionsForElement(SQLiteDatabase db, String[] questions, Constants.Elements element) {
            final String sql = "INSERT INTO " + DbObjects.Questions.TABLE + " (" + DbObjects.Questions.COL_DESC + ", " + DbObjects.Questions.COL_ELEMENT + ") VALUES (?, ?)";
            SQLiteStatement stm = db.compileStatement(sql);

            for (String question : questions) {
                stm.bindString(1, question);
                stm.bindLong(2, element.ordinal());
                stm.executeInsert();
            }

            stm.close();
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
                return DbObjects.Questions.CONTENT_ITEM_TYPE;

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
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case CANVAS:
                qb.setTables(DbObjects.Canvas.TABLE);
                qb.setProjectionMap(sCanvasProjectionMap);
                break;
            case CANVAS_ID:
                qb.setTables(DbObjects.Canvas.TABLE);
                qb.setProjectionMap(sCanvasProjectionMap);
                qb.appendWhere(DbObjects.Canvas._ID + "=" +
                        uri.getPathSegments().get(DbObjects.Canvas.CANVAS_ID_PATH_POSITION));
                break;
            case QUESTIONS:
                qb.setTables(DbObjects.Questions.TABLE);
                qb.setProjectionMap(sQuestionsProjectionMap);
                break;
            case QUESTION_ID:
                qb.setTables(DbObjects.Questions.TABLE);
                qb.setProjectionMap(sQuestionsProjectionMap);
                qb.appendWhere(DbObjects.Questions._ID + "=" +
                        uri.getPathSegments().get(DbObjects.Questions.QUESTION_ID_PATH_POSITION));
                break;
            case IDEAS:
                qb.setTables(DbObjects.Ideas.TABLE);
                qb.setProjectionMap(sIdeasProjectionMap);
                break;
            case IDEA_ID:
                qb.setTables(DbObjects.Ideas.TABLE);
                qb.setProjectionMap(sIdeasProjectionMap);
                qb.appendWhere(DbObjects.Canvas._ID + "=" +
                        uri.getPathSegments().get(DbObjects.Ideas.IDEA_ID_PATH_POSITION));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified, uses the default
        String orderBy = "";
        if (TextUtils.isEmpty(sortOrder)) {
            switch (sUriMatcher.match(uri)) {
                case CANVAS:
                case CANVAS_ID:
                    orderBy = DbObjects.Canvas.DEFAULT_SORT_ORDER;
                    break;
                case QUESTIONS:
                case QUESTION_ID:
                    orderBy = DbObjects.Questions.DEFAULT_SORT_ORDER;
                    break;
                case IDEAS:
                case IDEA_ID:
                    orderBy = DbObjects.Ideas.DEFAULT_SORT_ORDER;
                    break;
            }
        } else {
            orderBy = sortOrder;
        }
        // Opens the database object in "read" mode, since no writes need to be
        // done.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

		/*
         * Performs the query. If no problems occur trying to read the database, then a Cursor object is returned; otherwise, the cursor variable contains null. If no records were
		 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
		 */
        Cursor c = qb.query(db, // The database to query
                projection, // The columns to return from the query
                selection, // The columns for the where clause
                selectionArgs, // The values for the where clause
                null, // don't group the rows
                null, // don't filter by row groups
                orderBy // The sort order
        );

        // Tells the Cursor what URI to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null)
            values = new ContentValues(initialValues);
        else
            values = new ContentValues();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long now = System.currentTimeMillis();
        long insertedId;
        Uri insertedUri = null;

        switch (sUriMatcher.match(uri)) {
            case CANVAS:
                if (!values.containsKey(DbObjects.Canvas.COL_CREATED_AT))
                    values.put(DbObjects.Canvas.COL_CREATED_AT, now);
                insertedId = db.insert(DbObjects.Canvas.TABLE, null, values);
                insertedUri = ContentUris.withAppendedId(DbObjects.Canvas.CONTENT_ID_URI_BASE, insertedId);
                break;
            case QUESTIONS:
                if (!values.containsKey(DbObjects.Questions.COL_CREATED_AT))
                    values.put(DbObjects.Questions.COL_CREATED_AT, now);
                insertedId = db.insert(DbObjects.Questions.TABLE, null, values);
                insertedUri = ContentUris.withAppendedId(DbObjects.Questions.CONTENT_ID_URI_BASE, insertedId);
                break;
            case IDEAS:
                if (!values.containsKey(DbObjects.Ideas.COL_CREATED_AT))
                    values.put(DbObjects.Ideas.COL_CREATED_AT, now);
                insertedId = db.insert(DbObjects.Ideas.TABLE, null, values);
                insertedUri = ContentUris.withAppendedId(DbObjects.Ideas.CONTENT_ID_URI_BASE, insertedId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (insertedId == -1) throw new SQLException("Failed to insert row for " + uri);

        getContext().getContentResolver().notifyChange(insertedUri, null);
        return insertedUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updatedRows;
        String finalWhere;

        switch (sUriMatcher.match(uri)) {
            case CANVAS:
                updatedRows = db.update(DbObjects.Canvas.TABLE, values, selection, selectionArgs);
                break;
            case CANVAS_ID:
                String canvasId = uri.getPathSegments().get(DbObjects.Canvas.CANVAS_ID_PATH_POSITION);
                finalWhere = DbObjects.Canvas._ID + " = " + canvasId;
                if (selection != null) finalWhere = finalWhere + " AND " + selection;
                updatedRows = db.update(DbObjects.Canvas.TABLE, values, finalWhere, selectionArgs);
                break;
            case QUESTIONS:
                updatedRows = db.update(DbObjects.Questions.TABLE, values, selection, selectionArgs);
                break;
            case QUESTION_ID:
                String questionId = uri.getPathSegments().get(DbObjects.Questions.QUESTION_ID_PATH_POSITION);
                finalWhere = DbObjects.Questions._ID + " = " + questionId;
                if (selection != null) finalWhere = finalWhere + " AND " + selection;
                updatedRows = db.update(DbObjects.Questions.TABLE, values, finalWhere, selectionArgs);
                break;
            case IDEAS:
                updatedRows = db.update(DbObjects.Ideas.TABLE, values, selection, selectionArgs);
                break;
            case IDEA_ID:
                String ideaId = uri.getPathSegments().get(DbObjects.Ideas.IDEA_ID_PATH_POSITION);
                finalWhere = DbObjects.Ideas._ID + " = " + ideaId;
                if (selection != null) finalWhere = finalWhere + " AND " + selection;
                updatedRows = db.update(DbObjects.Ideas.TABLE, values, finalWhere, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (updatedRows != 0) getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deletedRows;
        String finalWhere;

        switch (sUriMatcher.match(uri)) {
            case CANVAS_ID:
                finalWhere = DbObjects.Canvas._ID + "=" + uri.getPathSegments().get(DbObjects.Canvas.CANVAS_ID_PATH_POSITION);
                if (selection != null) selection = finalWhere + " AND " + selection;
                else selection = finalWhere;
            case CANVAS:
                deletedRows = db.delete(DbObjects.Canvas.TABLE, selection, selectionArgs);
                break;
            case QUESTION_ID:
                finalWhere = DbObjects.Questions._ID + "=" + uri.getPathSegments().get(DbObjects.Questions.QUESTION_ID_PATH_POSITION);
                if (selection != null) selection = finalWhere + " AND " + selection;
                else selection = finalWhere;
            case QUESTIONS:
                deletedRows = db.delete(DbObjects.Questions.TABLE, selection, selectionArgs);
                break;
            case IDEA_ID:
                finalWhere = DbObjects.Ideas._ID + "=" + uri.getPathSegments().get(DbObjects.Ideas.IDEA_ID_PATH_POSITION);
                if (selection != null) selection = finalWhere + " AND " + selection;
                else selection = finalWhere;
            case IDEAS:
                deletedRows = db.delete(DbObjects.Ideas.TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (deletedRows > 0) getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }
}
