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

    String response = "";
    String answer = "";


    public AsyncDelete(Callback cb, ProgressDialog pDiag, int resID) {
        this.cb = cb;
        this.pDiag = pDiag;
        this.resID = resID;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        answer = BookingApplication.cService.deleteReservation(resID);
        responseCode = Integer.parseInt(answer.split(",")[0]);
        response =answer.split(",")[1];
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (responseCode == 200) {
            cb.onEventCompleted(response);
        } else {
            cb.onEventFailed(response);
        }

    }
}
