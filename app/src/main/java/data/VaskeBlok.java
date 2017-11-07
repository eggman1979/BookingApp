package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class VaskeBlok implements Serializable, Parcelable{

	private static final long serialVersionUID = -8187778595446423144L;
	private int boligselskabID;
	private int blokID;
	private int startTid;


	public VaskeBlok(int boligselskabID, int blokID, int startTid){
		this.boligselskabID = boligselskabID;
		this.blokID = blokID;
		this.startTid = startTid;
	}


	protected VaskeBlok(Parcel in) {
		boligselskabID = in.readInt();
		blokID = in.readInt();
		startTid = in.readInt();
	}

	public static final Creator<VaskeBlok> CREATOR = new Creator<VaskeBlok>() {
		@Override
		public VaskeBlok createFromParcel(Parcel in) {
			return new VaskeBlok(in);
		}

		@Override
		public VaskeBlok[] newArray(int size) {
			return new VaskeBlok[size];
		}
	};

	public int getBoligselskabID() {
		return boligselskabID;
	}


	public void setBoligselskabID(int boligselskabID) {
		this.boligselskabID = boligselskabID;
	}


	public int getBlokID() {
		return blokID;
	}
	public void setBlokID(int blokID) {
		this.blokID = blokID;
	}
	public int getStartTid() {
		return startTid;
	}
	public void setStartTid(int startTid) {
		this.startTid = startTid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(boligselskabID);
		dest.writeInt(blokID);
		dest.writeInt(startTid);
	}
}
