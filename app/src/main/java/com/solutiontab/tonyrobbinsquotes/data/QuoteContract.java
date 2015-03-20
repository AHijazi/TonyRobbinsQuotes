package com.solutiontab.tonyrobbinsquotes.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abdurrahman Hijazi on 07-Mar-15.
 */
public class QuoteContract {


    public static final String CONTENT_AUTHORITY = "com.solutiontab.tonyrobbinsquotes";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_QUOTE = "quotes_table";

    public static final String PATH_TOPIC = "topics_table";

    public static final class QuoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "quotes_table";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUOTE).build();
        public static final String COLUMN_QUOTE_TEXT = "text";        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUOTE;
        public static final String COLUMN_QUOTE_IMAGE_TITLE = "image";        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUOTE;
        public static final String COLUMN_QUOTE_CATEGORY = "category";
        public static final String COLUMN_READ = "read";
        public static final String COLUMN_FAVOURITE = "favourite";

        public static Uri buildQuoteUri() {
            return CONTENT_URI;
        }

        public static Uri buildQuoteWithTopic(int topic_id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(topic_id)).build();
        }

        /// maybe not useful
        public static Uri buildQuoteWithId(long id) {
            return CONTENT_URI.buildUpon().appendQueryParameter(_ID, Long.toString(id)).build();
        }
//        public static Uri buildQuoteUri (long id){
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }

        public static String getTopicFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getFavoriteFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static long getIdFromUri(Uri uri) {
            String idString = uri.getQueryParameter(_ID);

            if (null != idString && idString.length() > 0)
                return Long.parseLong(idString);
            else
                return 0;
        }






    }


    public static final class TopicEntry implements BaseColumns {

        public static final String TABLE_NAME = "topics_table";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOPIC).build();
        public static final String COLUMN_TOPIC_TEXT = "text";        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOPIC;

        public static Uri buildTopicUri() {
            return CONTENT_URI;
        }        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOPIC;

        public static String getTopicFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }







    }


}
