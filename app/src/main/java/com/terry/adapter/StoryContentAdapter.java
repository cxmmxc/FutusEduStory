package com.terry.adapter;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.terry.R;
import com.terry.bean.StoryBean;

import java.util.ArrayList;

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

    public void setData(ArrayList<StoryBean> beans) {
        if (beans != null) {
            mStoBeans.clear();
            mStoBeans.addAll(beans);
        }
        notifyDataSetChanged();
    }

    public void addData(ArrayList<StoryBean> beans) {
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
        String url = storyBean.getPicUrl();

        if (!TextUtils.isEmpty(url)) {
            viewHolder.mDrawImg.setImageURI(Uri.parse(url));
        }else {
            viewHolder.mDrawImg.setImageURI(null);
        }
        viewHolder.title_story.setText(storyBean.getTitle());
            return convertView;
        }

    class ViewHolder{
        SimpleDraweeView mDrawImg;
        TextView title_story;
    }
}
