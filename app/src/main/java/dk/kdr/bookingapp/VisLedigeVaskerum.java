package dk.kdr.bookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.Reservation;
import data.VaskeBlok;

public class VisLedigeVaskerum extends AppCompatActivity {

    ListView list;
    TextView blokText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_ledige_vaskerum);

        Intent i = getIntent();
        ArrayList<Reservation> resListe = i.getExtras().getParcelableArrayList("reservationer");
        VaskeBlok vaskeblok = i.getExtras().getParcelable("vaskeBlok");
        blokText = (TextView) findViewById(R.id.bloktid);
        list = (ListView) findViewById(R.id.vaskerum_liste);
        blokText.setText(vaskeblok.getStartTid() + ":00");
        TavleAdapter ta = new TavleAdapter(this, resListe, 3);

        list.setAdapter(ta);


    }
}
