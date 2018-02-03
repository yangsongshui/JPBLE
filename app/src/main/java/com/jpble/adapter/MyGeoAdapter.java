package com.jpble.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpble.OnItemListener;
import com.jpble.R;
import com.jpble.bean.Sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by omni20170501 on 2017/6/6.
 */

public class MyGeoAdapter extends RecyclerView.Adapter<MyGeoAdapter.ViewHolder> {
    private List<Sim.DataBean> data;
    private Context context;
    //是否显示单选框,默认false
    private boolean isshowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    private OnItemListener onItemCheckListener;

    public MyGeoAdapter(Context context) {
        data = new ArrayList<>();
        this.context = context;
        initMap();
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        map.clear();
        for (int i = 0; i < data.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_geo, parent, false);
        return new MyGeoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Sim.DataBean bleDevice = data.get(position);

        holder.geo_cb.setChecked(map.get(position));
        holder.deviceName.setText(bleDevice.getLockVo().getName());
        //设置checkBox改变监听
        holder.geo_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.put(position, isChecked);

            }
        });
        holder.geo_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCheckListener != null) {
                    onItemCheckListener.OnItemCheck(holder, position, data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private RelativeLayout geo_rl;
        private CheckBox geo_cb;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.geo_name);
            geo_rl = (RelativeLayout) itemView.findViewById(R.id.geo_rl);
            geo_cb = (CheckBox) itemView.findViewById(R.id.geo_cb);
        }
    }

    public void setItems(List<Sim.DataBean> data) {
        this.data = data;
        initMap();
        this.notifyDataSetChanged();
    }

    public void clearDevice() {
        data.clear();
        notifyDataSetChanged();
    }

    public Sim.DataBean getDevice(int position) {
        return data.get(position);
    }

    public void setDevice(Sim.DataBean device) {
        data.add(data.size(), device);
        notifyItemInserted(data.size());
    }

    public void reDevice(int position) {
        if (data.size() >= position) {
            data.remove(position);
        }
        notifyDataSetChanged();
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

    public void setOnItemCheckListener(OnItemListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }
}
