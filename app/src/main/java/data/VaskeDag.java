package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VaskeDag implements Serializable, Parcelable {

    private static final long serialVersionUID = -1241553300038566499L;
    List<VaskeTid> vasketider;
    int antalBlokke;


    public VaskeDag(int antalBlokke, List<VaskeTid> vasketider) {
        this.antalBlokke = antalBlokke;
        this.vasketider = vasketider;
    }


    protected VaskeDag(Parcel in) {
        vasketider = new ArrayList<VaskeTid>();
        in.readList(vasketider, null);
        antalBlokke = in.readInt();

    }


    public static final Creator<VaskeDag> CREATOR = new Creator<VaskeDag>() {
        @Override
        public VaskeDag createFromParcel(Parcel in) {
            return new VaskeDag(in);
        }

        @Override
        public VaskeDag[] newArray(int size) {
            return new VaskeDag[size];
        }
    };

    public boolean isVaskeDagLedig() {
// TODO
        return false;
    }


    public int getAntalBlokke() {
        return antalBlokke;
    }


    public void setAntalBlokke(int antalBlokke) {
        this.antalBlokke = antalBlokke;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(vasketider);
        dest.writeInt(antalBlokke);

    }

    public List<VaskeTid> getVasketider() {
        return vasketider;
    }

    public void setVasketider(List<VaskeTid> vasketider) {
        this.vasketider = vasketider;
    }


}
