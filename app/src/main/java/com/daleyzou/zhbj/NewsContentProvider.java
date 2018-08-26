package com.daleyzou.zhbj;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class NewsContentProvider extends ContentProvider {

    private NewsSQLiteOpenHelper dbHelper = null;

    private static final UriMatcher MATCHER =  new UriMatcher(UriMatcher.NO_MATCH);
    private  static final int BOOK_TABLE = 1;
    static {

        MATCHER.addURI("com.daleyzou.zhbj","news_table",BOOK_TABLE);
    }


    @Override
    public boolean onCreate() {
        Log.d("Test","NewsContentProvider - onCreate");
        dbHelper = new NewsSQLiteOpenHelper(this.getContext(),"news.db",null,1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case BOOK_TABLE:
                return db.query("news_table", projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri.toString());

        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)){
            case BOOK_TABLE:
                return "vnd.android.cursor.dir/news_table";
            default:
                throw new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri insertUri = null;
        switch (MATCHER.match(uri)){
            case BOOK_TABLE:
                long rowId = db.insert("news_table", "", values);
                insertUri = ContentUris.withAppendedId(uri,rowId);
                return insertUri;
            default:
                throw new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
