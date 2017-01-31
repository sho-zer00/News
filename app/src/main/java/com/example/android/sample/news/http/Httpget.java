package com.example.android.sample.news.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 指定したURLからダウンロードする
 */

public class Httpget {

    // 接続のタイムアウト（ミリ秒）
    private static final int CONNECT_TIMEOUT_MS = 3000;
    // 読み込みのタイムアウト（ミリ秒）
    private static final int READ_TIMEOUT_MS = 5000;

    // 接続先URL
    private String url;
    // HTTPステータスコード
    private int status;
    // レスポンスの入力ストリーム
    private InputStream in;

    public Httpget(String url) {
        this.url = url;
    }

    public boolean get() {
        try {
            URL url = new URL(this.url);

            // 通信の設定を行う
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET"); // メソッド
            c.setConnectTimeout(CONNECT_TIMEOUT_MS); // 接続のタイムアウト
            c.setReadTimeout(READ_TIMEOUT_MS);  // 読み込みのタイムアウト
            c.setInstanceFollowRedirects(true); // Redirect許可

            // 接続
            c.connect();

            // ステータスコードの取得
            status = c.getResponseCode();

            if (status >= 200 && status < 300) {
                // 成功したら、レスポンスの入力ストリームを、
                // BufferedInputStreamとして参照をもつ
                in = new BufferedInputStream(c.getInputStream());
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 失敗
        return false;
    }

    // レスポンスの入力ストリームを返す
    public InputStream getResponse() {
        return in;
    }

    // HTTPステータスコードを返す
    public int getStatus() {
        return status;
    }

}
