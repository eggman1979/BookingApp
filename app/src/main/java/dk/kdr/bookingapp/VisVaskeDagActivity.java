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
    Button btn;
    List<VaskeTid> vTid;
    TextView datoText;
    VaskeDag vd;
    boolean[] ledigeTider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_vaske_dag);

        timeListe = (ListView) findViewById(R.id.timeListe);
        datoText = (TextView) findViewById(R.id.dato_id);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        long dato = b.getLong("Dato");
        ledigeTider = BookingApplication.vtCont.ledigeVaskeTider(dato);
        vd = BookingApplication.vtCont.findVaskeDag(dato);

        vTid = vd.getVasketider();

        System.out.println(vTid.get(0).getDato());
        String datoString = CalenderController.millisToDate(dato).toString();
        datoText.setText(datoString);

       timeListe.setAdapter(new TimeListeAdapter(this, vd.getVasketider(),ledigeTider));
        timeListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {




               // long dato = vTid.get(position).getDato();
           //     System.out.println(dato);


//                //Check om de tilgængelige reservationer er af samme vaskeblok som brugeren har valgt
//                VaskeBlok vaskeblok = vTid.get(position).getVaskeBlok();
//                for (Reservation res : new VaskeTidController().createReservation()) {
//                    if (res.getvaskeBlokID() == vaskeblok.getBlokID() && dato == res.getDato()) {
//                        resListe.add(res);
//                    }
//                }
//
////              Vaskeblok og de ledige reservationer pakkes ind for  at sendes til VisLedigeVaskerum aktivitetens
//                Intent i = new Intent(getApplicationContext(), VisLedigeVaskerum.class);
//                Bundle b = new Bundle();
//                b.putParcelable("vaskeBlok", vaskeblok);
//                b.putParcelableArrayList("reservationer", resListe);
//                i.putExtras(b);
//                getApplicationContext().startActivity(i);
            }
        });


    }
}
