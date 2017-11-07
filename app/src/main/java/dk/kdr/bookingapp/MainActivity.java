package dk.kdr.bookingapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import logik.BookingApplication;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideAllBars();
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
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
        Intent i = new Intent(this, ShowMonthActivity.class);
        startActivity(i);

    }
}
