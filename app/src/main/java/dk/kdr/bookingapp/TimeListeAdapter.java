package dk.kdr.bookingapp;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import data.VaskeBlok;
import data.VaskeDag;
import data.VaskeTavle;
import data.VaskeTid;
import logik.BookingApplication;

/**
 * Created by KimdR on 30-10-2017.
 */

public class TimeListeAdapter extends BaseAdapter {


    Context context;
    List<VaskeBlok> blokke;
    boolean[] ledigeTider;
    List<VaskeDag> dage;

    public TimeListeAdapter(Context context, List<VaskeBlok> blokke, boolean[] ledigeTider, List<VaskeDag> dage) {
        this.blokke = blokke;
        this.context = context;
        this.ledigeTider = ledigeTider;
        this.dage = dage;
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
        if (convertView == null) {
            timeView = new View(context);
        } else {
            timeView = convertView;
        }


        timeView = inflater.inflate(R.layout.time_item, null);

        TextView textView = (TextView) timeView.findViewById(R.id.tid);
        ImageView image = (ImageView) timeView.findViewById(R.id.booked_af_user);
        boolean brugerHarBooked = false;
        for (VaskeDag vd : dage) {
            for (int i = 0; i < blokke.size(); i++) {
                VaskeTid tid = vd.getVasketider().get(position);
                if (tid.getReservation() != null) {
                    if (tid.getReservation().getBrugerID() == BookingApplication.bruger.getBrugerID()) {
                        brugerHarBooked = true;
                    }
                }
            }
        }


        textView.setText(blokke.get(position).getStartTid() + ":00");

        if (ledigeTider[position]) {
           timeView.setBackgroundColor(Color.GREEN);
            if (brugerHarBooked) {
                image.setBackgroundResource(R.drawable.avail_dot);
            }

        } else {
            timeView.setBackgroundColor(Color.RED);
            if (brugerHarBooked) {
              image.setBackgroundResource(R.drawable.full_dot);
            }
        }


        return timeView;
    }
}