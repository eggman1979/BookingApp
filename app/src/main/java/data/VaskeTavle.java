package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class VaskeTavle implements Serializable, Parcelable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6977445431778097356L;
	private int tavleID;
	private int boligForeningID;
	private int antalBlokkePerDag;
	private List<VaskeDag> vaskeDage = new ArrayList<>();

	public List<VaskeDag> getVaskeDage() {


		return vaskeDage;
	}

	public void setVaskeDage(List<VaskeDag> vaskeDage) {
		this.vaskeDage = vaskeDage;
	}

	public int getAntalBlokkePerDag() {

		return antalBlokkePerDag;
	}

	public void setAntalBlokkePerDag(int antalBlokkePerDag) {
		this.antalBlokkePerDag = antalBlokkePerDag;
	}

	public VaskeTavle(){};
	
	public VaskeTavle(int tavleID, int boligForeningID, int antalBlokkePerDag, ArrayList<VaskeDag> vaskeDage){
		this.vaskeDage = vaskeDage;
		this.tavleID = tavleID;
		this.boligForeningID = boligForeningID;
		this.antalBlokkePerDag = antalBlokkePerDag;
		
	}


	protected VaskeTavle(Parcel in) {
		vaskeDage= new ArrayList<VaskeDag>();
		 in.readList(vaskeDage,null);
	}

	public static final Creator<VaskeTavle> CREATOR = new Creator<VaskeTavle>() {
		@Override
		public VaskeTavle createFromParcel(Parcel in) {
			return new VaskeTavle(in);
		}

		@Override
		public VaskeTavle[] newArray(int size) {
			return new VaskeTavle[size];
		}
	};

	public int getTavleID() {
		return tavleID;
	}

	public void setTavleID(int tavleID) {
		this.tavleID = tavleID;
	}

	public int getBoligForeningID() {
		return boligForeningID;
	}

	public void setBoligForeningID(int boligForeningID) {
		this.boligForeningID = boligForeningID;
	}

	
	
	public int getAntalBlokkePrDag() {
		// TODO Auto-generated method stub
		return antalBlokkePerDag;
	}

	public void setAntalBlokkePrDag(int nyAntalBlokke){
		this.antalBlokkePerDag = nyAntalBlokke;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(vaskeDage);
		dest.writeInt(tavleID);
		dest.writeInt(boligForeningID);
		dest.writeInt(antalBlokkePerDag);
	}
}

