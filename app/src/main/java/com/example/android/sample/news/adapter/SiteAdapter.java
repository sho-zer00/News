package com.example.android.sample.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.sample.news.R;
import com.example.android.sample.news.data.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * サイトリスト用のAdapter
 */

public class SiteAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Site> mSites;

    //コンストラクタで初期化
    public SiteAdapter(Context context){
        mContext =  context;
        mInflater = LayoutInflater.from(context);
        mSites = new ArrayList<>();
    }

    //サイトリストにサイトを追加する
    public void addItem(Site site){
        mSites.add(site);
        notifyDataSetChanged();
    }
    //サイトリストにサイトを全て追加する
    public void addAll(List<Site>sites){
        mSites.addAll(sites);
        notifyDataSetChanged();
    }

    //サイトリストからアイテムを取り除く
    public void removeItem(long siteId){
        int size = mSites.size();
        int position = -1;
        for(int i = 0; i<size;i++){
            Site site = mSites.get(i);
            if(siteId == site.getId()){
                mSites.remove(position);
                notifyDataSetChanged();
                return;
            }
        }
    }

    //リスト表示すべきアイテムの数を返す
    @Override
    public int getCount(){
        return mSites.size();
    }

    //positionに対するサイト情報を返す
    @Override
    public Site getItem(int position){
        return mSites.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    /**
     * 記事一覧のアイテム用ViewHolder
     */
    private static class SiteViewHolder {

        private TextView title;
        private TextView linksCount;

        public SiteViewHolder(View itemView) {
            title = (TextView) itemView.findViewById(R.id.Title);
            linksCount = (TextView) itemView.findViewById(R.id.ArticlesCount);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SiteViewHolder holder;
        View itemView;

        if (convertView == null) {
            itemView = mInflater.inflate(R.layout.item_site, parent, false);
            holder = new SiteViewHolder(itemView);
            itemView.setTag(holder);

        }
        else {
            itemView = convertView;
            holder = (SiteViewHolder)convertView.getTag();
        }

        Site site = getItem(position);

        holder.title.setText(site.getTitle());
        holder.linksCount.setText(
                mContext.getString(R.string.site_link_count, site.getLinkCount()));

        return itemView;
    }

}
