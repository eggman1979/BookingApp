package logik;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import data.BoligForening;
import data.Reservation;
import data.VaskeBlok;
import data.VaskeTavle;


/**
 * Created by KimdR on 01-11-2017.
 */

public class ConnectService extends Service {

    private final IBinder mBinder = new LocalBinder();

    String baseURL = "http://192.168.0.13:8080/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ude fra
    //    String baseURL = "http://192.168.43.80:8080/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ude fra


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class LocalBinder extends Binder {
        ConnectService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ConnectService.this;
        }
    }

    public void hentReservationer(final long startDato, final long slutDato, final int boligID) throws IOException { //TODO Der skal et boligselskabs id med som parameter
        new Thread(new Runnable() {
            @Override
            public void run() {

                String line = "";
                InputStream is = null;

                try {

                    URL url = new URL(baseURL + "reservationService/reservationer/" + startDato + "/" + slutDato + "/" + boligID);
                    line = openServiceConnection(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();

                ArrayList<Reservation> resList = gson.fromJson(line, new TypeToken<List<Reservation>>() {
                }.getType());
                if (resList != null && resList.size() > 0) {
                    Log.w("data fra server", resList.get(0).getBrugerID() + "  der er noget?");
                    BookingApplication.setReservation(resList);
                }
            }
        }).start();
    }

    public void hentBoligforening(int boligforeningId) throws IOException {//TODO Der skal et boligselskabs id med som parameter
        final String urlExtend = "bfService/boligforeninger/" + boligforeningId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                String line = "";

                try {

                    URL url = new URL(baseURL + urlExtend);
                    line = openServiceConnection(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Gson gson = new Gson();
                BoligForening resList = gson.fromJson(line, BoligForening.class);
                Log.w("data fra server", resList.getNavn() + "  der er noget?");
            }


        }).start();
    }

    public void hentVaskeTavler() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {


                String line = "";
                InputStream is = null;

                try {

                    URL url = new URL(baseURL + "vtService/vasketavler/1  ");//TODO Der skal et boligselskabs id med som parameter
                    openServiceConnection(url);

                    Gson gson = new Gson();

                    ArrayList<VaskeTavle> resList = gson.fromJson(line, new TypeToken<List<VaskeTavle>>() {
                    }.getType());
                    Log.w("data fra server", resList.size() + "");
                    BookingApplication.vtCont.setVaskeTavler(resList);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    public void hentVaskeBlokke() throws IOException { //TODO Der skal et boligselskabs id med som parameter
        new Thread(new Runnable() {
            @Override
            public void run() {


                String line = "";
                InputStream is = null;

                try {

                    URL url = new URL(baseURL + "vaskebloksservice/vaskeblokke/1");
                    String data = openServiceConnection(url);
                    Log.w("run: ", "");

                    Gson gson = new Gson();
                    ArrayList<VaskeBlok> resList = gson.fromJson(data, new TypeToken<List<VaskeBlok>>() {
                    }.getType());
                    Log.w("data fra server", resList.size() + "");
                    BookingApplication.vtCont.setvBlokke(resList);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String openServiceConnection(URL url) {
        String line = "";
        InputStream is = null;
        try {
            try {

                System.out.println(url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.connect();
                is = conn.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));

                line = r.readLine();
                Log.w("run: ", line);


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is.close();

            }

        } catch (Exception e) {

        }
        return line;
    }

}
