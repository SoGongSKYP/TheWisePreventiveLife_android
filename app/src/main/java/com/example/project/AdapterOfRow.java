package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOfRow extends RecyclerView.Adapter<AdapterOfRow.RowViewHolder> {

    interface onRowClickListener{
        void onRowClick(View view, int pos);
    }
    private onRowClickListener rowClickListener = null;
    public void setRowClickListener(onRowClickListener listener){
        this.rowClickListener = listener;
    }

    Context context;
    ArrayList<SearchPath> paths;
    public AdapterOfRow(Context context, ArrayList<SearchPath> data) {
        this.context = context;
        this.paths = data;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder{
        TextView startStationTextView, finishStationTextView;
        RecyclerView colRecyclerView;
        public RowViewHolder(@NonNull View v) {
            super(v);
            startStationTextView = v.findViewById(R.id.route_start_TextView);
            finishStationTextView = v.findViewById(R.id.route_finish_TextView);
            colRecyclerView = v.findViewById(R.id.route_RecyclerView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Log.d("클릭된거 맞음?", Integer.toString(pos));
                    if(pos!=RecyclerView.NO_POSITION){
                        if(rowClickListener != null){
                            Log.d("여기도 통과?", "OK");
                            rowClickListener.onRowClick(view, pos);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_route_result, parent, false);
        RowViewHolder vh = new RowViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, final int position) {
        holder.startStationTextView.setText(paths.get(position).getInfo().getFirstStartStation());
        holder.finishStationTextView.setText(paths.get(position).getInfo().getLastEndStation());

        // 내부 루트 구현
        AdapterOfCol colAdapter = new AdapterOfCol(context, paths.get(position).getSubPaths());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.colRecyclerView.setLayoutManager(layoutManager);
        holder.colRecyclerView.setAdapter(colAdapter);
    }

    @Override
    public int getItemCount() {
        return (paths != null? paths.size() : 0);
    }


}
