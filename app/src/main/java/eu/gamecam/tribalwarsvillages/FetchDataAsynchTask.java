package eu.gamecam.tribalwarsvillages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import eu.gamecam.tribalwarsvillages.Config.FirebaseConfigManager;
import eu.gamecam.tribalwarsvillages.LoginAndRegister.Login;


/**
 * Created by Erik Juríček on 5.4.2018.
 */
public class FetchDataAsynchTask extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    private Activity actualActivity;
    FirebaseConfigManager fcm;

    public FetchDataAsynchTask(Activity activity, FirebaseConfigManager fcm) {
        dialog = new ProgressDialog(activity);
        actualActivity=activity;
        this.fcm=fcm;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Sťahujem data o verzii");
        dialog.show();
    }

    protected Void doInBackground(Void... args) {
        int ACTUAL_VERSION_CODE = 0;

        while (ACTUAL_VERSION_CODE == 0) {
            String ACTUAL_VERSION = fcm.getmFirebaseRemoteConfig().getString("actual_version");
            ACTUAL_VERSION_CODE = (int) (fcm.getmFirebaseRemoteConfig().getLong("actual_version_code"));
            String wc = fcm.getmFirebaseRemoteConfig().getString("welcome_text");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        actualActivity.startActivity(new Intent(actualActivity.getApplicationContext(),Login.class));
        actualActivity.finish();


    }
}