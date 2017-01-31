package com.example.android.sample.news.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.sample.news.data.Link;
import com.example.android.sample.news.database.RSSRepository;

import java.util.List;

/**
 * リンクの一覧を取得する
 */

public class LinklistLoader extends AsyncTaskLoader<List<Link>>{

    public LinklistLoader(Context context){
        super(context);
    }

    @Override
    public List<Link> loadInBackground(){
        //登録されているリンクを全て取得する
        return RSSRepository.getAllLinks(getContext());
    }
}
