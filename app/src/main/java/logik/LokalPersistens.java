package logik;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.Account;
import data.Bruger;

/**
 * Created by KimdR on 20-11-2017.
 */

public class LokalPersistens {

Context context;
    public LokalPersistens(Context context) {
        this.context = context;
    }

    public Object hentGemtFil(String filNavn) {
        String FILNAVN = context.getFilesDir() + "/" + filNavn + ".ser";
        Bruger bruger = null;
        try {
            FileInputStream fileOutputStream = new FileInputStream(FILNAVN);
            ObjectInputStream inputStream = new ObjectInputStream(fileOutputStream);
            if(filNavn.equals("bruger")){
                bruger = (Bruger) inputStream.readObject();
                return bruger;
            }
            else if( filNavn.equals("account")){
                Account account = (Account) inputStream.readObject();
                return account;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            return bruger;
        }

    }

    public Bruger hentGemtBruger(String filNavn) {
        String FILNAVN = context.getFilesDir() + "/" + filNavn + ".ser";
        Bruger bruger = null;
        try {
            FileInputStream fileOutputStream = new FileInputStream(FILNAVN);
            ObjectInputStream inputStream = new ObjectInputStream(fileOutputStream);
            bruger = (Bruger) inputStream.readObject();
            System.out.println(BookingApplication.vtCont.getReservations().size());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bruger;
    }
    public void gemData(final Object objects, String filNavn) {
        final String FILNAVN = context.getFilesDir() + "/" + filNavn + ".ser";
        System.out.println(FILNAVN);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(FILNAVN + " Slettes");
                File file = new File(FILNAVN);
                file.delete();


                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(FILNAVN);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(objects);
                    objectOutputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
