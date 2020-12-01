package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOfSearch extends RecyclerView.Adapter<AdapterOfSearch.SearchViewHolder> {

    public interface OnSearchClickListener{
        void onSearchItemCick(View v, int pos);
    }
    private AdapterOfSearch.OnSearchClickListener searchClickListener = null;
    public void setOnSearchClickListenter(AdapterOfSearch.OnSearchClickListener listenter){
        this.searchClickListener = listenter;
    }

    ArrayList<Place> searchData;
    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView placeTitle, placeDetail;

        public SearchViewHolder(@NonNull View v) {
            super(v);
            placeTitle = v.findViewById(R.id.row_place_title_TextView);
            placeDetail = v.findViewById(R.id.row_place_detail_TextView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(searchClickListener != null){
                            searchClickListener.onSearchItemCick(view, pos);
                        }
                    }
                }
            });
        }
    }

    public AdapterOfSearch(ArrayList<Place> data){
        this.searchData = data;
    }

    @NonNull
    @Override
    public AdapterOfSearch.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_result, parent, false);
        AdapterOfSearch.SearchViewHolder vh = new AdapterOfSearch.SearchViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOfSearch.SearchViewHolder holder, int position) {
        holder.placeTitle.setText(searchData.get(position).get_placeAddress());
        holder.placeDetail.setText(searchData.get(position).get_placeDetailAddress());
    }

    @Override
    public int getItemCount() {
        return ( searchData!=null ? searchData.size() : 0);
    }






}
