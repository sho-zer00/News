package com.example.android.sample.news.data;

/**
 * Created by sho on 2016/11/27.
 */

/*
 * 記事を表すオブジェクト
 */
public class Link {
    //データベースの主キー
    private long id;
    //記事タイトル
    private String title;
    //概要
    private String description;
    //発行日
    private long pubDate;
    //記事へのリンクURL
    private String URL;
    //配信サイトのデータベースの主キー
    private long siteId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }


}
