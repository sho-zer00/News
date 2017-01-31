package com.example.android.sample.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.sample.news.data.Site;
import com.example.android.sample.news.data.Link;

import java.util.ArrayList;
import java.util.List;


/**
 * 記事の登録、検索、削除を行うためのユーティリティクラスを作る
 */

public class RSSRepository {

    private RSSRepository(){

    }

    //データベースに値をそれぞれのデータのクラスに入れていく（登録しているサイトの表示用）
    public static long inSite(Context context,Site site){
        SQLiteDatabase database = new RSSDBHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RSSDBHelper.Site.TITLE,site.getTitle());
        values.put(RSSDBHelper.Site.DESC,site.getDescription());
        values.put(RSSDBHelper.Site.URL,site.getUrl());

        long id = database.insert(RSSDBHelper.Site.TABLE_NAME,null,values);

        database.close();

        return id;
    }

    //削除用のメソッド
    public static int delSite(Context context,long id){
        SQLiteDatabase database = new RSSDBHelper(context).getWritableDatabase();

        int affected = database.delete(
                RSSDBHelper.Site.TABLE_NAME,
                RSSDBHelper.Site.ID+" =? ",
                new String[]{String.valueOf(id)}
        );

        if(affected > 0){
            //記事を削除する
            database.delete(RSSDBHelper.Link.TABLE_NAME,
                    RSSDBHelper.Link.S_ID + " =? ",
                    new String[]{String.valueOf(id)});
        }

        database.close();

        return affected;
    }

    //Database読み取り
    public static List<Site> getAllSites(Context context){
        SQLiteDatabase database = new RSSDBHelper(context).getReadableDatabase();

        List<Site> sites = new ArrayList<>();

        Cursor cursor = database.query(RSSDBHelper.Site.TABLE_NAME,
                RSSDBHelper.Site.PROJECT,
                null,null,null,null,null,null);

        //データベースから取得した値をセッターに入れていく
        while (cursor.moveToNext()){
            Site site = new Site();
            long feedId = cursor.getLong(cursor.getColumnIndex(RSSDBHelper.Site.ID));

            site.setId(feedId);
            site.setTitle(cursor.getString(cursor.getColumnIndex(RSSDBHelper.Site.TITLE)));
            site.setDescription(cursor.getString(cursor.getColumnIndex(RSSDBHelper.Site.DESC)));
            site.setUrl(cursor.getString(cursor.getColumnIndex(RSSDBHelper.Site.URL)));

            //ひもづくリンク情報の件数を取得
            long linksCount = DatabaseUtils.queryNumEntries(database,
                    RSSDBHelper.Link.TABLE_NAME,RSSDBHelper.Link.S_ID+" =? ",
                    new String[]{String.valueOf(feedId)});

            site.setLinkCount(linksCount);

            sites.add(site);
        }

        cursor.close();

        database.close();

        return sites;
    }

    //データベースに値をそれぞれのデータのクラスに入れていく（記事一覧用）
    public static int inLink(Context context , long feedId, List<Link> links){
        SQLiteDatabase database = new RSSDBHelper(context).getReadableDatabase();

        int insertedRows = 0;

        for(Link link : links){
            ContentValues values = new ContentValues();
            values.put(RSSDBHelper.Link.TITLE,link.getTitle());
            values.put(RSSDBHelper.Link.DESC,link.getDescription());
            values.put(RSSDBHelper.Link.PUBLIC_DATE,link.getPubDate());
            values.put(RSSDBHelper.Link.L_URL,link.getURL());
            values.put(RSSDBHelper.Link.S_ID,feedId);

            long id = database.insertWithOnConflict(RSSDBHelper.Link.TABLE_NAME,
                    null,values,SQLiteDatabase.CONFLICT_IGNORE);

            if(id >= 0){
                //insert sucsess
                link.setId(id);
                insertedRows++;
            }
        }
        database.close();

        return  insertedRows;
    }

    //DataBase読み取り
    public static List<Link> getAllLinks(Context context){
        SQLiteDatabase database = new RSSDBHelper(context).getReadableDatabase();

        List<Link> links = new ArrayList<>();

        Cursor cursor = database.query(RSSDBHelper.Link.TABLE_NAME,
                RSSDBHelper.Link.PROJECT,null,null,null,null,
                RSSDBHelper.Link.PUBLIC_DATE+" DESC ",null);

        while(cursor.moveToNext()){
            Link link = new Link();

            link.setId(cursor.getLong(cursor.getColumnIndex(RSSDBHelper.Link.ID)));
            link.setTitle(cursor.getString(cursor.getColumnIndex(RSSDBHelper.Link.TITLE)));
            link.setDescription(cursor.getString(cursor.getColumnIndex(RSSDBHelper.Link.DESC)));
            link.setPubDate(cursor.getLong(cursor.getColumnIndex(RSSDBHelper.Link.PUBLIC_DATE)));
            link.setURL(cursor.getString(cursor.getColumnIndex(RSSDBHelper.Link.L_URL)));
            link.setSiteId(cursor.getLong(cursor.getColumnIndex(RSSDBHelper.Link.S_ID)));

            links.add(link);
        }

        cursor.close();

        database.close();

        return  links;
    }
}
