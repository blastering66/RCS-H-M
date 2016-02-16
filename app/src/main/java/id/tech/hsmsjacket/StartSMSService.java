package id.tech.hsmsjacket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by RebelCreative-A1 on 06/01/2016.
 */
public class StartSMSService extends Service {
    Intent myIntent;
    PendingIntent alarmIntent;
    AlarmManager alarams;
    SharedPreferences sp;
    String cContactId;
    Intent activate;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myIntent = intent;
        Calendar c = Calendar.getInstance();
        activate = new Intent(StartSMSService.this, SmsListener.class);

        activate.putExtra("code", "1");

        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 111, activate,PendingIntent.FLAG_CANCEL_CURRENT);

        alarams = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarams.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                1000 * 60 * 30, alarmIntent);
        Log.e("Service Started", "");

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
