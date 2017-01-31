package com.example.android.sample.news.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.sample.news.data.Site;
import com.example.android.sample.news.database.RSSRepository;

import java.util.List;


/**
 * サイト一覧の取得
 */

public class SitelistLoader extends AsyncTaskLoader<List<Site>> {

    public SitelistLoader(Context context){
        super(context);
    }

    @Override
    public List<Site> loadInBackground(){
        //登録されているRSSフィード配信サイトを全て取得する
        return RSSRepository.getAllSites(getContext());
    }
}
