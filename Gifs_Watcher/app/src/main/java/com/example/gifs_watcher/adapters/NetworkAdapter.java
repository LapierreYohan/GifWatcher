package com.example.phonenetworks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.phonenetworks.R;
import com.example.phonenetworks.models.Record;

import java.util.ArrayList;

public class NetworkAdapter extends BaseAdapter {

    ArrayList<Record> datas = null;

    public NetworkAdapter(ArrayList<Record> datas) {
        this.datas = datas;
    }

    public void setArray(ArrayList<Record> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public Object getItem(int i) {
        return this.datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ConstraintLayout layout;
        LayoutInflater inFlater = LayoutInflater.from(viewGroup.getContext());

        if (view == null) {
            layout = (ConstraintLayout) inFlater.inflate(R.layout.adapter_item_network, viewGroup, false);
        } else {
            layout = (ConstraintLayout) view;
        }

        Record record = datas.get(i);

        TextView id = layout.findViewById(R.id.tv_id);
        TextView city = layout.findViewById(R.id.tv_city);
        TextView operator = layout.findViewById(R.id.tv_operator);
        ImageView image = layout.findViewById(R.id.iv_preview);

        id.setText(record.getFields().getInseeDep() + "");
        city.setText(record.getFields().getNomCom());
        operator.setText(record.getFields().getNomOp());

        switch (record.getFields().getNomOp().toLowerCase()) {
            case "orange":
                image.setImageResource(R.drawable.orange);
                record.getFields().setImage(R.drawable.orange);
                break;
            case "sfr":
                image.setImageResource(R.drawable.sfr);
                record.getFields().setImage(R.drawable.sfr);

                break;
            case "bouygues telecom":
                image.setImageResource(R.drawable.buigues);
                record.getFields().setImage(R.drawable.buigues);
                break;
            case "free mobile":
                image.setImageResource(R.drawable.free);
                record.getFields().setImage(R.drawable.free);
                break;
            default:
                image.setImageResource(R.drawable.ic_launcher_background);
                record.getFields().setImage(R.drawable.ic_launcher_background);
                break;
        }

        return layout;
    }
}
