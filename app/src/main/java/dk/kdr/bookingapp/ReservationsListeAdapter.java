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

import data.Reservation;
import data.VaskeBlok;
import data.VaskeTavle;
import logik.BookingApplication;
import logik.CalenderController;

/**
 * Created by KimdR on 13-12-2017.
 */

public class ReservationsListeAdapter extends BaseAdapter {

    List<Reservation> resList;
    Context context;

    public ReservationsListeAdapter(Context context, List<Reservation> resList) {
        this.resList = resList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return resList.size();
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
        gridView = inflater.inflate(R.layout.reservation_liste_item, null);
        TextView tavle = (TextView) gridView.findViewById(R.id.tavle);
        TextView tid = (TextView) gridView.findViewById(R.id.tid);
        TextView dato = (TextView) gridView.findViewById(R.id.dato);

        Reservation res = resList.get(position);
        tavle.setText("Vaskerum " + res.getTavleID());
        VaskeBlok blok = BookingApplication.vtCont.getvBlokke().get(res.getvaskeBlokID()-1);

        tid.setText("Kl. "+blok.getStartTid() + ":00");

        String datoString = CalenderController.millisToDate(res.getDato()).toString();

        dato.setText(datoString +" ");


        return gridView;
    }
}
