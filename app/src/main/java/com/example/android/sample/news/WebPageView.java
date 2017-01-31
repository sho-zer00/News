package com.example.android.sample.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

/**
 * 横幅が広い画面のときにWebViewでページ表示
 */

public class WebPageView extends WebViewFragment{
    //このフラグメントのインスタンスを返す
    public static WebPageView newInstance(String url){
        WebPageView view = new WebPageView();

        Bundle args = new Bundle();
        args.putString("url",url);
        view.setArguments(args);

        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,Bundle savedInstanceState){
        WebView webView = (WebView)super.onCreateView(inflater,group,savedInstanceState);

        Bundle args = getArguments();
        String url = args.getString("url");

        //指定したURLをロードする
        webView.loadUrl(url);

        return  webView;
    }
}


