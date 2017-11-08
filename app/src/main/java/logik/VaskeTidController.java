package logik;

import android.util.Log;

import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.Reservation;
import data.VaskeBlok;
import data.VaskeDag;
import data.VaskeTavle;
import data.VaskeTid;

/**
 * Created by KimdR on 22-10-2017.
 */

public class VaskeTidController {

    private List<Reservation> reservations;
    private List<VaskeTavle> tavler;
    private List<VaskeDag> vaskeDage;
    private List<VaskeBlok> vBlokke;
    private boolean[] erDagLedig;


    public final int ANTAL_DAGE_I_KALENDER = 42; // 7 dage om ugen * 6 uger

    public boolean[] getErDagLedig() {
        return erDagLedig;
    }

    public void setErDagLedig(boolean[] erDagLedig) {
        this.erDagLedig = erDagLedig;
    }

    public List<VaskeDag> createVaskeDage(LocalDate startDag, LocalDate slutDag, int tavleId) {

        vaskeDage = new ArrayList<>();
        //Tager én dag af gangen i kalenderen

        for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {

            // System.out.println("************************************************************");
            long dateInMilli = CalenderController.dateToMillis(startDag.plusDays(i));
//            System.out.println("Datoen er " + startDag.plusDays(i));
            //System.out.println("VT controller" + CalenderController.millisToDate(dateInMilli));
            int antalBlokke = vBlokke.size();
            List<VaskeTid> vTider = new ArrayList<>();

            for (int j = 0; j < antalBlokke; j++) {


                VaskeTid vTid = new VaskeTid(dateInMilli, tavleId, vBlokke.get(j));
                for (Reservation res : reservations) {
//                    System.out.println("Reservation har datoen :" + CalenderController.millisToDate(res.getDato()));
//                    System.out.println("Reservatoinen har vaskeblok nummer " + res.getvaskeBlokID() + " tavleID : " +res.getTavleID() + " j =" + j + " Dato = " + (res.getDato() == dateInMilli)); // && res.getTavleID() == tavleId));
                    if (res.getvaskeBlokID() == (j + 1) && res.getDato() == dateInMilli && res.getTavleID() == tavleId) {
                        vTid.setReservation(res);
                        //            System.out.println("Der er blevet sat en reservation!!! " + i);

                    }
                }
                vTider.add(vTid);
            }
            VaskeDag vd = new VaskeDag(antalBlokke, vTider);
            vaskeDage.add(vd);
        }
        return vaskeDage;
    }

    public List<VaskeTavle> fillVaskeTavle(LocalDate startDag, LocalDate slutDag) {
        for (VaskeTavle tavle : tavler) {
            tavle.setVaskeDage(createVaskeDage(startDag, slutDag, tavle.getTavleID()));
        }
        opretLedighedsTabel();
        return tavler;
    }

    public List<VaskeTavle> getVaskeTavler() {
        return tavler;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setVaskeTavler(List<VaskeTavle> tavler) {
        this.tavler = tavler;
    }

    public List<VaskeDag> getVaskeDage() {
        return vaskeDage;
    }

    public void setvBlokke(List<VaskeBlok> vBlokke) {
        this.vBlokke = vBlokke;
    }


    public void opretLedighedsTabel() {
        erDagLedig = new boolean[vaskeDage.size()];

        /*
        Følgende loop skal gå igennem alle dage, og checke om der findes en vasketid, dette skal gøre på alle tavler, med reservation == null, i såfald skal pågældende felt i erDagLedig arrayet vendes til at være sandt
         */
        for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
            for (int j = 0; j < vBlokke.size(); j++) {
                for (int k = 0; k < tavler.size(); k++) {
                    if (tavler.get(k).getVaskeDage().get(i).getVasketider().get(j).getReservation() == null) {
                        erDagLedig[i] = true;

                    }
                }
            }
            System.out.println("VaskeDag " + i + " = " + erDagLedig[i]);
        }

    }

    public boolean[] ledigeVaskeTider(long dato) {
        boolean[] ledigeTider = new boolean[vBlokke.size()];
        int index = 0;
        for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
            if (getVaskeDage().get(i).getVasketider().get(0).getDato() == dato) {
                index = i;
            }
        }

        for (int i = 0; i < vBlokke.size(); i++) {
            for (VaskeTavle tavle : tavler) {
                if (tavle.getVaskeDage().get(index).getVasketider().get(i).getReservation() == null)
                    ledigeTider[i] = true;
            }
        }
        return ledigeTider;
    }

    public VaskeDag findVaskeDag(long dato) {
        for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
            if (getVaskeDage().get(i).getVasketider().get(0).getDato() == dato) {
                return vaskeDage.get(i);
            }
        }
        return null;
    }
    public List<VaskeTid> findVaskeTiderFraBlok(long dato){
        List<VaskeTid> vList = new ArrayList<>();

        return vList;
    }
}