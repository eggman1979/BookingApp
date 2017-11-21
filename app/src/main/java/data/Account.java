package data;

import java.io.Serializable;

/**
 * Created by KimdR on 20-11-2017.
 * Midlertidig klasse til at gemme Account information i, indtil det er blevet besluttet hvordan
 * brugernavn og åasswprd slaæ ge,,es
 */


public class Account implements Serializable{

    private String password;
    private int brugerID;
    private String boligforening;


    public Account(String password, int brugerID, String boligforening){
        this.password = password;
        this.brugerID = brugerID;
        this.boligforening = boligforening;

    }

    public String getBoligforening() {
        return boligforening;
    }

    public void setBoligforening(String boligforening) {
        this.boligforening = boligforening;
    }

    public int getBrugerID() {

        return brugerID;
    }

    public void setBrugerID(int brugerID) {
        this.brugerID = brugerID;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
