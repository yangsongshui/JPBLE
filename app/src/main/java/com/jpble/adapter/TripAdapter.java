package com.jpble.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.bean.Trip;

import java.util.List;

/**
 * 作者：omni20170501
 */

public class TripAdapter extends BaseExpandableListAdapter {
    List<Trip> mList;

    public TripAdapter(List<Trip> mList) {
        this.mList = mList;
    }

    //获取分组的个数
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getmList().size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getmList().get(childPosition);
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        Trip trip = (Trip) getGroup(groupPosition);
        Log.e("info",trip.getDate());
        groupViewHolder.trip_time.setText(trip.getDate());
        return convertView;
    }

    //        获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_info, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        Trip.TripInfo info = mList.get(groupPosition).getmList().get(childPosition);
        Log.e("info",info.toString());
        childViewHolder.time_info.setText(info.getTime() + "`" + info.getMinute()+ "");
        childViewHolder.time_distance.setText(info.getKm());
        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView trip_time;

        public GroupViewHolder(View itemView) {
            trip_time = (TextView) itemView.findViewById(R.id.trip_time);


        }
    }

    static class ChildViewHolder {
        TextView time_info, time_distance;

        public ChildViewHolder(View itemView) {
            time_info = (TextView) itemView.findViewById(R.id.time_info);
            time_distance = (TextView) itemView.findViewById(R.id.time_distance);
        }
    }
}
