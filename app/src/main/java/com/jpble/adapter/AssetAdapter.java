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
import com.jpble.bean.Device;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omni20170501 on 2017/6/6.
 */

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {
    private List<Device> data;
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
        Device bleDevice = data.get(position);
        holder.listRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCheckListener != null) {
                    onItemCheckListener.OnItemCheck(holder, position, data.get(position));
                }
            }
        });

        holder.deviceName.setText(bleDevice.getName());
        holder.deviceName.setTextColor(context.getResources().getColor(bleDevice.isBle()?R.color.blue:R.color.zircon));
        holder.listBle.setImageResource(bleDevice.isBle()?R.drawable.ble:R.drawable.ble_false);
        holder.listSatellite.setImageResource(bleDevice.isBle()?R.drawable.satellite:R.drawable.satellite_false);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private ImageView listSatellite,listBle;
        private RelativeLayout listRl;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.list_name);
            listBle = (ImageView) itemView.findViewById(R.id.list_ble);
            listSatellite = (ImageView) itemView.findViewById(R.id.list_satellite);
            listRl = (RelativeLayout) itemView.findViewById(R.id.list_rl);
        }
    }

    public void setItems(List<Device> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void clearDevice() {
        data.clear();
        notifyDataSetChanged();
    }

    public void setDevice(Device device) {
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


}
