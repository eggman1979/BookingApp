package dk.kdr.bookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.Reservation;
import data.VaskeBlok;
import data.VaskeDag;
import data.VaskeTid;
import logik.BookingApplication;

public class VisLedigeVaskerum extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView list;
    TextView blokText;
    long dato;
    int blok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_ledige_vaskerum);

        Intent i = getIntent();
        dato = i.getLongExtra("Dato", -1L);
        blok = i.getIntExtra("Blok", -1);
        VaskeBlok vBlok = BookingApplication.vtCont.getvBlokke().get(blok);
        List<VaskeTid> tider = BookingApplication.vtCont.findVaskeTid(dato,blok);
        boolean[] ledigeRum = BookingApplication.vtCont.ledigeVaskerum(dato, blok);


        blokText = (TextView) findViewById(R.id.bloktid);
        list = (ListView) findViewById(R.id.vaskerum_liste);
        blokText.setText("Vasketid: " + vBlok.getStartTid()+":00");

        TavleAdapter ta = new TavleAdapter(this, ledigeRum, tider);

        list.setAdapter(ta);
        list.setOnItemClickListener(this);


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, ReserverTidAktivitet.class );
        Bundle b = new Bundle();
        b.putLong("dato", dato);
        b.putInt("blok", blok+1); // Måske plus 1?
        b.putInt("rum", position+1);
        i.putExtras(b);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
