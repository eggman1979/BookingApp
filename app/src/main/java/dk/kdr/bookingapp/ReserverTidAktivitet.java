package dk.kdr.bookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import logik.BookingApplication;
import logik.CalenderController;

public class ReserverTidAktivitet extends AppCompatActivity {

    Button accept, afvis;
    TextView datoText, vaskeRumText, brugerIDText, boligforeningText, tidText, vaskeBlokText;
    long dato;
    int vaskeBlok, bruger, vaskerum;
    String tid, boligforening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserver_tid_aktivitet);

        Intent i = this.getIntent();
        if (BookingApplication.bruger != null) {
            System.out.println("BRUGER ER IKKE NULL");
            bruger = BookingApplication.bruger.getBrugerID();
            boligforening = "Kildevang";
        }

        dato = i.getLongExtra("dato", -1);
        vaskeBlok = i.getIntExtra("blok", -1);
        System.out.println("vaskeblok " + vaskeBlok);
        vaskerum = i.getIntExtra("rum", -1);
        tid = BookingApplication.vtCont.getvBlokke().get(vaskeBlok).getStartTid() + ":00";


        datoText = (TextView) findViewById(R.id.reserver_dato);
        vaskeBlokText = (TextView) findViewById(R.id.reserver_vaskeblok);
        brugerIDText = (TextView) findViewById(R.id.reserver_bruger);
        vaskeRumText = (TextView) findViewById(R.id.reserver_vaskerum);
        tidText = (TextView) findViewById(R.id.reserver_tid_);
        boligforeningText = (TextView) findViewById(R.id.reserver_boligforening);

        datoText.setText(CalenderController.millisToDate(dato).toString());
        vaskeBlokText.setText(vaskeBlok +"");
        vaskeRumText.setText(vaskerum+"");
        boligforeningText.setText(boligforening);
        tidText.setText(tid);
        brugerIDText.setText(bruger+"");

        afvis = (Button) findViewById(R.id.afvis);
        accept = (Button) findViewById(R.id.accept);

    }
}
