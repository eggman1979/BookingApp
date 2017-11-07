package dk.kdr.bookingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import data.VaskeDag;
import data.VaskeTavle;

import static android.R.attr.start;


/**
 * Created by KimdR on 21-10-2017.
 */

public class CalenderView extends BaseAdapter {

    Context context;
    List<VaskeTavle> dates;
    private boolean[] erDagLedig;

    public CalenderView(Context context, List<VaskeTavle> dates, boolean[] erDagLedig) {
        this.context = context;
        this.dates = dates;
        this.erDagLedig = erDagLedig;
    }

    @Override
    public int getCount() {
        return 42;
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

//      finder dato på baggrund af de reservationer der er sendt med, datoen vises i toppen af aktiviteten.
        int date = new LocalDate(dates.get(0).getVaskeDage().get(position).getVasketider().get(0).getDato()).getDayOfMonth();

        textView.setText(date + "");

//        Check om vaskedagen er ledig, hvis den er , så males der grøn ellers rød
        if (erDagLedig[position]) {
            gridView.setBackgroundColor(Color.GREEN);
        } else {
            gridView.setBackgroundColor(Color.RED);
        }

        return gridView;
    }
}
