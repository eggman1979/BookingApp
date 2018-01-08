package dk.kdr.bookingapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.List;

import data.VaskeDag;
import data.VaskeTavle;
import logik.AsyncData;
import logik.BookingApplication;
import logik.CalenderController;
import logik.Callback;
import logik.VaskeTidController;

public class ShowWeekActivity extends BaseActivity implements Callback, View.OnClickListener {

    int week = 0;
    ListView weekList;
    TextView prevWeek, currentWeek, nextWeek, changeToMonth;
    VaskeTidController vtc;
    List<VaskeTavle> tavler;
    ProgressDialog pDiag;
    DateTime startDag;
    DateTime slutDag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_week);
        BookingApplication.isMonth = false;
        vtc = BookingApplication.vtCont;

        pDiag = ProgressDialog.show(this, "Henter data fra server, ", " vent venligst", true);
        new AsyncData(this, pDiag).execute();

        weekList = (ListView) findViewById(R.id.weekList);
        prevWeek = (TextView) findViewById(R.id.prevWeek);
        prevWeek.setOnClickListener(this);
        currentWeek = (TextView) findViewById(R.id.currentWeek);
        nextWeek = (TextView) findViewById(R.id.nextWeek);
        nextWeek.setOnClickListener(this);
        changeToMonth = (TextView) findViewById(R.id.monthView);
        changeToMonth.setOnClickListener(this);


        week = CalenderController.getToday().getWeekOfWeekyear();
        currentWeek.setText("uge " + week);

        startDag = CalenderController.getFirstDayOfWeek(week);
        slutDag = CalenderController.getLastDayOfWeek(week);

        tavler = vtc.fillVaskeTavle(startDag, slutDag);

        if (vtc.getErDagLedig() != null) {
            weekList.setAdapter(new CalenderView(this, tavler, vtc.getErDagLedig(), true));
        }

        weekList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                VaskeDag vDag = tavler.get(0).getVaskeDage().get(position);
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
        if (v == changeToMonth) {
            BookingApplication.isMonth = true;
            Intent i = new Intent(this, ShowMonthActivity.class);
            startActivity(i);
        } else if (v == prevWeek) {
            week--;

        } else if (v == nextWeek) {
            week++;
        }
        int weekInYear = CalenderController.getWeek(week);
        currentWeek.setText("uge " + weekInYear);
        final DateTime startDag = CalenderController.getFirstDayOfWeek(week);
        final DateTime slutDato = CalenderController.getLastDayOfWeek(week);

        tavler = vtc.fillVaskeTavle(startDag, slutDato);
        if (vtc.getErDagLedig() != null) {
            weekList.setAdapter(new CalenderView(this, tavler, vtc.getErDagLedig(), true));
        }
    }


    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Afslut Vaskebooking")
                    .setMessage("Du er ved at afslutte Vaskebooking, er du sikker=")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ShowWeekActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else{ super.onBackPressed();}
    }

    @Override
    public void onEventCompleted(String msg) {
        tavler = vtc.fillVaskeTavle(startDag, slutDag);


        BookingApplication.isMonth = false;
        weekList.setAdapter(new CalenderView(this, tavler, vtc.getErDagLedig(), true));
    }

    @Override
    public void onEventFailed(String msg) {

    }
}
