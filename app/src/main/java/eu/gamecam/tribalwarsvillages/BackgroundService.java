package eu.gamecam.tribalwarsvillages;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Erik Juríček on 5.4.2018.
 */

public class BackgroundService extends WakefulIntentService {

    private ArrayList<Village> villages=null;
    private boolean isNotificationEnable=true;
    private boolean result;

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    Thread thread;
    private volatile boolean running = true;

    private Notification notification;
    private MediaPlayer player;

    public BackgroundService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        /*player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        player.setLooping(true);
        player.start();*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Kontrolovanie dedín bolo vypnuté", Toast.LENGTH_LONG).show();
        running=false;
        //player.stop();
        //stopForeground();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        super.onStart(intent,startid);
        Toast.makeText(this, "Kontrolovanie dedín bolo zapnuté", Toast.LENGTH_LONG).show();
        //startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Test");

        runnable = new Runnable() {
            public void run() {
                while(true) {
                    if (!running) return;
                    if (updateVillages()) {
                        result = true;
                        if (isNewVillage()) {
                            if (isNotificationEnable) doNotificate();
                        }
                        System.out.println("Prebehlo overovanie");
                    } else {
                        result=false;
                        System.out.println("Není internetové pripojenie");
                    }
                    updateUI();

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //handler.postDelayed(runnable, 60000);

        thread= new Thread(runnable);
        thread.start();
        return START_STICKY;
    }



    //MyCode
    public void updateUI() {
        String time= new SimpleDateFormat("HH:mm:ss").format(new Date());
        Menu.updateInterface(time,result);
    }
    public void doNotificate() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "Channel")
                        .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                        .setContentTitle("Nová vesnica")
                        .setContentText("Objavila sa nová vesnica")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        ;

        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationChannel channel = new NotificationChannel("Channel", "Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Divoke kmene");
            // Register the channel with the system
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);
        }


        Intent resultIntent = new Intent(context, VillageList.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Loggss.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public boolean isNewVillage () {
        boolean upozornenie=false;
        for (int i=0; i<villages.size(); i++) {
            Village tato = villages.get(i);
            if (tato.getPlayerID()==0) {
                if(tato.getPoints()>3000) {
                    boolean pridaj=true;
                    for (int j = 0; j< Menu.actualSawBarbarian.size(); j++) {
                        if (Menu.actualSawBarbarian.get(j).getId()==tato.getId()) {
                            pridaj=false;
                        }
                    }
                    if (pridaj) {
                        Menu.actualSawBarbarian.add(tato);
                        Menu.newVillagesAddedToList.add(tato);
                        upozornenie=true;
                    }
                }
            }
        }

        Menu.myRef.setValue(Menu.actualSawBarbarian);
        return upozornenie;
    }

    public boolean updateVillages() {
        villages=getVillages("skp3");
        if (villages.size()==0) return false;
        else return true;
    }

    public static ArrayList<Village> getVillages(String world) {
        ArrayList<Village> vilret=new ArrayList<Village>();

        try {

            URL url = new URL("https://"+world+".divoke-kmene.sk/map/village.txt");
            //First open the connection

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();



            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = responseBuffer.readLine()) != null) {
                String [] pole;
                pole=output.split(",");
                Village veska=new Village();
                veska.setId(Integer.parseInt(pole[0]));
                veska.setX(Integer.parseInt(pole[2]));
                veska.setY(Integer.parseInt(pole[3]));
                veska.setPlayerID(Integer.parseInt(pole[4]));
                veska.setName(pole[1]);
                veska.setPoints(Integer.parseInt(pole[5]));
                vilret.add(veska);
            }

        } catch (ProtocolException e1) {

        } catch (MalformedURLException e1) {

        } catch (IOException e1) {

        }

        return vilret;
    }

}
