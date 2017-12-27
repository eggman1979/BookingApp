package dk.kdr.bookingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
        LinearLayout background = (LinearLayout) gridView.findViewById(R.id.background);
        LinearLayout border = (LinearLayout) gridView.findViewById(R.id.border);


////      finder dato på baggrund af de reservationer der er sendt med, datoen vises i toppen af aktiviteten.
        DateTime date = CalenderController.millisToDate(dates.get(0).getVaskeDage().get(position).getVasketider().get(0).getDato());
        System.out.println("CalendarView " + date.getMillis() + " " +date.getDayOfMonth() + " " + date.toString());
        int day = date.getDayOfMonth();
        String weekday = CalenderController.getWeekDay(date);
        boolean erFortid = CalenderController.erMindre(CalenderController.getToday(), date);

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
                    }
                }
            }
        }

        week.setText(weekday);

//        Check om vaskedagen er ledig, hvis den er , så males der grøn ellers rød
        if (erDagLedig[position]) {
            background.setBackgroundColor(Color.GREEN);
            border.setBackgroundColor(Color.GREEN);
            if (harBrugerBooked) {
                image.setBackgroundResource(R.drawable.avail_dot);
            }
            if (erFortid) {
                background.setBackgroundColor(Color.parseColor("#006600"));
                border.setBackgroundColor(Color.parseColor("#006600"));
                if (harBrugerBooked) {
                    image.setBackgroundResource(R.drawable.overdue_avail_dot);
                }
            }

        } else {
            background.setBackgroundColor(Color.RED);
            if (harBrugerBooked) {
                image.setBackgroundResource(R.drawable.full_dot);
            }
            if (erFortid) {
                background.setBackgroundColor(Color.parseColor("#660000"));
                border.setBackgroundColor(Color.parseColor("#660000"));
                if (harBrugerBooked) {
                    image.setBackgroundResource(R.drawable.overdue_full_dot);
                }

            }

        }

        if (!erFortid && CalenderController.getToday().getDayOfMonth() == day) {
            if (date.equals(CalenderController.getToday())) {
                border.setBackgroundColor(Color.RED);
            }
        }


        return gridView;
    }
}
