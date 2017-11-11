package dk.kdr.bookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.Reservation;
import data.VaskeBlok;
import data.VaskeDag;
import data.VaskeTid;
import logik.BookingApplication;
import logik.CalenderController;
import logik.VaskeTidController;

/**
 * Aktivitet der viser en liste over hvilke vaskeblokke der er tilgængelige, samt hvilke af disse der er ledige
 */

public class VisVaskeDagActivity extends AppCompatActivity {

    ListView timeListe;
    List<VaskeTid> vTid;
    TextView datoText;
    boolean[] ledigeTider;
    long dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_vaske_dag);

        timeListe = (ListView) findViewById(R.id.timeListe);
        datoText = (TextView) findViewById(R.id.dato_id);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        dato = b.getLong("Dato");
        System.out.println("Fra VisVaskeDagAktivitet - dato " + dato);

        BookingApplication.hentReservationer(dato,dato);

        ledigeTider = BookingApplication.vtCont.ledigeVaskeTider(dato, BookingApplication.isMonth);
        List<VaskeBlok> vBlokke = BookingApplication.vtCont.getvBlokke();




        String datoString = CalenderController.millisToDate(dato).toString();
        datoText.setText(datoString);

        timeListe.setAdapter(new TimeListeAdapter(this,vBlokke, ledigeTider));
        timeListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


               System.out.println("Fra VaskeDagAktivitet - setOnItem.... Dato " + dato);

                Intent i = new Intent(getApplicationContext(), VisLedigeVaskerum.class);
                i.putExtra("Dato", dato);
                i.putExtra("Blok", position);
                getApplicationContext().startActivity(i);
            }
        });


    }
}
