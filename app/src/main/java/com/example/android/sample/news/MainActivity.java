package com.example.android.sample.news;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.android.sample.news.data.Link;



public class MainActivity extends AppCompatActivity
        implements SiteListFragment.SiteListFragmentListener, LinklistFragment.LinklistFragmentListener{

    //定期フェッチのジョブID
    private static final int JOB_FETCH_FEED = 1;
    //ジョブの実行時間(ms)
    private static final long INTERVAL = 60L * 60 * 1000L;
    //２ペインの画面かどうか
    private boolean mIsDualPane = false;
    //ナビゲーションドロワートグル
    private ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsDualPane = findViewById(R.id.DualPaneContainer)!= null;

        //NavigationDrawerの設定を行う
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.app_name,R.string.app_name);

        //ドロワーのトグルを有効にする
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(mDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //定期実行するタスクを設定する
        setRepeatingTask();
    }

    private void setRepeatingTask(){
        //定期的に新しい記事がないかをチェックする
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //APILevelが２１以上の場合には、JobSchedularを使用
            ComponentName jobService = new ComponentName(this,PollingJob.class);

            JobInfo fetchJob = new JobInfo.Builder(JOB_FETCH_FEED,jobService)
                    .setPeriodic(INTERVAL)
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build();
            //Jobスケジューラを取得
            JobScheduler scheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);

            //ジョブを取得
            scheduler.schedule(fetchJob);
        }
        else
        {
            //api level が２１以下ならAlarmManagerを使用する
            Intent serviceIntent = new Intent(this,PollingService.class);
            //すでに登録されているかをチェック
            boolean isExist = (PendingIntent.getService(this,JOB_FETCH_FEED,serviceIntent,
                    PendingIntent.FLAG_NO_CREATE)!= null);

            if(!isExist){
                PendingIntent operation = PendingIntent.getService(
                        this,JOB_FETCH_FEED,serviceIntent,
                        PendingIntent.FLAG_NO_CREATE
                );
                //AlarmManagerの使用
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                long trigger = SystemClock.elapsedRealtime() + INTERVAL;

                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,trigger,INTERVAL,operation);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //ドロワーのトグルの状態を同期する
        if(mDrawerToggle != null){
            mDrawerToggle.syncState();
        }
    }
    //画面のむきなどの状態が変わった時に呼ばれる
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(mDrawerToggle != null){
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    //アクションバー内のメニューがタップされた時に呼ばれる
    //このイベントをドロワーのとぐるに流すことより、自動で開閉を行なってくれる
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onLinkClicked(@NonNull Link link){
        //リンクがタブされたら、リンク先のwebページを開く
        if(mIsDualPane){
            //横幅が広い場合には、WebViewViewをDetailに入れる
            WebPageView view = WebPageView.newInstance(link.getURL());
            getFragmentManager().beginTransaction()
                    .replace(R.id.DetailContainer,view)
                    .commit();
        }
        else{
            //横幅が狭い時はChrome Custom Tabsを使用

            //ツールバーの色
            int colorPrimary  =
                    ContextCompat.getColor(this,R.color.colorPrimary);

            //CustomTabsIntentを作成するためのビルダークラス
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

            //Webのタイトルを表示する
            CustomTabsIntent intent = builder.setShowTitle(true)
                    .setToolbarColor(colorPrimary)
                    .build();

            //Chrome Custom TabsでWebページをひらく
            intent.launchUrl(this, Uri.parse(link.getURL()));
        }
    }
    /*
    **以下の作業で登録済みのRSS配信サイトがデータベースから削除されると、そのRSS配信サイトフィードから得られたリンク
    * 情報も削除されていく
    * また、新しいRSS配信サイトが登録された時にはリンク情報を再取得して表示するようにしている
     */
    @Override
    public void onSiteDeleted(long id){
        LinklistFragment fragment = (LinklistFragment)getFragmentManager()
                .findFragmentById(R.id.Master);

        if(fragment != null){
            fragment.removeLinks(id);
        }
    }

    @Override
    public void onSiteAdded(){
        LinklistFragment fragment = (LinklistFragment)getFragmentManager()
                .findFragmentById(R.id.Master);

        if(fragment != null){
            fragment.reload();
        }
    }
}
