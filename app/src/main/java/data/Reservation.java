package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import data.Bruger;

public class    Reservation implements Serializable, Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = 3827982888483436674L;
    private int brugerID;
    private long dato;
    private int vaskeBlokID;
    private int tavleID;
    private int reservationID;
    private int boligselskabID;
    private long tilfoejetDato;



    public Reservation() {
    }

    public Reservation(int reservationID, int brugerID, long dato, int vaskeBlokID, int boligselskabID, int tavleID,long tilfoejetDato) {
        this.reservationID = reservationID;
        this.brugerID = brugerID;
        this.dato = dato;
        this.vaskeBlokID = vaskeBlokID;
        this.tavleID = tavleID;
        this.tilfoejetDato = tilfoejetDato;
        this.boligselskabID = boligselskabID;


    }

    public Reservation( int brugerID, long dato, int vaskeBlokID, int boligselskabID, int tavleID,long tilfoejetDato) {
        this.brugerID = brugerID;
        this.dato = dato;
        this.vaskeBlokID = vaskeBlokID;
        this.boligselskabID = boligselskabID;
        this.tilfoejetDato = tilfoejetDato;
        this.tavleID = tavleID;

    }



    public int getBrugerID() {
        return brugerID;
    }

    public void setBrugerID(int brugerID) {
        this.brugerID = brugerID;
    }

    public int getVaskeBlokID() {
        return vaskeBlokID;
    }

    public void setVaskeBlokID(int vaskeBlokID) {
        this.vaskeBlokID = vaskeBlokID;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public long getTilfoejetDato() {
        return tilfoejetDato;
    }

    public void setTilfoejetDato(long tilfoejetDato) {
        this.tilfoejetDato = tilfoejetDato;
    }

    public int getBoligselskabID() {
        return boligselskabID;
    }

    public void setBoligselskabID(int boligselskabID) {
        this.boligselskabID = boligselskabID;
    }

    public void setBruger(int brugerID) {
        this.brugerID = brugerID;
    }

    public long getDato() {
        return dato;
    }

    public void setDato(long dato) {
        this.dato = dato;
    }

    public int getvaskeBlokID() {
        return vaskeBlokID;
    }

    public void setvaskeBlokID(int vaskeBlokID) {
        this.vaskeBlokID = vaskeBlokID;
    }

    public int getTavleID() {
        return tavleID;
    }

    public void setTavleID(int tavleID) {
        this.tavleID = tavleID;
    }

    protected Reservation(Parcel in) {
        brugerID = in.readInt();
        dato = in.readLong();
        vaskeBlokID = in.readInt();
        tavleID = in.readInt();
        reservationID = in.readInt();
        tilfoejetDato = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(brugerID);
        dest.writeLong(dato);
        dest.writeInt(vaskeBlokID);
        dest.writeInt(tavleID);
        dest.writeInt(reservationID);
        dest.writeLong(tilfoejetDato);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };


    public String toString(){
        return " reservationID: " + reservationID + " brugerID: " + brugerID + " dato: " + dato + " vaskeBlokID: " +  vaskeBlokID + " BoligID: "+ boligselskabID + " tavleID: "+ tavleID + "tilføjelsesDato: " + tilfoejetDato;
    }
}
