package logik;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import data.Reservation;

/**
 * Created by KimdR on 22-11-2017.
 */

public class AsyncReservation extends AsyncTask {
    Callback cb;
    Reservation reservation;
    ProgressDialog pDiag;
    private int responseCode;

    public AsyncReservation(Callback cb, Reservation reservation, ProgressDialog pDiag) {
        this.cb = cb;
        this.reservation = reservation;
        this.pDiag = pDiag;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        // Der skal være en returværdig der fortæller om det er gået godt eller ej;
        responseCode = BookingApplication.cService.reserverVasketid(reservation);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (responseCode == 200) {
            cb.onEventCompleted();
        } else {
            cb.onEventFailed();
        }
        pDiag.dismiss();
    }
}
