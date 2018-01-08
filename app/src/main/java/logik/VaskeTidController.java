package logik;

import android.util.Log;

import org.joda.time.DateTime;
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

    public VaskeTidController() {
        reservations = new ArrayList<>();
        tavler = new ArrayList<>();
        vBlokke = new ArrayList<>();
    }

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

    public List<VaskeDag> createVaskeDage(DateTime startDag, DateTime slutDag, int tavleId) {

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

    public List<VaskeTavle> fillVaskeTavle(DateTime startDag, DateTime slutDag) {

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
        /*
        Følgende loop skal gå igennem alle dage, og checke om der findes en vasketid, dette skal gøre på alle tavler, med reservation == null, i såfald skal pågældende felt i erDagLedig arrayet vendes til at være sandt
         */

        if (tavler != null && tavler.size() > 0) {
            for (int i = 0; i < antalDage; i++) {
                for (int j = 0; j < vBlokke.size(); j++) {
                    for (int k = 0; k < tavler.size(); k++) {
                        if (tavler.get(k).getVaskeDage().get(i).getVasketider().get(j).getReservation() == null) {
                            erDagLedig[i] = true;


                        }
                    }
                }
            }

            return erDagLedig;
        } else {

            return null;
        }
    }


    /**
     * Returerer en vaskedag per vasketavle der findes
     *
     * @param dato den dato vaskedagen skal foregå
     * @return
     */
    public List<VaskeDag> findVaskeDag(long dato) {

        List<VaskeDag> vDage = new ArrayList<>();


        for (VaskeTavle tavle : tavler) {

            for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
                VaskeDag vdag = tavle.getVaskeDage().get(i);
                if (vdag.getVasketider().get(0).getDato() == dato) {
                    vDage.add(vdag);
                    break;
                }
            }
        }

        return vDage;
    }

    public List<VaskeTid> findVaskeTid(long dato, int blok) {
        List<VaskeTid> tider = new ArrayList<>();
        for (VaskeTavle tavle : tavler) {

            for (int i = 0; i < ANTAL_DAGE_I_KALENDER; i++) {
                VaskeTid vTid = tavle.getVaskeDage().get(i).getVasketider().get(blok);
                if (vTid.getDato() == dato) {
                    tider.add(vTid);
                    break;
                }
            }
        }
        return tider;
    }

    public boolean[] ledigeVaskerum(long dato, int blok) {

        int antalDage = 42;
        if (!BookingApplication.isMonth) {
            antalDage = 7;
        }
        boolean[] ledigeRum = new boolean[tavler.size()];
        int index = 0;


        LocalDate date = new LocalDate(dato);

        for (int i = 0; i < antalDage; i++) {
            if (tavler.get(0).getVaskeDage().get(i).getVasketider().get(0).getDato() == dato) {
                index = i;
                break;
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

    public void addReservations(List<Reservation> res) {
        reservations.addAll(res);
    }

    public List<Reservation> getReservations() {

        return reservations;
    }

    public long getSidstHentet() {
        long sidstHentet = 0;
        if (reservations.size() > 0) {
            for (Reservation r : reservations) {
                if (r.getTilfoejetDato() > sidstHentet) {
                    sidstHentet = r.getTilfoejetDato();

                }
            }
        }else{
            return sidstHentet;
        }

        return sidstHentet + 1;
    }

    public void cleanReservation() {
        List<Reservation> tempList = new ArrayList<>();
        int count = 0;
        for (Reservation res : reservations) {
            for (Reservation res2 : reservations) {
                if (res.getReservationID() == res2.getReservationID()) {
                    if (res.getTilfoejetDato() < res2.getTilfoejetDato()) {
                        tempList.add(res);
                        count++;
                    }
                }
            }

        }
        for (Reservation res : tempList) {
            reservations.remove(res);
        }

    }

    public Reservation getReservation(long dato, int tavle, int blok) {
        for (Reservation res : reservations) {
            if (res.getDato() == dato && res.getTavleID() == (tavle) && res.getvaskeBlokID() == (blok)) {
                return res;
            }
        }
        return null;
    }
}