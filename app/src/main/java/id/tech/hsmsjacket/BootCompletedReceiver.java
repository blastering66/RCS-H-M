package id.tech.hsmsjacket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by RebelCreative-A1 on 06/01/2016.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        try{
            Thread.sleep(30000);
        }catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Intent notif_service = new Intent(context, StartSMSService.class);
        notif_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notif_service.addFlags(Intent.FLAG_RECEIVER_NO_ABORT);
        //add FLAG_RECEIVER_FOREGROUND to force the intent in foreground
        notif_service.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startService(notif_service);

        Log.e("SERVICE >>>>", "SERVICE START");
    }
}
