package com.keelim.cnubus.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.PagerViewHolder> {
    private ArrayList<String> arrayList = null;

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(null, parent, false);
        MainAdapter.PagerViewHolder pagerViewHolder = new PagerViewHolder(view);
        return pagerViewHolder;
    }

    public MainAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PagerViewHolder extends RecyclerView.ViewHolder {

        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
