package com.acpi.mls.missionlunarspace.listObjetMobile;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.ChoixClasseActivity;
import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.R;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    protected ArrayList<String> data;
    protected ChoixGroupeActivity choixGroupeActivity = null;
    protected ChoixClasseActivity choixClasseActivity = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            mTitle = itemView.findViewById(R.id.text_mobile);
        }
    }

    public RecyclerViewAdapter(ArrayList<String> data) {
        this.data = data;
    }

    public RecyclerViewAdapter(ArrayList<String> data, ChoixGroupeActivity choixGroupeActivity) {
        this.data = data;
        this.choixGroupeActivity = choixGroupeActivity;
    }

    public RecyclerViewAdapter(ArrayList<String> data, ChoixClasseActivity choixClasseActivity) {
        this.data = data;
        this.choixClasseActivity = choixClasseActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_objet, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
        if (this.choixGroupeActivity != null) {
            if (this.choixGroupeActivity.getPhase() == 3) {
                this.choixGroupeActivity.mouvementFait();
            }
        }
        if(this.choixClasseActivity != null){
            this.choixClasseActivity.saveClassement();
        }
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

}

