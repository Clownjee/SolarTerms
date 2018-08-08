package com.shangxiazuoyou.countrycodepicker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvCode;

    ViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCode = (TextView) itemView.findViewById(R.id.tv_code);
    }
}
