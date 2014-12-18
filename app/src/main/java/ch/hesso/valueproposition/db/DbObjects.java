package ch.hesso.valueproposition.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Definition of db entities
 */
public final class DbObjects {

    public static final String AUTHORITY = "ch.hesso.valueproposition.providers.canvas";

    public static final class Canvas implements BaseColumns {

        private Canvas() {}

        public static final String TABLE = "canvas";

        // URI definitions
        /**
         * The scheme part for this provider's URI
         */
        private static final String SCHEME = "content://";

        /**
         * Path part for the canvas URI
         */
        private static final String PATH_CANVAS = "/canvas";

        /**
         * Path part for the canvas ID URI
         */
        private static final String PATH_CANVAS_ID = "/canvas/";

        /**
         * 0-relative position of a canvas ID segment in the path part of a canvas ID URI
         */
        public static final int CANVAS_ID_PATH_POSITION = 1;

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of canvas.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ch.hesso.valueproposition.canvas";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * canvas.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ch.hesso.valueproposition.canvas";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_CANVAS);

        /**
         * The content URI base for a single canvas. Callers must
         * append a numeric canvas id to this Uri to retrieve a canvas
         */
        public static final Uri CONTENT_ID_URI_BASE
                = Uri.parse(SCHEME + AUTHORITY + PATH_CANVAS_ID);

        /**
         * The content URI match pattern for a single canvas, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
                = Uri.parse(SCHEME + AUTHORITY + PATH_CANVAS_ID + "/#");

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "created_at DESC";

        // Columns definition
        public static final String COL_TITLE = "title";
        public static final String COL_DESC = "description";
        public static final String COL_CREATED_AT = "created_at";

    }

    public static final class Questions implements BaseColumns {

        private Questions() {}

        public static final String TABLE = "questions";

        // URI definitions
        /**
         * The scheme part for this provider's URI
         */
        private static final String SCHEME = "content://";

        /**
         * Path part for the question URI
         */
        private static final String PATH_QUESTIONS = "/questions";

        /**
         * Path part for the question ID URI
         */
        private static final String PATH_QUESTION_ID = "/questions/";

        /**
         * 0-relative position of a question ID segment in the path part of a question ID URI
         */
        public static final int QUESTION_ID_PATH_POSITION = 1;

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of ideas.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ch.hesso.valueproposition.questions";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * idea.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ch.hesso.valueproposition.questions";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_QUESTIONS);

        /**
         * The content URI base for a single question. Callers must
         * append a numeric question id to this Uri to retrieve an question
         */
        public static final Uri CONTENT_ID_URI_BASE
                = Uri.parse(SCHEME + AUTHORITY + PATH_QUESTION_ID);

        /**
         * The content URI match pattern for a single question, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
                = Uri.parse(SCHEME + AUTHORITY + PATH_QUESTION_ID + "/#");

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "created_at DESC";

        // Columns definition
        public static final String COL_DESC = "content";
        public static final String COL_ELEMENT = "element";
        public static final String COL_CREATED_AT = "created_at";

    }

    public static final class Ideas implements BaseColumns {

        private Ideas() {}

        public static final String TABLE = "ideas";

        // URI definitions
        /**
         * The scheme part for this provider's URI
         */
        private static final String SCHEME = "content://";

        /**
         * Path part for the ideas URI
         */
        private static final String PATH_IDEAS = "/ideas";

        /**
         * Path part for the ideas ID URI
         */
        private static final String PATH_IDEA_ID = "/ideas/";

        /**
         * 0-relative position of a ideas ID segment in the path part of a idea ID URI
         */
        public static final int IDEA_ID_PATH_POSITION = 1;

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of ideas.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ch.hesso.valueproposition.ideas";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * idea.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ch.hesso.valueproposition.ideas";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_IDEAS);

        /**
         * The content URI base for a single idea. Callers must
         * append a numeric idea id to this Uri to retrieve an idea
         */
        public static final Uri CONTENT_ID_URI_BASE
                = Uri.parse(SCHEME + AUTHORITY + PATH_IDEA_ID);

        /**
         * The content URI match pattern for a single idea, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
                = Uri.parse(SCHEME + AUTHORITY + PATH_IDEA_ID + "/#");

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "created_at DESC";

        // Columns definition
        public static final String COL_DESC = "content";
        public static final String COL_ELEMENT = "element";
        public static final String COL_CANVAS = "canvas_id";
        public static final String COL_CREATED_AT = "created_at";

    }

}
