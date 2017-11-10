package dk.kdr.bookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import data.VaskeDag;
import data.VaskeTavle;
import data.VaskeTid;
import logik.BookingApplication;
import logik.CalenderController;
import logik.VaskeTidController;

public class ShowWeekActivity extends AppCompatActivity {

    int week = 0;
    ListView weekList;
    TextView prevWeek, currentWeek, nextWeek;
    VaskeTidController vtc;
    List<VaskeTavle> tavler;
    ProgressDialog pDiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_week);
        BookingApplication.isMonth = false;
        vtc = BookingApplication.vtCont;

        weekList = (ListView) findViewById(R.id.weekList);
        prevWeek = (TextView) findViewById(R.id.prevWeek);
        currentWeek = (TextView) findViewById(R.id.currentWeek);
        nextWeek = (TextView) findViewById(R.id.nextWeek);


        week = CalenderController.getToday().getWeekOfWeekyear();
        final LocalDate startDag = CalenderController.getFirstDayOfWeek(week);
        final LocalDate slutDato = CalenderController.getLastDayOfWeek(week);

        pDiag = ProgressDialog.show(this, "Henter data fra server, ", " vent venligst", true);

        //AsyncTask der har til opgave at sørge for at reservationerne er hentet, inden de checkes, ellers er der stor sandsynliged for at kalenderen vises forkert.
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                System.out.println("ASKYNK I UGEAKTIVITET før KALD *" + vtc.getVaskeTavler().get(0).getVaskeDage().size());
                BookingApplication.hentReservationer(CalenderController.dateToMillis(startDag), CalenderController.dateToMillis(slutDato));


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDiag.dismiss();
            }
        }.execute();
        tavler = vtc.fillVaskeTavle(startDag, slutDato);
        int check = 0;
        for (VaskeTavle tavle : tavler) {
            for(VaskeDag dag : tavle.getVaskeDage())
                for(VaskeTid tid : dag.getVasketider())
                    if(tid. getReservation() != null){
                        System.out.println(check++);

            }
        }
        System.out.println("Antallet af tavler er " + tavler.size() + "; antallet af vaskedage: " + vtc.getErDagLedig().length);

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
}