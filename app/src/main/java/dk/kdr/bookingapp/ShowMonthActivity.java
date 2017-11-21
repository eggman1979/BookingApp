package dk.kdr.bookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import org.joda.time.LocalDate;


import java.util.List;

import data.VaskeDag;

import data.VaskeTavle;
import logik.AsyncData;
import logik.BookingApplication;
import logik.CalenderController;
import logik.Callback;
import logik.VaskeTidController;

public class ShowMonthActivity extends AppCompatActivity implements View.OnClickListener, Callback {


    List<VaskeTavle> dates;
    GridView gridView;
    TextView maanedText, prev, next, week;
    int month;
    VaskeTidController vs;
    LocalDate startDay;
    ProgressDialog pDiag;
    boolean[] erDagLedig;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vs = BookingApplication.vtCont;

        month = CalenderController.getToday().getMonthOfYear();
        startDay = CalenderController.getFirstMondayInCalender(month);
        System.out.println("activity " + startDay.toString());
        boolean landscape = getResources().getBoolean(R.bool.isLandscape);
        if (landscape) {
            setContentView(R.layout.activity_showmonth_landscape);
        } else {
            setContentView(R.layout.activity_showmonth);
        }


        //AsyncTask der har til opgave at sørge for at reservationerne er hentet, inden de checkes, ellers er der stor sandsynliged for at kalenderen vises forkert.
        pDiag = ProgressDialog.show(this, "Henter data fra server, ", " vent venligst", true);
        new AsyncData(this, pDiag).execute();

        gridView = (GridView) findViewById(R.id.gridView1);
        maanedText = (TextView) findViewById(R.id.maaned_text);

        week = (TextView) findViewById(R.id.showWeek);
        week.setOnClickListener(this);

        prev = (TextView) findViewById(R.id.month_decrease);
        prev.setOnClickListener(this);
        next = (TextView) findViewById(R.id.month_increase);
        next.setOnClickListener(this);


        BookingApplication.isMonth = true;

        gridView = (GridView) findViewById(R.id.gridView1);
        maanedText = (TextView) findViewById(R.id.maaned_text);
        maanedText.setText(CalenderController.getMonthInText(month));




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (vs.getErDagLedig().length == 7) {
                    BookingApplication.isMonth = true;
                    vs.fillVaskeTavle(startDay, null);
                }
                VaskeDag vDag = dates.get(0).getVaskeDage().get(position);
                long dato = vDag.getVasketider().get(0).getDato();
                Intent i = new Intent(getApplicationContext(), VisVaskeDagActivity.class);
                Bundle b = new Bundle();

                b.putLong("Dato", dato);
                i.putExtras(b);
                getApplicationContext().startActivity(i);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int oldMonth = month;
        if (v == prev) {
            month--;

        } else if (v == next) {
            month++;
        } else if (v == week) {
            Intent i = new Intent(this, ShowWeekActivity.class);
            startActivity(i);
        }

        if (oldMonth != month) {
            maanedText.setText(CalenderController.getMonthInText(month));
            final LocalDate startDay = CalenderController.getFirstMondayInCalender(month);

            //Progress dialog vises, hvis der er lang loading tid
            pDiag = ProgressDialog.show(this, "Henter data fra server, ", " vent venligst", true);


            //AsyncTask der har til opgave at sørge for at reservationerne er hentet, inden de checkes, ellers er der stor sandsynliged for at kalenderen vises forkert.
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    BookingApplication.hentReservationer(CalenderController.dateToMillis(startDay), CalenderController.dateToMillis(CalenderController.getLastDayInCalender(month)));
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pDiag.dismiss();
                }
            }.execute();
            dates = vs.fillVaskeTavle(startDay, null);
            BookingApplication.isMonth = true;

            gridView.setAdapter(new CalenderView(this, dates, vs.getErDagLedig(), false));
        }
    }

    @Override
    public void onEventCompleted() {

        //TODO Der skal hentes den bruger der er logget ind i appen, og på baggrund af denne skal der findes boligselskab og tavle

        dates = vs.fillVaskeTavle(startDay, null);
        erDagLedig = vs.getErDagLedig();

        BookingApplication.isMonth = true;

        gridView = (GridView) findViewById(R.id.gridView1);



        gridView.setAdapter(new CalenderView(this, dates, erDagLedig, false));






    }

    @Override
    public void onEventFailed() {

    }
}





