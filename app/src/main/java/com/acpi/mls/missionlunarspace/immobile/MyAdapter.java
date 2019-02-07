package com.acpi.mls.missionlunarspace.immobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acpi.mls.missionlunarspace.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<MyObject> list;

    public MyAdapter(List<MyObject> list) {
        this.list = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.immobile_objet, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyObject myObject = list.get(position);
        holder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
