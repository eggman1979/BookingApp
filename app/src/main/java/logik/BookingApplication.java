package logik;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.Bruger;
import data.Reservation;
import data.VaskeTavle;

/**
 * Created by KimdR on 01-11-2017.
 */

public class BookingApplication extends Application {

    private static BookingApplication ourInstance = new BookingApplication();
    private ConnectService.LocalBinder binder;

    public static ServiceConnection connection;
    public static ConnectService cService = null;
    public static boolean isBound = false;
    public static Bruger bruger = new Bruger(1, 1, "Kim", 1); // Denne skal hentes enten fra server, eller skal være gemt lokalt i serialiseret fil
    public static VaskeTidController vtCont;


//    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    public void onCreate() {
        super.onCreate();

        startBinding();
        Intent intent = new Intent(this, ConnectService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (bruger != null) {
            initializeAppData();
        }else{
            //TODO Hvis brugeren er null, skal der logges ind, og der skal ikke auto initialiseres data
        }
    }

    public void startBinding() {
        /** Defines callbacks for service binding, passed to bindService() */
        connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                binder = (ConnectService.LocalBinder) service;

                cService = binder.getService();
                System.out.println("cService" + cService);

                isBound = true;
                System.out.println("isBound" + isBound);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                isBound = false;
            }
        };
    }


    public void initializeAppData() {
        vtCont = new VaskeTidController();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BookingApplication.isBound) {
                    try {
                        cService.hentReservationer(CalenderController.dateToMillis(CalenderController.getFirstMondayInCalender(LocalDate.now().getMonthOfYear())), CalenderController.dateToMillis(CalenderController.getLastDayInCalender(LocalDate.now().getMonthOfYear())), bruger.getBoligForeningID());
                        Thread.sleep(100); //Har indsat sleep for ikke at spamme Servicen for meget.
                        cService.hentVaskeTavler();
                        Thread.sleep(100);
                        cService.hentVaskeBlokke();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    h.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    public static void setReservation(List<Reservation> reservations) {
        vtCont.setReservations(reservations);
    }

    public static void hentReservationer(final long startDato, final long slutDato) {
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BookingApplication.isBound) {
                    try {
                        cService.hentReservationer(startDato, slutDato, bruger.getBoligForeningID());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    h.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    public void dropBinding() {
        unbindService(connection);
    }
}
