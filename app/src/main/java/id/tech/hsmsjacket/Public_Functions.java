package id.tech.hsmsjacket;

import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by macbook on 2/4/16.
 */
public class Public_Functions {

    public static boolean sendSMS(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(Parameter_Collections.nomer_kirim,null,message,null, null);
            Log.e("SMS Sent", message);
        } catch (Exception e) {
            Log.e("Error Sending Sms", e.getMessage().toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
