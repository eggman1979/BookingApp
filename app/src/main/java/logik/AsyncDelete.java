package logik;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by KimdR on 14-12-2017.
 */


public class AsyncDelete extends AsyncTask {

    Callback cb;
    ProgressDialog pDiag;
    int resID;

    int responseCode = -1;

    public AsyncDelete(Callback cb, ProgressDialog pDiag, int resID) {
        this.cb = cb;
        this.pDiag = pDiag;
        this.resID = resID;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        responseCode = BookingApplication.cService.deleteReservation(resID);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (responseCode == 200) {
            cb.onEventCompleted();
        } else {
            cb.onEventFailed();
        }

    }
}
