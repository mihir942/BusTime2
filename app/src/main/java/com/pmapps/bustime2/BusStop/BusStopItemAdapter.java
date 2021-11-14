package com.pmapps.bustime2.BusStop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustime2.R;

import java.util.List;

public class BusStopItemAdapter extends RecyclerView.Adapter<BusStopItemAdapter.BusStopViewHolder> {

    private final Context mContext;
    private final List<BusStopItem> mBusStopList;

    public BusStopItemAdapter(Context context, List<BusStopItem> busStopList) {
        mContext = context;
        mBusStopList = busStopList;
    }

    @NonNull
    @Override
    public BusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.bus_stop_item, parent, false);
        return new BusStopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStopViewHolder holder, int position) {
        BusStopItem busStopItem = mBusStopList.get(position);

        String busStopTitle = busStopItem.getBusStopTitle();
        String busStopRoad = busStopItem.getBusStopRoad();
        String busStopCode = busStopItem.getBusStopCode();

        holder.busStopTitle_TV.setText(busStopTitle);
        holder.busStopRoad_TV.setText(busStopRoad);
        holder.busStopCode_TV.setText(busStopCode);
    }

    @Override
    public int getItemCount() {
        return mBusStopList.size();
    }


    public class BusStopViewHolder extends RecyclerView.ViewHolder {

        private final TextView busStopTitle_TV;
        private final TextView busStopCode_TV;
        private final TextView busStopRoad_TV;

        public BusStopViewHolder(@NonNull View itemView) {
            super(itemView);

            busStopTitle_TV = itemView.findViewById(R.id.busStopTitle_TV);
            busStopRoad_TV = itemView.findViewById(R.id.busStopRoad_TV);
            busStopCode_TV = itemView.findViewById(R.id.busStopCode_TV);
        }
    }
}
