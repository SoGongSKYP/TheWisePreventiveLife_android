package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOfCol extends RecyclerView.Adapter<AdapterOfCol.ColViewHolder> {

    Context context;
    ArrayList<SubPath> subPaths;
    public AdapterOfCol(Context context, ArrayList<SubPath> data) {
        this.context = context;
        this.subPaths = data;
    }

    public class ColViewHolder extends RecyclerView.ViewHolder {
        LinearLayout startLinear, finishLinear;
        TextView startTextView, finishTextView, laneTextView;
        ImageView trafficTypeImageView, nextImageView;
        public ColViewHolder(@NonNull View v) {
            super(v);
            startLinear = v.findViewById(R.id.col_start_LinearLayout);
            finishLinear = v.findViewById(R.id.col_finish_LinearLayout);
            startTextView = v.findViewById(R.id.col_start_TextView);
            finishTextView = v.findViewById(R.id.col_finish_TextView);
            laneTextView = v.findViewById(R.id.col_lane_TextView);
            trafficTypeImageView = v.findViewById(R.id.col_traffic_type_ImageView);
            nextImageView = v.findViewById(R.id.col_next_ImageView);
        }
    }
    @NonNull
    @Override
    public ColViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.col_in_row_result, parent, false);
        ColViewHolder vh = new ColViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ColViewHolder holder, int position) {
        int type = subPaths.get(position).getTrafficType();
        if(type == 1){ // 지하철
            holder.trafficTypeImageView.setImageResource(R.drawable.subway);
            holder.startTextView.setText(subPaths.get(position).getStartStation().get_placeAddress());
            holder.finishTextView.setText(subPaths.get(position).getEndStation().get_placeAddress());
            holder.laneTextView.setText(subPaths.get(position).getLaneList().get(0).getName());
        }else if(type == 2){ //버스
            holder.trafficTypeImageView.setImageResource(R.drawable.bus);
            holder.startTextView.setText(subPaths.get(position).getStartStation().get_placeAddress());
            holder.finishTextView.setText(subPaths.get(position).getEndStation().get_placeAddress());
            holder.laneTextView.setText(subPaths.get(position).getLaneList().get(0).getName());
        }else { // 도보
            holder.trafficTypeImageView.setImageResource(R.drawable.run);
            holder.startLinear.setVisibility(View.GONE);
            holder.finishLinear.setVisibility(View.GONE);
            holder.nextImageView.setVisibility(View.INVISIBLE);
            holder.laneTextView.setText("도보");
        }
    }

    @Override
    public int getItemCount() {
        return (subPaths != null ? subPaths.size() : 0);
    }


}
