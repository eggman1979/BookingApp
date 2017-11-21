package logik;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;

import data.Account;
import data.Bruger;
import data.Reservation;

/**
 * Created by KimdR on 01-11-2017.
 */

public class BookingApplication extends Application {

    private static BookingApplication ourInstance = new BookingApplication();
    public static boolean isBrugerSet =false;
    private ConnectService.LocalBinder binder;

    public static boolean isMonth = false;
    public static ServiceConnection connection;
    public static ConnectService cService = null;
    public static boolean isBound = false;
    public static Bruger bruger = null;
    public static Account account = null;
    public static VaskeTidController vtCont;
    public static boolean isDataLoadedFromServer = false;
    public static boolean isBrugerLoaded = false;
    public static LokalPersistens persistent;
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor prefEditor;


    //  SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);


    @Override
    public void onCreate() {
        super.onCreate();

        prefs = getSharedPreferences("init", 0);
        prefEditor = prefs.edit();

        persistent = new LokalPersistens(this);

        startBinding();

        isBrugerSet = prefs.getBoolean("isBrugerSet", false);

        Intent intent = new Intent(this, ConnectService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        vtCont = new VaskeTidController();
        if (isBrugerSet) {
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isBound) {
                        System.out.println("LOADES");
                        BookingApplication.bruger = (Bruger) persistent.hentGemtFil("bruger");
                        BookingApplication.account = (Account) persistent.hentGemtFil("account");
                        isBrugerLoaded = bruger != null && account != null;

                    } else {
                        h.postDelayed(this, 100);
                    }
                }

            }, 100);
        }

//        //TODO His brugeren er null, skal der logges ind, og der skal ikke auto initialiseres data
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

    /**
     * initializeAppData, er midlertidigt lukket, indtil en bedre løsning er fundet.
     * Ind til dag benyttes servicen, til at hente data, fra aktiviteterne,
     */
    @Deprecated
    public static void initializeAppData() {
        vtCont = new VaskeTidController();

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
//              if (BookingApplication.isBound){ //&& bruger != null) {
                try {
                    //    cService.hentGemtReservation("res");
                    System.out.println("Der er nu hentet og størrelesn er : " + vtCont.getReservations().size());
//
//
                    Long sidstHentet = vtCont.getSidstHentet();
                    System.out.println("SidstHentet er " + sidstHentet);
////

                    cService.hentVaskeTavler();

                    Thread.sleep(100);
                    cService.hentVaskeBlokke();
                    Thread.sleep(100); //Har indsat sleep for ikke at spamme Servicen for meget.
                    cService.hentReservationer(bruger.getBoligForeningID(), sidstHentet);

                    isDataLoadedFromServer = true;


                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                } catch (
                        InterruptedException e)

                {
                    e.printStackTrace();
                }
            }
        }, 100);

//        h.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isDataLoadedFromServer) {
//                    vtCont.cleanReservation();
//
//                    System.out.println("DATA GEMMES");
////                    cService.gemData(vtCont.getReservations(), "res");
////                    cService.gemData(vtCont.getVaskeTavler(), "tavler");
////                    cService.gemData(vtCont.getvBlokke(), "blokke");
//
//                    System.out.println("Antal reservationer efter rens = " + vtCont.getReservations().size());
//                } else {
//                    h.postDelayed(this, 100);
//                }
//            }
//        }, 100);

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
                        cService.hentReservationer(bruger.getBoligForeningID(), vtCont.getSidstHentet());
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


    // Skal testes om cService er bound på dette tidspunkt
    public static Bruger login(final String userName, final String password, final String foreningID) {
        Bruger bruger = new Bruger();
        bruger = cService.login(userName, password, foreningID);
        return bruger;
    }


}