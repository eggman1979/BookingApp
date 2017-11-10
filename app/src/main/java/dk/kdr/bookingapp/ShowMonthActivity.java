package dk.kdr.bookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
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
import logik.BookingApplication;
import logik.CalenderController;
import logik.VaskeTidController;

public class ShowMonthActivity extends AppCompatActivity implements View.OnClickListener {


    List<VaskeTavle> dates;
    GridView gridView;
    TextView maanedText, prev, next, week;
    int month;
    VaskeTidController vs;
    ProgressDialog pDiag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vs = BookingApplication.vtCont;
        BookingApplication.isMonth = true;

        month = CalenderController.getToday().getMonthOfYear();
        LocalDate startDay = CalenderController.getFirstMondayInCalender(month);
        System.out.println("activity " + startDay.toString());
        vs.fillVaskeTavle(startDay, null);


        boolean landscape = getResources().getBoolean(R.bool.isLandscape);
        if (landscape) {
            setContentView(R.layout.activity_showmonth_landscape);
        } else {
            setContentView(R.layout.activity_showmonth);
        }


        dates = vs.fillVaskeTavle(startDay, null);

        gridView = (GridView) findViewById(R.id.gridView1);
        maanedText = (TextView) findViewById(R.id.maaned_text);
        maanedText.setText(CalenderController.getMonthInText(month));
        week = (TextView) findViewById(R.id.showWeek);
        week.setOnClickListener(this);

        prev = (TextView) findViewById(R.id.month_decrease);
        prev.setOnClickListener(this);
        next = (TextView) findViewById(R.id.month_increase);
        next.setOnClickListener(this);


        //TODO Der skal hentes den bruger der er logget ind i appen, og på baggrund af denne skal der findes boligselskab og tavle
        gridView.setAdapter(new CalenderView(this, dates, vs.getErDagLedig(),false));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
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
        }
        else if(v == week){
            Intent i = new Intent(this, ShowWeekActivity.class);
            startActivity(i);
        }

        if(oldMonth != month) {
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

            gridView.setAdapter(new CalenderView(this, dates, vs.getErDagLedig(), false));
        }
    }
}





