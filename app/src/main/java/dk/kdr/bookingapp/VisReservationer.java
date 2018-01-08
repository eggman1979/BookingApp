package dk.kdr.bookingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import data.Reservation;
import logik.BookingApplication;
import logik.CalenderController;

/**
 * Created by KimdR on 12-12-2017.
 */


public class VisReservationer extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView list;
    List<Reservation> myRes;
    List<Reservation> reservationer;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_vis_reservationer);

        myRes = new ArrayList<>();
        reservationer = BookingApplication.vtCont.getReservations();
        System.err.println(reservationer.toString());
        for (Reservation res : reservationer) {
            if (res.getBrugerID() == BookingApplication.bruger.getBrugerID()) {
                if (res.getDato() >= CalenderController.dateToMillis(DateTime.now())) {
                    myRes.add(res);
                }
            }
        }

        list = (ListView) findViewById(R.id.resList);
        list.setAdapter(new ReservationsListeAdapter(this, myRes));
        list.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, ReserverTidAktivitet.class);
        Bundle b = new Bundle();
        Reservation res = myRes.get(position);
        long dato = res.getDato();
        int bruger = res.getBrugerID();
        int vaskeBlok = res.getVaskeBlokID();
        int tavle = res.getTavleID();

        b.putLong("dato", dato);
        b.putInt("blok", vaskeBlok);
        b.putInt("rum", tavle);

        i.putExtras(b);
        startActivity(i);
    }
}
