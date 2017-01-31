package com.example.android.sample.news;

import android.app.IntentService;
import android.content.Intent;

import com.example.android.sample.news.data.Site;
import com.example.android.sample.news.database.RSSRepository;
import com.example.android.sample.news.http.Httpget;
import com.example.android.sample.news.parser.RssParser;

import java.util.List;

/**
 * 定期的に起動される、フィードの記事をダウンロードするService
 */

public class PollingService extends IntentService {

    private static final String TAG = "PollingService";

    public PollingService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){

        List<Site> sites = RSSRepository.getAllSites(this);

        int newArticles = 0;

        //登録済みの全サイトからダウンロード
        for(Site site : sites){

            //RSSフィードをダウンロード
            Httpget httpget = new Httpget(site.getUrl());

            if(!httpget.get()){
                //ダウンロードに失敗
                continue;
            }

            RssParser parser = new RssParser();

            //ダウンロードしたRSSフィードを解析する
            if(!parser.parse(httpget.getResponse())){
                continue;
            }

            newArticles += RSSRepository.inLink(this,site.getId(),parser.getLinkList());
        }

        if(newArticles > 0){
            //新しいリンクがある場合は通知する
            NotificationTuti.notifyUpdate(this,newArticles);
        }

    }
}
