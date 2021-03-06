package dk.kdr.bookingapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import org.joda.time.DateTime;
import java.util.List;
import data.VaskeDag;
import data.VaskeTavle;
import logik.AsyncData;
import logik.BookingApplication;
import logik.CalenderController;
import logik.Callback;
import logik.VaskeTidController;

public class ShowMonthActivity extends BaseActivity implements View.OnClickListener, Callback {


    List<VaskeTavle> dates;
    GridView gridView;
    TextView maanedText, prev, next, week;
    int month;
    VaskeTidController vs;
    DateTime startDay;
    ProgressDialog pDiag;
    boolean[] erDagLedig;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vs = BookingApplication.vtCont;


        month = CalenderController.getToday().getMonthOfYear();
        startDay = CalenderController.getFirstMondayInCalender(month);

        setContentView(R.layout.activity_showmonth);

        System.out.println("WAKAWAKA");
        //AsyncTask der har til opgave at sørge for at reservationerne er hentet, inden
        // checkes, ellers er der stor sandsynliged for at kalenderen vises forkert.
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
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pDiag.dismiss();
    }

    @Override
    public void onClick(View v) {
        int oldMonth = month;
        if (v == prev) {
            month--;

        } else if (v == next) {
            month++;
        } else if (v == week) {
            BookingApplication.isMonth = false;
            Intent i = new Intent(this, ShowWeekActivity.class);
            startActivity(i);
        }

        if (oldMonth != month) {
            maanedText.setText(CalenderController.getMonthInText(month));
            startDay = CalenderController.getFirstMondayInCalender(month);

            //Progress dialog vises, hvis der er lang loading tid
            final ProgressDialog pDiag = ProgressDialog.show(this, "Henter data fra server, ", " vent venligst", true);

            new AsyncData(this, pDiag).execute();
        }
    }

    @Override
    public void onEventCompleted(String msg) {


        dates = vs.fillVaskeTavle(startDay, null);
        erDagLedig = vs.getErDagLedig();

        BookingApplication.isMonth = true;
        gridView.setAdapter(new CalenderView(this, dates, vs.getErDagLedig(), false));
    }

    @Override
    public void onEventFailed(String msg) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Fejl")
                .setMessage("Serveren kunne ikke kontaktes")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ShowMonthActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
                            ShowMonthActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else {super.onBackPressed();}

    }
}




