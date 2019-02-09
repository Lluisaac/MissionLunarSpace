package com.acpi.mls.missionlunarspace.immobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    protected TextView textView;

    public MyViewHolder(View itemView) {
        super(itemView);

        this.textView = (TextView) itemView.findViewById(R.id.text);
    }

}
