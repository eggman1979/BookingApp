package dk.kdr.bookingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import data.Reservation;
import data.VaskeTavle;
import data.VaskeTid;
import logik.BookingApplication;

/**
 * Created by KimdR on 30-10-2017.
 */

public class TavleAdapter extends BaseAdapter {

    Context context;
    boolean[] ledigeRum;
    List<VaskeTid> tider;


    public TavleAdapter(Context context, boolean[] ledigeRum, List<VaskeTid> tider) {
        this.context = context;
        this.ledigeRum = ledigeRum;
        this.tider = tider;
    }

    @Override
    public int getCount() {
        return ledigeRum.length;
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
        View gridView;

        if (convertView == null) {
            gridView = new View(context);
        } else {
            gridView = (View) convertView;
        }
        boolean brugerHasBooked = false;
        for (VaskeTid vt : tider) {
            if (vt.getReservation() != null) {
                if (vt.getReservation().getBrugerID() == BookingApplication.bruger.getBrugerID() && position == (vt.getReservation().getTavleID()-1)) {
                    brugerHasBooked = true;


                }
            }
        }

        gridView = inflater.inflate(R.layout.vaskerum_item, null);

        TextView textView = (TextView) gridView.findViewById(R.id.tavleid);
        textView.setText("Vaskerum " + (position + 1)); //Skriver hvilket vaskerum på elementerne
        ImageView image = (ImageView) gridView.findViewById(R.id.booked_af_user);

        if (ledigeRum[position]) {
            gridView.setBackgroundColor(Color.GREEN);
            if(brugerHasBooked){
                image.setBackgroundResource(R.drawable.avail_dot);
            }
        }else{
            gridView.setBackgroundColor(Color.RED);
            if(brugerHasBooked){
                image.setBackgroundResource(R.drawable.full_dot);
            }
        }
        return gridView;
    }
}