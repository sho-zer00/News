package com.example.android.sample.news;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sample.news.adapter.LinkAdapter;
import com.example.android.sample.news.data.Link;
import com.example.android.sample.news.loader.LinklistLoader;

import java.util.List;

/**
 * 記事一覧を表示するFragment
 */

public class LinklistFragment extends Fragment
                implements LoaderManager.LoaderCallbacks<List<Link>>, LinkAdapter.OnItemClickListener{

    //LoaderのID
    private static final int LOADER_LINKS = 1;

    //リストがタップされた時のリスナー
    public interface LinklistFragmentListener{
        void onLinkClicked(@NonNull Link link);
    }

    private LinkAdapter mAdapter;

    //Activityがインタフェースを実装しているかチェックする
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (!(context instanceof LinklistFragmentListener)) {
            // ActivityがArticlesFragmentListenerを実装していない場合
            throw new RuntimeException(context.getClass().getSimpleName()
                    + " does not implement LinklistFragmentListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Loaderを初期化する
        getLoaderManager().initLoader(LOADER_LINKS,null,this);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        //Loaderを破棄
        getLoaderManager().destroyLoader(LOADER_LINKS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        //Viewを生成する
        View v = inflater.inflate(R.layout.fragment_links,container,false);

        Context context = inflater.getContext();

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.LinkList);
        //必ずLayoutManagerを設定する
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context,LinearLayoutManager.VERTICAL,false
        ));
        mAdapter = new LinkAdapter(context);
        //リストのタップイベントを、一旦フラグメントを受け取る
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    //リストアイテムがタップされた時のイベント
    @Override
    public void onItemClick(Link link){
        LinklistFragmentListener listener = (LinklistFragmentListener)getActivity();
        if(listener != null){
            //アクティビティにタップされたリンクを伝える
            listener.onLinkClicked(link);
        }
    }

    //RSS配信サイトが削除された時に、それにひもづく記事も削除する
    public void removeLinks(long siteId){
        mAdapter.removeItem(siteId);
    }

    //RSS配信サイトが追加された時に、同時にそのフィードのリンクもリストに反映する
    public void reload(){
        mAdapter.clearItems();

        //すでにLoaderが作られているならそれを使う
        Loader loader = getLoaderManager().getLoader(LOADER_LINKS);
        if(loader != null){
            loader.forceLoad();
        }else{
            getLoaderManager().restartLoader(LOADER_LINKS,null,this);
        }
    }

    @Override
    public Loader<List<Link>> onCreateLoader(int id,Bundle args){
        if(id == LOADER_LINKS){
            LinklistLoader loader = new LinklistLoader(getActivity());
            loader.forceLoad();
            return loader;
        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<List<Link>> loader,List<Link> data){
        int id = loader.getId();

        if(id == LOADER_LINKS && data != null && data.size()>0){
            mAdapter.addItems(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Link>> loader){}
}
