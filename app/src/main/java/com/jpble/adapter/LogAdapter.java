package com.jpble.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpble.OnLogItemListener;
import com.jpble.R;
import com.jpble.bean.LogInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omni20170501 on 2017/6/6.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private List<LogInfo.DataBean> data;
    private Context context;
    private OnLogItemListener onLogItemListenern;

    public LogAdapter(Context context) {

        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        return new LogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.log_time.setText(data.get(position).getDateStr());
        holder.logRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLogItemListenern != null)
                    onLogItemListenern.OnItemCheck(holder, position, data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView log_time, log_info;
        private RelativeLayout logRl;

        public ViewHolder(View itemView) {
            super(itemView);
            log_time = (TextView) itemView.findViewById(R.id.log_time);
            log_info = (TextView) itemView.findViewById(R.id.log_info);
            logRl = (RelativeLayout) itemView.findViewById(R.id.log_rl);
        }
    }

    public void setItems(List<LogInfo.DataBean> data) {
        this.data.clear();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getLockVo().getGLat()!=null && data.get(i).getLockVo().getGLng()!=null )
                this.data.add(data.get(i));
        }
        this.notifyDataSetChanged();
    }

    public void clearDevice() {
        data.clear();
        notifyDataSetChanged();
    }

    public void setDevice(LogInfo.DataBean device) {
        data.add(data.size(), device);
        notifyItemInserted(data.size());
    }

    public void reDevice(int position) {
        if (data.size() >= position) {
            data.remove(position);
        }
        notifyDataSetChanged();
    }

    public void setOnItemCheckListener(OnLogItemListener onLogItemListenern) {
        this.onLogItemListenern = onLogItemListenern;
    }


}
