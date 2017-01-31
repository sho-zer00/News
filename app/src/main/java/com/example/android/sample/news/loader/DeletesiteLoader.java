package com.example.android.sample.news.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.sample.news.database.RSSRepository;

/**
 * delete information for site
 */

public class DeletesiteLoader extends AsyncTaskLoader<Integer> {

    //削除対象id
    private long id;

    public DeletesiteLoader(Context context,long id){
        super(context);
        this.id = id;
    }

    public long getTargetId(){
        return id;
    }

    @Override
    public Integer loadInBackground(){
        //サイトをデータベースから削除する
        return RSSRepository.delSite(getContext(),id);
    }
}
