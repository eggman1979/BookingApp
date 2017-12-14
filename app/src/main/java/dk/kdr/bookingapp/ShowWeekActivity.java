package dk.kdr.bookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
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
import logik.BookingApplication;
import logik.CalenderController;
import logik.VaskeTidController;

public class ShowWeekActivity extends AppCompatActivity implements View.OnClickListener {

    int week = 0;
    ListView weekList;
    TextView prevWeek, currentWeek, nextWeek, changeToMonth;
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
        prevWeek.setOnClickListener(this);
        currentWeek = (TextView) findViewById(R.id.currentWeek);
        nextWeek = (TextView) findViewById(R.id.nextWeek);
        nextWeek.setOnClickListener(this);
        changeToMonth = (TextView) findViewById(R.id.monthView);
        changeToMonth.setOnClickListener(this);


        week = CalenderController.getToday().getWeekOfWeekyear();
        currentWeek.setText("uge " + week);

        final LocalDate startDag = CalenderController.getFirstDayOfWeek(week);
        final LocalDate slutDato = CalenderController.getLastDayOfWeek(week);

        tavler = vtc.fillVaskeTavle(startDag, slutDato);


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
        int weekInYear = CalenderController.getWeek(week );
        currentWeek.setText("uge " + weekInYear);
        final LocalDate startDag = CalenderController.getFirstDayOfWeek(week);
        final LocalDate slutDato = CalenderController.getLastDayOfWeek(week);

        tavler = vtc.fillVaskeTavle(startDag, slutDato);
        if (vtc.getErDagLedig() != null) {
            weekList.setAdapter(new CalenderView(this, tavler, vtc.getErDagLedig(), true));
        }
    }
}
