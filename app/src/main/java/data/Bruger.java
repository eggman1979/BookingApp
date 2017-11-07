package data;

import java.io.Serializable;


public class Bruger implements Serializable {

	/**
	 * En klasse der repræsenterer en vaskeribruger
	 */

	private int brugerID;
	private int boligForeningID;
	private int ANTAL_NOEGLER;
	private int brugteNoegler;
	private String navn;

/**
 * En general konstrukt�r der skaber objekter til afsendelse
 * @param brugerID
 * @param boligForeningID
 * @param navn
 * @param ANTAL_NOEGLER
 */
	public Bruger(int brugerID, int boligForeningID ,String navn, int ANTAL_NOEGLER){
		this.brugerID = brugerID;
		this.boligForeningID = boligForeningID;
		this.ANTAL_NOEGLER = ANTAL_NOEGLER;
		this.navn = navn;
		}

	public Bruger(){ }
	
	/**
	 * Konstrukt�r der bruges til at oprette objekter til lagring i database
	 * @param boligForeningID
	 * @param navn
	 * @param ANTAL_NOEGLER
	 */
	public Bruger(int boligForeningID ,String navn, int ANTAL_NOEGLER){
		this.boligForeningID = boligForeningID;
		this.ANTAL_NOEGLER = ANTAL_NOEGLER;
		this.navn = navn;
	}

	public int getBrugerID() {
		return brugerID;
	}


	public void setBrugerID(int brugerID) {
		this.brugerID = brugerID;
	}


	public int getBoligForeningID() {
		return boligForeningID;
	}


	public void setBoligForeningID(int boligForeningID) {
		this.boligForeningID = boligForeningID;
	}


	public int getBrugteNoegler() {
		return brugteNoegler;
	}


	public void setBrugteNoegler(int brugteNoegler) {
		this.brugteNoegler = brugteNoegler;
	}


	public String getNavn() {
		return navn;
	}


	public void setNavn(String navn) {
		this.navn = navn;
	}


	public int getANTAL_NOEGLER() {
		return ANTAL_NOEGLER;
	}
}
