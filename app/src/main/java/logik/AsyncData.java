package logik;

import android.app.ProgressDialog;
import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

import data.BoligForening;

/**
 * Created by KimdR on 19-11-2017.
 */

public class AsyncData extends AsyncTask<Void, Void, Void> {


    Callback cb;
    ProgressDialog pDiag;
    ConnectService cService;

    public AsyncData(Callback cb, ProgressDialog pDiag) {
        this.cb = cb;
        this.pDiag = pDiag;
cService = new ConnectService();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            cService.hentReservationer(BookingApplication.boligForening.getId(), BookingApplication.vtCont.getSidstHentet());

            if (BookingApplication.vtCont.getVaskeTavler().size() == 0) {
                Thread.sleep(100);
                cService.hentVaskeTavler();
            }
            if (BookingApplication.vtCont.getvBlokke().size() == 0) {
                Thread.sleep(100);
                cService.hentVaskeBlokke();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


        if (BookingApplication.vtCont.getVaskeTavler() != null && BookingApplication.vtCont.getvBlokke() != null) {
            if (BookingApplication.vtCont.getVaskeTavler().size() > 0 && BookingApplication.vtCont.getvBlokke().size() > 0) {
                cb.onEventCompleted(null);
            }
        } else {
            cb.onEventFailed(null);
        }
        if (pDiag != null) {
            pDiag.dismiss();
        }

    }
}
