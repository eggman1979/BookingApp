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
    private String response;

    public AsyncReservation(Callback cb, Reservation reservation, ProgressDialog pDiag) {
        this.cb = cb;
        this.reservation = reservation;
        this.pDiag = pDiag;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        // Der skal være en returværdi der fortæller om det er gået godt eller ej;
        String answer  =BookingApplication.cService.reserverVasketid(reservation);

         responseCode = Integer.parseInt(answer.split(",")[0]);
        response = answer.split(",")[1];
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (responseCode == 200) {
            cb.onEventCompleted(response);
        } else {
            cb.onEventFailed(response);
        }
        pDiag.dismiss();
    }
}
