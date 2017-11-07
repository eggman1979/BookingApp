package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Date;


/**
 * VaskeTid klassen er en klasse der skal fungere som et bindeled imellem Vasketavlen og en reservation.
 * VaskeTavlen skal fyldes op med vasketider, med eller uden reservationer.
 * @author KimdR
 *
 */
public class VaskeTid implements Serializable, Parcelable {


	private static final long serialVersionUID = -8087778595446423144L;


	private long dato;
	private int tavle_id;
	private VaskeBlok vaskeBlok;
	private Reservation reservation = null;

	protected VaskeTid(Parcel in) {

		vaskeBlok = in.readParcelable(VaskeBlok.class.getClassLoader());
		reservation = in.readParcelable(Reservation.class.getClassLoader());
		dato = in.readLong();
		tavle_id = in.readInt();

	}

	public static final Creator<VaskeTid> CREATOR = new Creator<VaskeTid>() {
		@Override
		public VaskeTid createFromParcel(Parcel in) {
			return new VaskeTid(in);
		}

		@Override
		public VaskeTid[] newArray(int size) {
			return new VaskeTid[size];
		}
	};

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}




	public VaskeBlok getVaskeBlok() {
		return vaskeBlok;
	}

	public void setVaskeBlok(VaskeBlok vaskeBlok) {
		this.vaskeBlok = vaskeBlok;
	}


	
	public VaskeTid(){
		
	}
	public VaskeTid(long dato, int tavleId, VaskeBlok vaskeBlok){
		this.dato = dato;
		this.tavle_id = tavleId;
		this.vaskeBlok = vaskeBlok;
 
	}

	public long getDato() {
		return dato;
	}


	public void setDato(long dato) {
		this.dato = dato;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(vaskeBlok, flags);
		dest.writeParcelable(reservation,flags);
		dest.writeInt(tavle_id);
		dest.writeLong(dato);

	}
}
