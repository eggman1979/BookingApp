package dk.kdr.bookingapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import data.Reservation;
import logik.BookingApplication;

/**
 * Created by KimdR on 12-12-2017.
 */


public class VisReservationer extends AppCompatActivity {

    ListView list;

    List<Reservation> reservationer;
    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_vis_reservationer);

List<Reservation> myRes = new ArrayList<>();
         reservationer = BookingApplication.vtCont.getReservations();
        for (Reservation res : reservationer) {
            if (res.getBrugerID() == BookingApplication.bruger.getBrugerID()) {
                myRes.add(res);
            }
        }

        list = (ListView) findViewById(R.id.resList);
        list.setAdapter(new ReservationsListeAdapter(this, myRes));
//

    }
}
