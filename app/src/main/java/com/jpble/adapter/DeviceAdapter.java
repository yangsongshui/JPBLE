package com.jpble.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpble.OnItemCheckListener;
import com.jpble.R;
import com.jpble.ble.BLEDevice;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omni20170501 on 2017/6/6.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private List<BLEDevice> data;
    private Context context;
    private OnItemCheckListener onItemCheckListener;

    public DeviceAdapter(Context context) {

        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deivce, parent, false);
        return new DeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BLEDevice bleDevice = data.get(position);
        holder.device_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCheckListener != null){


                    onItemCheckListener.OnItemCheck(holder, position, data.get(position));
                }
            }
        });

        holder.device_name.setText(bleDevice.getDevice().getName());
        holder.device_mac.setText(bleDevice.getDevice().getAddress());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private TextView device_name ,device_mac;
        private RelativeLayout device_item;

        public ViewHolder(View itemView) {
            super(itemView);
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            device_mac = (TextView) itemView.findViewById(R.id.device_mac);
            device_item = (RelativeLayout) itemView.findViewById(R.id.device_item);


        }
    }

    public void setItems(List<BLEDevice> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void clearDevice() {
        data.clear();
        notifyDataSetChanged();
    }

    public void setDevice(BLEDevice device) {
        data.add(data.size(), device);
        notifyItemInserted(data.size());
    }

    public void reDevice(int position) {
        if (data.size() >= position) {
            data.remove(position);
        }
        notifyDataSetChanged();
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }


}
