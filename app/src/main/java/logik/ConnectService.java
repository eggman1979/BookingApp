package logik;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import data.Account;
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


        String baseURL = "http://ubuntu4.javabog.dk:8842/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ue fra // Hjemmenet
//    String baseURL = "http://192.168.43.80:8080/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ude fra // telefon

    //    String baseURL = "http://192.168.0.110:8080/BookingServer/rest/"; //TODO SKal ændres til den rigtige server når der skal testes ude fra
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
            Log.w("data fra server ", resList.get(0).getBrugerID() + "  der er noget?");
            BookingApplication.vtCont.addReservations(resList);
            for (Reservation res : resList) {
                LocalDate dd = CalenderController.millisToDate(res.getDato());
                System.out.println(dd.toString() + " ReservationsID er " + res.getReservationID() + " VaskeBlok " + res.getvaskeBlokID());
            }
        } else {
            Log.w("Error", " Reservationerne er null");
        }
    }


//    }

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
        Log.w("data fra server", bf.getNavn() + "  der er noget?");
        BookingApplication.boligForening = bf;
        BookingApplication.persistent.gemData(BookingApplication.boligForening, "boligforening");

    }

    public void hentVaskeTavler() throws IOException {
        String line = "";
        InputStream is = null;
        try {

            URL url = new URL(baseURL + "vtService/vasketavler/" + BookingApplication.boligForening.getId());
            String data = openServiceConnection(url);
            Log.w("run: ", "");

            Gson gson = new Gson();
            ArrayList<VaskeTavle> tavleList = gson.fromJson(data, new TypeToken<List<VaskeTavle>>() {
            }.getType());
            Log.w("data fra server", tavleList.size() + "");
            BookingApplication.vtCont.setVaskeTavler(tavleList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void hentVaskeBlokke() throws IOException { //TODO Der skal et boligselskabs id med som parameter


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
            System.out.println("Kunne ikke få forbindelse med serveren...");
        }
        return line;
    }


    public void hentData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hentGemtReservation("res");
                hentGemtTavler();
                hentGemtBlokke();
            }
        }).start();
    }

    public int reserverVasketid(Reservation res) {
        int response = 500;
        try {
            
            URL url = new URL(baseURL+ "reservationService/reservationer");

            System.out.println(url);
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
         response = connection.getResponseCode() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void hentGemtReservation(String filNavn) {
        String FILNAVN = this.getFilesDir() + "/" + filNavn + ".ser";
        try {
            FileInputStream fileOutputStream = new FileInputStream(FILNAVN);
            ObjectInputStream inputStream = new ObjectInputStream(fileOutputStream);
            BookingApplication.vtCont.setReservations((List<Reservation>) inputStream.readObject());
            System.out.println(BookingApplication.vtCont.getReservations().size());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void hentGemtTavler() {
        String FILNAVN = this.getFilesDir() + "/tavler.ser";
        try {
            FileInputStream fileOutputStream = new FileInputStream(FILNAVN);
            ObjectInputStream inputStream = new ObjectInputStream(fileOutputStream);
            BookingApplication.vtCont.setVaskeTavler((List<VaskeTavle>) inputStream.readObject());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void hentGemtBlokke() {
        String FILNAVN = this.getFilesDir() + "/blokke.ser";
        try {
            FileInputStream fileOutputStream = new FileInputStream(FILNAVN);
            ObjectInputStream inputStream = new ObjectInputStream(fileOutputStream);
            BookingApplication.vtCont.setvBlokke((List<VaskeBlok>) inputStream.readObject());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}