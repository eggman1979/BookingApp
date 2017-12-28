package logik;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import data.BoligForening;
import data.Bruger;
import data.Reservation;
import data.VaskeBlok;
import data.VaskeTavle;


/**
 * Created by KimdR on 01-11-2017.
 */

public class ConnectService extends Service {

    private final IBinder mBinder = new LocalBinder();


    //        String baseURL = "http://ubuntu4.javabog.dk:8842/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ue fra // Server
    String baseURL = "http://10.123.199.195:8080/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ude fra // hjemmenet


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Bruger login(final String userName, final String password, final String foreningID) {
        BookingApplication.isBrugerSet = false;

        String line = "";
        InputStream is = null;

        try {

            URL url = new URL(baseURL + "brService/brugere/login/" + userName + "/" + password + "/" + foreningID);
            line = openServiceConnection(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        Bruger hentetBruger = gson.fromJson(line, Bruger.class);
        BookingApplication.bruger = hentetBruger;
        BookingApplication.isBrugerSet = true;
        BookingApplication.persistent.gemData(hentetBruger, "bruger");

        return hentetBruger;
    }


    class LocalBinder extends Binder {
        ConnectService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ConnectService.this;
        }
    }


    public void hentReservationer(final int boligID, final long sidstHentet) throws IOException { //TODO Der skal et boligselskabs id med som parameter

        String line = "";
        InputStream is = null;

        try {
            URL url = new URL(baseURL + "reservationService/reservationer/" + boligID + "/" + sidstHentet);
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
            BookingApplication.vtCont.addReservations(resList);
            for (Reservation res : resList) {
                DateTime dd = CalenderController.millisToDate(res.getDato());
            }
        } else {
        }
    }

    public void hentBoligforening(int boligforeningId) throws IOException {//TODO Der skal et boligselskabs id med som parameter
        final String urlExtend = "bfService/boligforeninger/" + boligforeningId;

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
        BoligForening bf = gson.fromJson(line, BoligForening.class);
        BookingApplication.boligForening = bf;
        BookingApplication.persistent.gemData(BookingApplication.boligForening, "boligforening");
    }

    public void hentVaskeTavler() throws IOException {
        String line = "";
        InputStream is = null;
        try {

            URL url = new URL(baseURL + "vtService/vasketavler/" + BookingApplication.boligForening.getId());
            String data = openServiceConnection(url);
            Gson gson = new Gson();
            ArrayList<VaskeTavle> tavleList = gson.fromJson(data, new TypeToken<List<VaskeTavle>>() {
            }.getType());
            BookingApplication.vtCont.setVaskeTavler(tavleList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void hentVaskeBlokke() throws IOException { //TODO Der skal et boligselskabs id med som parameter


        try {

            URL url = new URL(baseURL + "vaskebloksservice/vaskeblokke/" + BookingApplication.boligForening.getId());
            String data = openServiceConnection(url);

            Gson gson = new Gson();
            ArrayList<VaskeBlok> resList = gson.fromJson(data, new TypeToken<List<VaskeBlok>>() {
            }.getType());

            BookingApplication.vtCont.setvBlokke(resList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String openServiceConnection(URL url) {
        String line = "";
        InputStream is = null;
        try {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.connect();
                is = conn.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));

                line = r.readLine();


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
            //TODO skal håndteres
        }
        return line;
    }

    public String reserverVasketid(Reservation res) {
        int responseCode = 500;
        String response = "";
        String line = "";
        InputStream is = null;

        try {

            URL url = new URL(baseURL + "reservationService/reservationer");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json; charset=utf8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            Gson gson = new Gson();
            String resString = gson.toJson(res);
            writer.write(resString);
            writer.close();
            out.close();

            connection.connect();
            responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                is = connection.getInputStream();
            } else if (responseCode == 500) {
                is = connection.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();
            response = responseCode + "," + line;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public String deleteReservation(int reservationID) {
        InputStream is = null;
        String response = "";
        int responseCode = -500;
        try {
            URL url = new URL(baseURL + "reservationService/reservationer/" + reservationID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            is = connection.getInputStream();


            responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                is = connection.getInputStream();
            } else if (responseCode == 500) {
                is = connection.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            response = responseCode + "," + br.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

}