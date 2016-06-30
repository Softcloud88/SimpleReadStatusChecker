package com.softcloud.simplereadstatus;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softcloud.simplereadstatus.newsStatusUtils.ReadStatusHelper;
import com.softcloud.simplereadstatus.newsStatusUtils.ReadableManager;

import java.util.List;

/**
 * Created by j-renzhexin on 2016/6/30.
 */
public class NewsAdapter extends BaseAdapter implements ReadableManager<News>{

    private List<News> newsList;
    private Activity activity;
    private ReadStatusHelper<News> statusHelper;

    public NewsAdapter(Activity activity) {
        this.activity = activity;
        statusHelper = ReadStatusHelper.create(activity, this);
    }

    public void setData(List<News> newsList) {
        this.newsList = newsList;
        statusHelper.checkReadStatus(this.newsList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return newsList != null ? newsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return newsList != null ? newsList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_news, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return viewHolder.getViewWithData(newsList.get(position));
    }

    @Override
    public String getContentMarker(News readable) {
        return readable != null ? readable.getTitle() : "null news";
    }

    @Override
    public void markRead(News readable) {
        readable.setHasRead(true);
    }

    @Override
    public void markNotRead(News readable) {
        readable.setHasRead(false);
    }

    @Override
    public void onCheckFinish(List<News> readables) {
        notifyDataSetChanged();
    }

    private class ViewHolder {
        View rootView;
        TextView tvTitle;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            initViews();
        }
        private void initViews() {
            tvTitle = (TextView) rootView.findViewById(R.id.text_title);
        }

        private View getViewWithData(final News news) {
            if (tvTitle != null) {
                tvTitle.setText(news.getTitle());
                tvTitle.setTextColor(news.isHasRead() ? activity.getResources()
                        .getColor(R.color.color_Light_grey) : activity.getResources().getColor(R.color.color_black));
                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusHelper.addReadable(news);
                        news.setHasRead(true);
                        notifyDataSetChanged();
                    }
                });
            }
            return rootView;
        }
    }
}
