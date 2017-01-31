package com.example.android.sample.news.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * データベースを作る
 */

public class RSSDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "RDB";
    private static final int DB_VERSION = 1;

    public static class Site{
        public static final String TABLE_NAME = "SITE";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESC = "description";
        public static final String URL = "url";

        public static final String[] PROJECT = new String[]{
                ID,TITLE,DESC,URL
        };

        private static final String CREATE_DB
                = "CREATE TABLE "+TABLE_NAME+" ( "
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + DESC + " TEXT, "
                + URL + " TEXT NOT NULL UNIQUE "
                + " ) ";
    }

    public static class Link{
        public static final String TABLE_NAME = "LINK";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESC = "description";
        public static final String PUBLIC_DATE = "pubDate";
        public static final String L_URL = "linkUrl";
        public static final String S_ID = "siteId";
        public static final String REGIST_TIME = "register_time";

        public static final String[] PROJECT = new String[]{
                ID,TITLE,DESC,PUBLIC_DATE,L_URL,S_ID,REGIST_TIME
        };

        private static final String CREATE_DB
                = "CREATE TABLE "+TABLE_NAME+"("
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT NOT NULL, "
                + DESC + " TEXT, "
                + PUBLIC_DATE +" INTEGER, "
                +L_URL+" TEXT NOT NULL UNIQUE, "
                +S_ID+" INTEGER NOT NULL, "
                +REGIST_TIME+" TIMESTAMP DEFAULT (DATETIME('now','localtime')) "
                +")";
    }

    public RSSDBHelper(Context context){super(context,DB_NAME,null,DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(Site.CREATE_DB);
        db.execSQL(Link.CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
