package dk.kdr.bookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.Reservation;
import data.VaskeBlok;
import data.VaskeDag;
import data.VaskeTid;
import logik.BookingApplication;

public class VisLedigeVaskerum extends AppCompatActivity {

    ListView list;
    TextView blokText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_ledige_vaskerum);

        Intent i = getIntent();
        long dato = i.getLongExtra("dato", -1L);
        List<VaskeTid> vTider = BookingApplication.vtCont.findVaskeTiderFraBlok(dato);


        blokText = (TextView) findViewById(R.id.bloktid);
        list = (ListView) findViewById(R.id.vaskerum_liste);
      //  blokText.setText("Grøn Lort");
        TavleAdapter ta = new TavleAdapter(this, null, 3);

        list.setAdapter(ta);


    }
}
