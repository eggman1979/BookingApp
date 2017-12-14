package dk.kdr.bookingapp;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.Reservation;
import logik.AsyncReservation;
import logik.BookingApplication;
import logik.CalenderController;
import logik.Callback;

public class ReserverTidAktivitet extends AppCompatActivity implements View.OnClickListener, Callback {

    Button accept, afvis;
    TextView datoText, vaskeRumText, brugerIDText, boligforeningText, tidText, vaskeBlokText, statusText;
    long dato;
    int vaskeBlok, bruger, vaskerum;
    String tid, boligforening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserver_tid_aktivitet);

        Intent i = this.getIntent();


        dato = i.getLongExtra("dato", -1);
        vaskeBlok = i.getIntExtra("blok", -1);

        vaskerum = i.getIntExtra("rum", -1);
        tid = BookingApplication.vtCont.getvBlokke().get(vaskeBlok-1).getStartTid() + ":00";

        statusText = (TextView) findViewById(R.id.status);
        datoText = (TextView) findViewById(R.id.reserver_dato);
        vaskeBlokText = (TextView) findViewById(R.id.reserver_vaskeblok);
        brugerIDText = (TextView) findViewById(R.id.reserver_bruger);
        vaskeRumText = (TextView) findViewById(R.id.reserver_vaskerum);
        tidText = (TextView) findViewById(R.id.reserver_tid_);
        boligforeningText = (TextView) findViewById(R.id.reserver_boligforening);

        boligforening = BookingApplication.boligForening.getNavn();


        Reservation reservation = BookingApplication.vtCont.getReservation(dato, vaskerum, vaskeBlok);
        System.out.println("Reservationen er " + reservation);

        afvis = (Button) findViewById(R.id.afvis);
        afvis.setOnClickListener(this);
        accept = (Button) findViewById(R.id.accept);
        accept.setOnClickListener(this);
        accept.setEnabled(false);


        String brugerText = "ingen";
        if(dato < CalenderController.dateToMillis(CalenderController.getToday())){
            statusText.setTextColor(Color.RED);
            statusText.setText("Dato er overskredet");
            if(reservation != null){
                brugerText = reservation.getBrugerID() +"";
            }
        }
        else if (reservation == null) {
            statusText.setText("ledig");
            statusText.setTextColor(Color.GREEN);
            brugerText = "Ingen";
            accept.setEnabled(true);


        } else {
            statusText.setText("optaget");
            statusText.setTextColor(Color.RED);
           brugerText  = reservation.getBrugerID() +"";
        }



        datoText.setText(CalenderController.millisToDate(dato).toString());
        vaskeBlokText.setText(vaskeBlok + "");
        vaskeRumText.setText(vaskerum + "");
        boligforeningText.setText(boligforening);
        tidText.setText(tid);
        brugerIDText.setText(brugerText);



    }

    @Override
    public void onClick(View v) {
        System.out.println("Vaskeblok = " + vaskeBlok);
        if (v == afvis) {
            Toast.makeText(this, "Du valgte at afvise reservationen", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (v == accept) {
            ProgressDialog pDiag = ProgressDialog.show(this, "Forsøger at reservere din vasketid", "Vent venligst", true);
            // Reservationen opbygges på bagrund af dato, tid osv. bruger og boligforening hentes fra Bookingapplication;
            Reservation res = new Reservation(BookingApplication.bruger.getBrugerID(), dato, vaskeBlok, BookingApplication.boligForening.getId(), vaskerum, -1L);
            new AsyncReservation(this, res, pDiag).execute();
        }
        Intent i = null;
        if (BookingApplication.isMonth) {
            i = new Intent(this, ShowMonthActivity.class);
        } else {
            i = new Intent(this, ShowWeekActivity.class);
        }
        startActivity(i);

    }

    @Override
    public void onEventCompleted() {
        Toast.makeText(this, "Reservationen er gemt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventFailed() {
        Toast.makeText(this, "Der opstod en fejl, kontroller din internet forbindelse", Toast.LENGTH_SHORT).show();
    }
}
