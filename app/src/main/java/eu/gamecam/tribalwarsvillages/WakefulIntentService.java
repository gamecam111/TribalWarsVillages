package eu.gamecam.tribalwarsvillages;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Erik Juríček on 6.4.2018.
 */
public abstract class WakefulIntentService extends Service {

    public static final String LOCK_NAME_LOCAL  = "MyLockService";

    private PowerManager.WakeLock mWakeLock = null;

    @RequiresPermission(allOf = { Manifest.permission.WAKE_LOCK })
    public WakefulIntentService() {
        super();
    }

    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        final PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_LOCAL);
        mWakeLock.setReferenceCounted(true);

        Intent intent = new Intent(this, Menu.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,   PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "FOREGROUND");

        builder.setSmallIcon(R.drawable.ic_loop_black_24dp);
        Bitmap myLogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon);
        builder.setLargeIcon(myLogo);
        builder.setTicker("Tribal Wars Villages");
        builder.setContentText("Kliknutím otvoríte menu");
        builder.setContentTitle("Vyhladávanie je aktívne");
        builder.setContentIntent(pi);
        builder.setOngoing(true);
        builder.setOnlyAlertOnce(true);

        notification = builder.build();
        startForeground(2,notification);
    }

    @Override
    public void onStart(Intent intent, final int startId) {
        mWakeLock.acquire();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //releaseWakeLock();
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Wake lock
    //
    /////////////////////////////////////////////////////////////////

    public void releaseWakeLock() {
        if (this.mWakeLock != null) {
            try {
                mWakeLock.release();
            } catch (Throwable th) {
                // ignoring this exception, probably wakeLock was already released
            }
            this.mWakeLock = null;
        }
    }
}