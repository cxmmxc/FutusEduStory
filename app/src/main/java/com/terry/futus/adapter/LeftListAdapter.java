package com.terry.futus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.terry.futus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry.Chen on 2015/7/8 10:50.
 * Description:
 * Email:cxm_lmz@163.com
 */
public class LeftListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mList;

    public LeftListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<String>();
    }

    public void setData(List<String> list) {
        if (list != null)
            mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.drawer_list_item_layout, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position));
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
