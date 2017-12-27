package dk.kdr.bookingapp;


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
import logik.AsyncDelete;
import logik.AsyncReservation;
import logik.BookingApplication;
import logik.CalenderController;
import logik.Callback;

public class ReserverTidAktivitet extends AppCompatActivity implements View.OnClickListener, Callback {

    Button accept, afvis, delete;
    TextView datoText, vaskeRumText, brugerIDText, boligforeningText, tidText, vaskeBlokText, statusText;
    long dato;
    int vaskeBlok, vaskerum;
    String tid, boligforening;
    Reservation reservation;
    ProgressDialog pDiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserver_tid_aktivitet);



        Intent i = this.getIntent();
        dato = i.getLongExtra("dato", -1);
        vaskeBlok = i.getIntExtra("blok", -1);
        vaskerum = i.getIntExtra("rum", -1);

        tid = BookingApplication.vtCont.getvBlokke().get(vaskeBlok - 1).getStartTid() + ":00";

        statusText = (TextView) findViewById(R.id.status);
        datoText = (TextView) findViewById(R.id.reserver_dato);
        vaskeBlokText = (TextView) findViewById(R.id.reserver_vaskeblok);
        brugerIDText = (TextView) findViewById(R.id.reserver_bruger);
        vaskeRumText = (TextView) findViewById(R.id.reserver_vaskerum);
        tidText = (TextView) findViewById(R.id.reserver_tid_);
        boligforeningText = (TextView) findViewById(R.id.reserver_boligforening);

        boligforening = BookingApplication.boligForening.getNavn();
        reservation = BookingApplication.vtCont.getReservation(dato, vaskerum, vaskeBlok);

        afvis = (Button) findViewById(R.id.afvis);
        afvis.setOnClickListener(this);
        accept = (Button) findViewById(R.id.accept);
        accept.setOnClickListener(this);
        accept.setEnabled(false);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        String brugerText = "ingen";
        if (dato < CalenderController.dateToMillis(CalenderController.getToday())) {
            statusText.setTextColor(Color.RED);
            statusText.setText("Dato er overskredet");
            if (reservation != null) {
                brugerText = reservation.getBrugerID() + "";
            }
        } else if (reservation == null) {
            statusText.setText("ledig");
            statusText.setTextColor(Color.GREEN);
            brugerText = "Ingen";
            accept.setEnabled(true);
        } else {
            statusText.setText("optaget");
            statusText.setTextColor(Color.RED);
            brugerText = reservation.getBrugerID() + "";
            if (reservation.getBrugerID() == BookingApplication.bruger.getBrugerID()) {
                delete.setVisibility(View.VISIBLE);
                accept.setVisibility(View.INVISIBLE);
                afvis.setBackgroundResource(R.drawable.accept_button_menu);
            }
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

        if (v == afvis) {
            Toast.makeText(this, "Du valgte at afvise reservationen", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (v == accept) {
            pDiag = ProgressDialog.show(this, "Forsøger at reservere din vasketid", "Vent venligst", true);
            // Reservationen opbygges på bagrund af dato, tid osv. bruger og boligforening hentes fra Bookingapplication;
            Reservation res = new Reservation(BookingApplication.bruger.getBrugerID(), dato, vaskeBlok, BookingApplication.boligForening.getId(), vaskerum, -1L);
            new AsyncReservation(this, res, pDiag).execute();
        }
        if (v == delete) {
            pDiag = ProgressDialog.show(this, "Forsøger at slette din vasketid", "Vent venligst", true);
            new AsyncDelete(this, pDiag, reservation.getReservationID()).execute();
        }
        Intent i = null;
        if (BookingApplication.isMonth) {
            i = new Intent(this, ShowMonthActivity.class);
        } else {
            i = new Intent(this, ShowWeekActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onEventCompleted(String msg) {
        if (accept.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            BookingApplication.vtCont.getReservations().remove(reservation);
        }
    }

    @Override
    public void onEventFailed(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pDiag != null) {
            pDiag.dismiss();
        }
    }
}
