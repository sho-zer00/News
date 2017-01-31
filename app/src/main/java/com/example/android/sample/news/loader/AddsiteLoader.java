package com.example.android.sample.news.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import com.example.android.sample.news.data.Link;
import com.example.android.sample.news.data.Site;
import com.example.android.sample.news.database.RSSRepository;
import com.example.android.sample.news.http.Httpget;
import com.example.android.sample.news.parser.RssParser;

import java.io.InputStream;
import java.util.List;

/**
 * 記事の一覧を取得するためのLoader
 */

public class AddsiteLoader extends AsyncTaskLoader<Site>{

    private String url;

    public AddsiteLoader(Context context , String url){
        super(context);
        this.url = url;
    }

    @Override
    public Site loadInBackground(){
        if(!TextUtils.isEmpty(this.url)){
            //RSSフィードをダウンロード
            Httpget httpget = new Httpget(this.url);
            if(!httpget.get()){
                //通信に失敗した時
                return  null;
            }

            //ダウンロードしたレスポンスの解析
            InputStream inputStream = httpget.getResponse();
            RssParser parser = new RssParser();

            if(!parser.parse(inputStream)){
                //解析に失敗した時
                return null;
            }

            //解析結果を取り出す
            Site site = parser.getSite();
            List<Link> links = parser.getLinkList();

            //DBに登録
            site.setUrl(url);
            site.setLinkCount(links.size());

            //サイトに登録
            long feedId = RSSRepository.inSite(getContext(),site);
            site.setId(feedId);

            if(feedId > 0 && links.size() > 0){
                //記事を登録する
                RSSRepository.inLink(getContext(),feedId,links);

                return site;
            }
        }
        return null;
    }
}
