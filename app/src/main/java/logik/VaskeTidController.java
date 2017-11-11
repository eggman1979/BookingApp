package logik;

import android.util.Log;

import org.joda.time.Days;
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

import static org.joda.time.Days.daysBetween;

/**
 * Created by KimdR on 22-10-2017.
 */

public class VaskeTidController {

    final int DAG = 1;
    final int UGE = 7;
    final int MAANED = 42;

    private List<Reservation> reservations;
    private List<VaskeTavle> tavler;
    private List<VaskeDag> vaskeDage;

    public List<VaskeBlok> getvBlokke() {
        return vBlokke;
    }

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

        int antalDage = CalenderController.getDaysBetween(startDag, slutDag);

        vaskeDage = new ArrayList<>();

        //Tager én dag af gangen i kalenderen

        for (int i = 0; i < antalDage; i++) {

            long dateInMilli = CalenderController.dateToMillis(startDag.plusDays(i));
            int antalBlokke = vBlokke.size();
            List<VaskeTid> vTider = new ArrayList<>();

            for (int j = 0; j < antalBlokke; j++) {


                VaskeTid vTid = new VaskeTid(dateInMilli, tavleId, vBlokke.get(j));
                if (reservations != null) {
                    for (Reservation res : reservations) {

                        if (res.getvaskeBlokID() == (j + 1) && res.getDato() == dateInMilli && res.getTavleID() == tavleId) {
                            vTid.setReservation(res);
                        }
                    }
                    vTider.add(vTid);
                }
            }
            VaskeDag vd = new VaskeDag(antalBlokke, vTider);
            vaskeDage.add(vd);
        }
        return vaskeDage;
    }

    public List<VaskeTavle> fillVaskeTavle(LocalDate startDag, LocalDate slutDag) {
        int antalDage = CalenderController.getDaysBetween(startDag, slutDag);

        for (VaskeTavle tavle : tavler) {
            tavle.setVaskeDage(createVaskeDage(startDag, slutDag, tavle.getTavleID()));
        }
        opretLedighedsTabel(antalDage);
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


    public boolean[] opretLedighedsTabel(int antalDage) {
        erDagLedig = new boolean[antalDage];
        System.out.println("*********** " + antalDage + " fra opretLedighedsTabel() **********");
        /*
        Følgende loop skal gå igennem alle dage, og checke om der findes en vasketid, dette skal gøre på alle tavler, med reservation == null, i såfald skal pågældende felt i erDagLedig arrayet vendes til at være sandt
         */
        for (int i = 0; i < antalDage; i++) {
            for (int j = 0; j < vBlokke.size(); j++) {
                for (int k = 0; k < tavler.size(); k++) {
                    if (tavler.get(k).getVaskeDage().get(i).getVasketider().get(j).getReservation() == null) {
                        erDagLedig[i] = true;


                    }
                }
            }
        }
        System.out.println("*********** " + antalDage + " fra opretLedighedsTabel() **********");
        return erDagLedig;
    }


    /**
     * Returerer en vaskedag per vasketavle der findes
     *
     * @param dato den dato vaskedagen skal foregå
     * @return
     */
    public List<VaskeDag> findVaskeDag(long dato) {

        List<VaskeDag> vDage = new ArrayList<>();
        int index = -1;

        for (VaskeTavle tavle : tavler) {
            if (index != -1) {
                vDage.add(tavle.getVaskeDage().get(index));
            }
            for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
                VaskeDag vDag = tavle.getVaskeDage().get(i);
                if (vDag.getVasketider().get(0).getDato() == dato) {
                    index = i;
                    vDage.add(tavle.getVaskeDage().get(index));
                }
            }
        }

        return vDage;
    }

    public boolean[] ledigeVaskerum(long dato, int blok) {

        boolean[] ledigeRum = new boolean[tavler.size()];
        int index = 0;

        for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
            if (getVaskeDage().get(i).getVasketider().get(0).getDato() == dato) {

                index = i;
            }
        }

        for (int i = 0; i < tavler.size(); i++) {
            VaskeTavle tavle = tavler.get(i);
            if (tavle.getVaskeDage().get(index).getVasketider().get(blok).getReservation() == null) {
                ledigeRum[i] = true;
            }
        }
        return ledigeRum;
    }

    public boolean[] ledigeVaskeTider(long dato, boolean isMonth) {
        // printAllReservations();

        int antalDage = ANTAL_DAGE_I_KALENDER;
        if (!isMonth) {
            antalDage = 7;
        }
        boolean[] ledigeTider = new boolean[vBlokke.size()];
        System.out.println("ræder ind i ledigeVaskeDage antal dage er " + antalDage + " ØØØØØØ");
        int index = 0;
        for (int i = 0; i < antalDage; i++) {
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

//    public void printAllReservations() {
//        int i = 0;
//        for (VaskeTavle tavle : tavler) {
//            for (VaskeDag vDag : tavle.getVaskeDage()) {
//                for (VaskeTid vTid : vDag.getVasketider()) {
//                    System.out.println(vTid.getReservation() + " " + i++);
//                }
//            }
//        }
//    }
}