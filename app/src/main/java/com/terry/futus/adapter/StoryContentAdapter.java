package com.terry.futus.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.terry.futus.R;
import com.terry.futus.StoryApp;
import com.terry.futus.bean.StoryBean;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
public class StoryContentAdapter extends BaseAdapter {
    private ArrayList<StoryBean> mStoBeans;
    private LayoutInflater mInflater;
    public StoryContentAdapter(Activity activity) {
        mStoBeans = new ArrayList<StoryBean>();
        mInflater = LayoutInflater.from(activity);
    }

    public void setData(List<StoryBean> beans) {
        if (beans != null) {
            mStoBeans.clear();
            mStoBeans.addAll(beans);
        }
        notifyDataSetChanged();
    }

    public void addData(List<StoryBean> beans) {
        if (beans != null) {
            mStoBeans.addAll(beans);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStoBeans.size();
    }

    @Override
    public StoryBean getItem(int position) {
        return mStoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        StoryBean storyBean = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.story_list_item, null);
            viewHolder.mDrawImg = (SimpleDraweeView) convertView.findViewById(R.id.mDrawImg);
            viewHolder.title_story = (TextView) convertView.findViewById(R.id.title_story);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            StoryBean bean = StoryApp.mDbManager.selector(StoryBean.class).where("title", "like", "%"+storyBean.getTitle()+"%").findFirst();
            if (bean != null) {
//                LogUtil.w(bean.toString());
                storyBean = bean;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        String url = storyBean.getImg();

        if (!TextUtils.isEmpty(url)) {
            viewHolder.mDrawImg.setImageURI(Uri.parse(url));
        }else {
            viewHolder.mDrawImg.setImageURI(null);
        }
        viewHolder.title_story.setText(storyBean.getTitle());
        int isRead = storyBean.getIsRead();
        if (isRead == 0) {
            viewHolder.title_story.setTextColor(Color.BLACK);
        }else {
            viewHolder.title_story.setTextColor(Color.GRAY);
        }
            return convertView;
        }

    class ViewHolder{
        SimpleDraweeView mDrawImg;
        TextView title_story;
    }
}
