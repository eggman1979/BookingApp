package dk.kdr.bookingapp;


import android.app.ProgressDialog;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.Account;
import data.Bruger;
import logik.AsyncLogin;
import logik.BookingApplication;
import logik.Callback;

/**
 * Loginskærmen. - Viser tre indtastningsfelter, hvor brugeren skal udfylde bruger id, boligforening med navn samt password.
 * Når
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback {
    Button loginBtn;
    EditText user, pass, forening;
    String userName, password, foreningNavn;
    ProgressDialog pDiag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAllBars();

        Fabric.with(this, new Crashlytics());

        BookingApplication.bruger = (Bruger) BookingApplication.persistent.hentGemtFil("bruger");
        Account acc = (Account) BookingApplication.persistent.hentGemtFil("account");
        BookingApplication.account = acc;

        if (BookingApplication.bruger == null) {
            setContentView(R.layout.activity_main);
            user = (EditText) findViewById(R.id.bruger);
            pass = (EditText) findViewById(R.id.pass);
            forening = (EditText) findViewById(R.id.selskab);

            loginBtn = (Button) findViewById(R.id.loginBtn);
            loginBtn.setOnClickListener(this);

        } else {
            //Hvis brugeren allerede findes i appen, så skal den bare hoppe til showMonthActivity
            Intent i = new Intent(this, ShowMonthActivity.class);
            startActivity(i);
        }

    }

    public void hideAllBars() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onClick(View v) {
        //TODO Skal lave funktionalitet op imod rest serveren.


        userName = user.getText().toString();
        password = pass.getText().toString();
        foreningNavn = forening.getText().toString();
        pDiag = ProgressDialog.show(this, "Henter data fra server, ", " vent venligst", true);
        new AsyncLogin(this, userName, password).execute();


    }

    @Override
    public void onEventCompleted() {
        // Log in er gået godt og der kan skiftes aktivitet
        BookingApplication.account = new Account(password, Integer.parseInt(userName), foreningNavn);

        // Der sættes et flag på at brugeren er logget ind
        BookingApplication.prefEditor.putBoolean("isBrugerSet", true).commit();

        BookingApplication.persistent.gemData(BookingApplication.bruger, "bruger");
        BookingApplication.persistent.gemData(BookingApplication.account, "account");
        pDiag.dismiss();
        Intent i = new Intent(this, ShowMonthActivity.class);
        startActivity(i);
    }

    @Override
    public void onEventFailed() {
        pDiag.dismiss();
        Toast.makeText(this, " Der kunne ikke forbindes til serveren", Toast.LENGTH_SHORT).show();
        user.setText("");
        pass.setText("");
        forening.setText("");
        System.out.println("FAILED");
    }
}