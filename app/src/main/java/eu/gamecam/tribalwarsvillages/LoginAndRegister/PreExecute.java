package eu.gamecam.tribalwarsvillages.LoginAndRegister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eu.gamecam.tribalwarsvillages.Config.FirebaseConfigManager;
import eu.gamecam.tribalwarsvillages.FetchDataAsynchTask;
import eu.gamecam.tribalwarsvillages.R;

public class PreExecute extends AppCompatActivity {

    FirebaseConfigManager fcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_execute);
        fcm = new FirebaseConfigManager();
        fcm.getRemoteConfig(this);

        FetchDataAsynchTask fts = new FetchDataAsynchTask(this,fcm);
        fts.execute();
    }
}
