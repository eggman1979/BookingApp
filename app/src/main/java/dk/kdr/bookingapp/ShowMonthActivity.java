package dk.kdr.bookingapp;

import android.content.Intent;
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
    TextView maanedText, prev, next;
    int month;
    VaskeTidController vs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vs = BookingApplication.vtCont;

        month = CalenderController.getToday().getMonthOfYear();
        LocalDate startDay = CalenderController.getFirstMondayInCalender(month);
        System.out.println("activity " + startDay.toString());
        vs.fillVaskeTavle(startDay, null);

        vs.opretLedighedsTabel();


        boolean landscape = getResources().getBoolean(R.bool.isLandscape);
        if (landscape) {
            setContentView(R.layout.activity_showmonth_landscape);
        } else {
            setContentView(R.layout.activity_showmonth);
        }


        dates = BookingApplication.vtCont.fillVaskeTavle(startDay, null);

        gridView = (GridView) findViewById(R.id.gridView1);

        maanedText = (TextView) findViewById(R.id.maaned_text);

        maanedText.setText(CalenderController.getMonthInText(month));

        prev = (TextView) findViewById(R.id.month_decrease);
        prev.setOnClickListener(this);
        next = (TextView) findViewById(R.id.month_increase);
        next.setOnClickListener(this);


        //TODO Der skal hentes den bruger der er logget ind i appen, og på baggrund af denne skal der findes boligselskab og tavle
        gridView.setAdapter(new CalenderView(this, dates, vs.getErDagLedig()));

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
        if (v == prev) {
            month--;

        } else if (v == next) {
            month++;
        }
        maanedText.setText(CalenderController.getMonthInText(month));
        LocalDate startDay = CalenderController.getFirstMondayInCalender(month);
//        System.out.println(CalenderController.dateToMillis(startDay));
//        System.out.println(CalenderController.dateToMillis(CalenderController.getLastDayInCalender(month)));
        BookingApplication.hentReservationer(CalenderController.dateToMillis(startDay), CalenderController.dateToMillis(CalenderController.getLastDayInCalender(month)));
        dates = BookingApplication.vtCont.fillVaskeTavle(startDay, null);

        gridView.setAdapter(new CalenderView(this, dates, vs.getErDagLedig()));

    }
}





