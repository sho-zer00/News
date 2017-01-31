package com.example.android.sample.news;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;

import com.example.android.sample.news.data.Site;
import com.example.android.sample.news.database.RSSRepository;
import com.example.android.sample.news.http.Httpget;
import com.example.android.sample.news.parser.RssParser;

import java.util.List;

/**
 * 定期的に起動される、フィードの記事をダウンロードする
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PollingJob extends JobService {

    private JobParameters parameters;

    @Override
    public boolean onStartJob(JobParameters parameters){
        this.parameters = parameters;
        new DownloadTask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters parameters){
        return false;
    }
    private class DownloadTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... parameters){

            List<Site> sites = RSSRepository.getAllSites(PollingJob.this);

            int newArticles = 0;

            // すべての登録済みRSS配信サイトからダウンロードする
            for(Site site : sites) {
                Httpget httpGet = new Httpget(site.getUrl());

                // ダウンロードする
                if (!httpGet.get()) {
                    continue;
                }

                RssParser parser = new RssParser();

                // ダウンロードしたフィードを解析する
                if (!parser.parse(httpGet.getResponse())) {
                    continue;
                }

                newArticles += RSSRepository.inLink(PollingJob.this,
                        site.getId(), parser.getLinkList());

            }

            if (newArticles > 0) {
                // 新しいリンクがある場合には通知する
                NotificationTuti.notifyUpdate(PollingJob.this, newArticles);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            jobFinished(parameters, false);
        }

    }
}
