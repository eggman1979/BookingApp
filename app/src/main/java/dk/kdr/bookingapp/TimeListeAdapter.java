package dk.kdr.bookingapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import data.VaskeBlok;
import data.VaskeTid;

/**
 * Created by KimdR on 30-10-2017.
 */

public class TimeListeAdapter extends BaseAdapter {



    Context context;
    List<VaskeBlok> blokke;
    boolean[] ledigeTider;

    public TimeListeAdapter(Context context, List<VaskeBlok> blokke, boolean[] ledigeTider){
        this.blokke = blokke;
        this.context = context;
        this.ledigeTider = ledigeTider;

    }


    @Override
    public int getCount() {
        return blokke.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View timeView;
        if(convertView == null){
            timeView = new View(context);
        }else{
            timeView = convertView;
        }

      timeView = inflater.inflate( R.layout.time_item, null);


        TextView textView = (TextView) timeView.findViewById(R.id.tid);


            textView.setText(blokke.get(position).getStartTid()+":00");

        if(ledigeTider[position]){
           textView.setBackgroundColor(Color.GREEN);

        }

        return timeView;
    }
}