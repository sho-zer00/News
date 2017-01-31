package com.example.android.sample.news.parser;


import android.provider.DocumentsContract;

import com.example.android.sample.news.data.Link;
import com.example.android.sample.news.data.Site;

import org.w3c.dom.Document;

import java.util.List;

/**
 * RSS 1.0 2.0 用のパーサーのインタフェース
 */

public interface FeedParser {

    //解析する
    boolean parse(Document document);

    //解析結果のSiteを取得する
    Site getSite();

    //解析結果のリンクリストを受け取る
    List<Link> getLinkList();
}
