package logik;

import android.os.AsyncTask;


import java.io.IOException;

/**
 * Created by KimdR on 19-11-2017.
 */


public class AsyncLogin extends AsyncTask<Void, Void, Void> {


    Callback cb;
    final String password;
    final String user;

    public AsyncLogin(Callback cb, String user, String password) {
        this.cb = cb;
        this.password = password;
        this.user = user;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
        BookingApplication.cService.login(user, password, "1");
            Thread.sleep(100);
            BookingApplication.cService.hentBoligforening(BookingApplication.bruger.getBoligForeningID());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(BookingApplication.bruger != null ){
            cb.onEventCompleted(null);
        }
        else{
            cb.onEventFailed(null);
        }
    }
}
