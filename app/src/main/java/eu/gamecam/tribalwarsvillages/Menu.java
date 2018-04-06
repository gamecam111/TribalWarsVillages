package eu.gamecam.tribalwarsvillages;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

public class Menu extends AppCompatActivity {
    public static ArrayList<Village> actualSawBarbarian=new ArrayList<Village>();
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static boolean isEnableBackground=false;
    public static ArrayList<Village> newVillagesAddedToList = new ArrayList<Village>();
    public static int points;
    public static TextView textView;
    public static ImageView img;
    public static Drawable ok;
    public static Drawable fail;
    public static ArrayList<Logs> all_logs=new ArrayList<Logs>();
    Intent backgroundService;
    static Activity ac;
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ac=this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //textView=(TextView) findViewById(R.id.time);
        //img=(ImageView) findViewById(R.id.imageView);
        ok=getResources().getDrawable(R.drawable.ic_done_black_24dp);
        fail=getResources().getDrawable(R.drawable.ic_close_black_24dp);
        intent=getIntent();

        backgroundService=new Intent(getApplicationContext(),BackgroundService.class);

        //startAlert();

        /*PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, backgroundService, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);


        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 10, alarmIntent);*/


        //wl.release();

        database = FirebaseDatabase.getInstance();
        EditText pointsed=(EditText) findViewById(R.id.editText);
        points=Integer.parseInt(pointsed.getText().toString());

        myRef = database.getReference("skp3"+"_ActualBarbarianVillages_"+points);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Village>> t = new GenericTypeIndicator<ArrayList<Village>>() {};
                actualSawBarbarian=dataSnapshot.getValue(t);
                if (actualSawBarbarian==null) {
                actualSawBarbarian=new ArrayList<Village>();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        final Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMyServiceRunning(BackgroundService.class)) {
                    startService(backgroundService);
                }
                else {
                    stopService(backgroundService);
                }

                if (isMyServiceRunning(BackgroundService.class)) {
                    btn.setText("Vypnúť");
                }
                else {
                    btn.setText("Zapnúť");
                }
            }
        });


        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent (getApplicationContext(),VillageList.class));
            }
        });

        Button btn3 = (Button) findViewById(R.id.button3a);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent (getApplicationContext(),Loggss.class));
            }
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Do nothing
    }

    public static void updateInterface(String time, boolean result) {
    all_logs.add(new Logs(time,result));
    //Mazanie starších logov
    if (all_logs.size()>15) {
        for (int i=15; i<all_logs.size(); i++)
            all_logs.remove(0);
    }



    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
