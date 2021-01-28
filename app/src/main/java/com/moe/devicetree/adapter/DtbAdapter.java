package com.moe.devicetree.adapter;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.view.View;
import java.io.File;
import android.view.LayoutInflater;
import com.moe.devicetree.R;
import android.widget.TextView;

public class DtbAdapter extends BaseAdapter {
    private File[] dtbs;
    public DtbAdapter(File[] dtbs){
        this.dtbs=dtbs;
    }
    @Override
    public int getCount() {
        return dtbs.length;
    }

    @Override
    public Object getItem(int p1) {
        return dtbs[p1];
    }

    @Override
    public long getItemId(int p1) {
        return p1;
    }

    @Override
    public View getView(int p1, View p2, ViewGroup p3) {
        if(p2==null){
            p2=LayoutInflater.from(p3.getContext()).inflate(R.layout.dtb_list_item,p3,false);
        }
        ((TextView)p2.findViewById(R.id.title)).setText(dtbs[p1].getName());
        return p2;
    }
    
    
    
    
}
