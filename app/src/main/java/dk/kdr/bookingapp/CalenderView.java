package dk.kdr.bookingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import data.VaskeTavle;
import logik.BookingApplication;
import logik.CalenderController;


/**
 * Created by KimdR on 21-10-2017.
 */

public class CalenderView extends BaseAdapter {

    String month = "";
    Context context;
    List<VaskeTavle> dates;
    private boolean[] erDagLedig;
    private boolean isWeek;

    public CalenderView(Context context, List<VaskeTavle> dates, boolean[] erDagLedig, boolean isWeek) {
        this.context = context;
        this.dates = dates;
        this.erDagLedig = erDagLedig;
        this.isWeek = isWeek;
    }

    @Override
    public int getCount() {
        return erDagLedig.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);
        } else {
            gridView = (View) convertView;
        }
        gridView = inflater.inflate(R.layout.grid_item, null);
        TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
        TextView week = (TextView) gridView.findViewById(R.id.weekday);
        ImageView image = (ImageView) gridView.findViewById(R.id.booked_af_user);


////      finder dato på baggrund af de reservationer der er sendt med, datoen vises i toppen af aktiviteten.
        LocalDate date = new LocalDate(dates.get(0).getVaskeDage().get(position).getVasketider().get(0).getDato());
        int day = date.getDayOfMonth();
        String weekday = CalenderController.getWeekDay(date);

        if (isWeek) {
            month = ". " + CalenderController.getMonthInText(date.getMonthOfYear());
        }
        boolean harBrugerBooked = false;
        textView.setText(day + month);
        for (VaskeTavle vt : dates) {
            for (int i = 0; i < vt.getVaskeDage().get(position).getAntalBlokke(); i++) {
                if (vt.getVaskeDage().get(position).getVasketider().get(i).getReservation() != null) {
                    int bruger = vt.getVaskeDage().get(position).getVasketider().get(i).getReservation().getBrugerID();
                    if (bruger == BookingApplication.bruger.getBrugerID()) {
                        harBrugerBooked = true;
                        System.out.println("true");
                    }
                }
            }
        }

        week.setText(weekday);

//        Check om vaskedagen er ledig, hvis den er , så males der grøn ellers rød
        if (erDagLedig[position]) {
            gridView.setBackgroundColor(Color.GREEN);
            if(harBrugerBooked){
                image.setBackgroundResource(R.drawable.avail_dot);
            }
        } else {
            gridView.setBackgroundColor(Color.RED);
            if(harBrugerBooked){
                image.setBackgroundResource(R.drawable.full_dot);
            }
        }

        return gridView;
    }
}
