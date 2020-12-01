package com.example.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOfPlace extends RecyclerView.Adapter<AdapterOfPlace.PlaceViewHolder>{
    int mode = 0;   // 1이 수정, 0이 기본

    public interface OnModifyClickListener{
        void onModifyItemClick(View v, int pos);
    }
    private AdapterOfPlace.OnModifyClickListener modifyClickListener = null;
    public void setModifyClickListenter(AdapterOfPlace.OnModifyClickListener listener){
        this.modifyClickListener = listener;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{
        private TextView placeTitleTextView, visitDateTextView;
        private ImageButton deleteButton;
        public PlaceViewHolder(@NonNull View v) {
            super(v);
            placeTitleTextView = v.findViewById(R.id.visit_place_title_TextView);
            visitDateTextView = v.findViewById(R.id.visit_place_date_TextView);
            deleteButton = v.findViewById(R.id.row_visit_delete_Button);
        }
    }


    private ArrayList<VisitPlace> placeList = new ArrayList<VisitPlace>();
    public AdapterOfPlace(ArrayList<VisitPlace> data, int mode) {
        this.placeList = data;
        this.mode = mode;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_patient_visit, parent, false);
        AdapterOfPlace.PlaceViewHolder vh = new AdapterOfPlace.PlaceViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, final int position) {
        holder.placeTitleTextView.setText(placeList.get(position).getVisitPlace().get_placeAddress());
        holder.visitDateTextView.setText(placeList.get(position).getVisitDate());
        if (mode == 0){
            holder.deleteButton.setVisibility(View.INVISIBLE);
        }else{
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(modifyClickListener!=null){
                        modifyClickListener.onModifyItemClick(view, position);
                        Log.d("지금 지워진 row 넘버 : ", Integer.toString(position));
                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, placeList.size());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (placeList != null? placeList.size() : 0);
    }


}
