package com.solutiontab.tonyrobbinsquotes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by Abdurrahman Hijazi on 07-Mar-15.
 */
public class QuoteProvider extends ContentProvider {


    static final int QUOTE = 100;
    static final int QUOTE_WITH_TOPIC = 101;

    static final int QUOTE_ID = 200;
    static final int TOPIC = 300;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;
    QuoteDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QuoteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, QuoteContract.PATH_QUOTE + "#", QUOTE_ID);
        matcher.addURI(authority, QuoteContract.PATH_QUOTE, QUOTE);
        matcher.addURI(authority, QuoteContract.PATH_QUOTE + "*", QUOTE_WITH_TOPIC);


        matcher.addURI(authority, QuoteContract.PATH_TOPIC, TOPIC);


        return matcher;
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new QuoteDbHelper(getContext());

        try {
            mOpenHelper.createDataBase();
        } catch (IOException ioe) {
            // throw new Error("Unable to create database");

        }


        return true;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case QUOTE:
                return QuoteContract.QuoteEntry.CONTENT_TYPE;

            case QUOTE_WITH_TOPIC:
                return QuoteContract.QuoteEntry.CONTENT_TYPE;


            case QUOTE_ID:
                return QuoteContract.QuoteEntry.CONTENT_ITEM_TYPE;

            case TOPIC:
                return QuoteContract.TopicEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case QUOTE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuoteContract.QuoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            case QUOTE_WITH_TOPIC:
                retCursor = getQuotesWithTopic(uri, projection, sortOrder);

                break;


//   DONT NEED IT
//            case QUOTE_READ:
//                break;

            case TOPIC:

                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuoteContract.TopicEntry.TABLE_NAME,
                        projection,
                        selection,
                        null,
                        null,
                        null,
                        sortOrder
                );


                break;


            case QUOTE_ID:
                retCursor = getQuotesWithTopic(uri, projection, sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getQuotesWithTopic(Uri uri, String[] projection, String sortOrder) {
        String topic_id = QuoteContract.QuoteEntry.getTopicFromUri(uri);
        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(), projection, QuoteContract.QuoteEntry.COLUMN_QUOTE_CATEGORY + "=?", new String[]{topic_id}, null, null, sortOrder);
    }

    private Cursor getQuotesWithFavourite(Uri uri, String[] projection, String sortOrder) {
        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(), projection, QuoteContract.QuoteEntry.COLUMN_FAVOURITE + "=?", new String[]{"1"}, null, null, sortOrder);
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updatedRow;
        switch (match) {

            case QUOTE:
                updatedRow = db.update(QuoteContract.QuoteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (updatedRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRow;
    }
}
