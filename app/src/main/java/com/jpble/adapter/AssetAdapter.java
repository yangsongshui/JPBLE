package com.jpble.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpble.OnItemListener;
import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.bean.Sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by omni20170501 on 2017/6/6.
 */

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {
    private List<Sim.DataBean> data;
    private Context context;
    private OnItemListener onItemCheckListener;

    public AssetAdapter(Context context) {

        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new AssetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Sim.DataBean bleDevice = data.get(position);
        holder.listRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCheckListener != null) {
                    onItemCheckListener.OnItemCheck(holder, position, data.get(position));
                }
            }
        });

        holder.deviceName.setText(bleDevice.getLockVo().getName());
        if (MyApplication.newInstance().bindMac != null && MyApplication.newInstance().bindMac.equals(bleDevice.getLockVo().getMac())) {
            holder.listBle.setImageResource(R.drawable.ble);
        } else
            holder.listBle.setImageResource(R.drawable.ble_false);
        holder.listSatellite.setImageResource(getIconId(map.get(bleDevice.getLockVo().getMac())));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private ImageView listSatellite, listBle;
        private RelativeLayout listRl;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.list_name);
            listBle = (ImageView) itemView.findViewById(R.id.list_ble);
            listSatellite = (ImageView) itemView.findViewById(R.id.list_satellite);
            listRl = (RelativeLayout) itemView.findViewById(R.id.list_rl);
        }
    }

    public void setItems(List<Sim.DataBean> data) {
        this.data = data;
        initMap(this.data);
        this.notifyDataSetChanged();
    }

    public void clearDevice() {
        data.clear();
        notifyDataSetChanged();
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

    public void setOnItemCheckListener(OnItemListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    Map<String, Integer> map = new HashMap<>();

    private void initMap(List<Sim.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            map.put(data.get(i).getLockVo().getMac(), -200);
        }
    }
    public void initRssi(){
        for (int i = 0; i < data.size(); i++) {
            map.put(data.get(i).getLockVo().getMac(), -200);
        }
        notifyDataSetChanged();
    }
    public void setMap(String mac, int rssi) {
        map.put(mac, rssi);
        notifyDataSetChanged();
    }

    public int getMap(String mac) {
        return map.get(mac);

    }

    private int getIconId(int rssi) {
        int rssiPercent = (int) (100.0f * (127.0f + rssi) / (127.0f + 20.0f));
        if (rssiPercent < 10) {
            return R.drawable.signal4;
        } else if (rssiPercent >= 10 && rssiPercent < 20) {
            return R.drawable.signal3;
        } else if (rssiPercent >= 20 && rssiPercent < 30) {
            return R.drawable.signal2;
        } else if (rssiPercent >= 30) {
            return R.drawable.signal1;
        }
        return R.drawable.signal4;
    }
}
