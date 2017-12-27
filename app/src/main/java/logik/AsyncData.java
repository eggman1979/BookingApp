package logik;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.SystemClock;

import java.io.IOException;

import data.BoligForening;

/**
 * Created by KimdR on 19-11-2017.
 */

public class AsyncData extends AsyncTask<Void, Void, Void> {


    Callback cb;
    ProgressDialog pDiag;

    public AsyncData(Callback cb, ProgressDialog pDiag) {
        this.cb = cb;
        this.pDiag = pDiag;

    }

    @Override
    protected Void doInBackground(Void... params) {

        System.out.println("Bruger = " + BookingApplication.bruger.getNavn());
        try {

            System.out.println("blokke er: "+ BookingApplication.vtCont.getvBlokke().size());
            System.out.println("tavler er: "+BookingApplication.vtCont.getVaskeTavler().size());


                BookingApplication.cService.hentReservationer(BookingApplication.boligForening.getId(), BookingApplication.vtCont.getSidstHentet());

            if(BookingApplication.vtCont.getVaskeTavler().size() == 0) {
                Thread.sleep(100);
                BookingApplication.cService.hentVaskeTavler();


            }
            if(BookingApplication.vtCont.getvBlokke().size() == 0) {
                Thread.sleep(100);
                BookingApplication.cService.hentVaskeBlokke();
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

        if (BookingApplication.vtCont.getVaskeTavler().size() > 0 && BookingApplication.vtCont.getvBlokke().size() > 0) {
            cb.onEventCompleted(null);
        } else {
            cb.onEventFailed(null);
        }
        if (pDiag != null) {
            pDiag.dismiss();
        }

    }
}
