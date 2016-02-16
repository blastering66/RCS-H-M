package id.tech.hsmsjacket;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.transition.Slide;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.tech.orm_sugar.SLite;

/**
 * Created by RebelCreative-A1 on 06/01/2016.
 */
public class SmsListener extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = context.getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        final String msgBody = msgs[i].getMessageBody();
                        Log.e("Mandor SMS from = ", msg_from);
                        Log.e("Mandor SMS message = ", msgBody);
                        String cMandorId = sharedPreferences.getString(Parameter_Collections.SH_ID_MANDOR, "0");

                        if (msg_from.equals(Parameter_Collections.nomer_holcim) ||
                                msg_from.equals(Parameter_Collections.nomer_holcim_2)) {
//                            String patokan = msgBody.substring(0, 3);
//                            Log.e("patokan", patokan);
                            String patokanNews = msgBody.substring(msgBody.length()-3, msgBody.length());
                            Log.e("patokan News = ", patokanNews);

                            if (msgBody.contains("Klaim") || msgBody.contains("Claim")) {

                                if(msgBody.contains("menunggu")){
                                    //input ke DB
                                    int loc_trxid = msgBody.indexOf("TrxId: ");
                                    int loc_qty = msgBody.indexOf("sejumlah : ");
                                    int loc_qty_edge = msgBody.indexOf("Sak");

                                    int loc_retailer = msgBody.indexOf("(");
                                    int loc_retailer_edge = msgBody.indexOf(")");

                                    String trxId = msgBody.substring(loc_trxid + 6, loc_trxid + 12);
                                    String qty = msgBody.substring(loc_qty+ 10, loc_qty_edge);
                                    qty.replace(" ","");
                                    String retailerId = msgBody.substring(loc_retailer+1, loc_retailer_edge);

                                    //EDIT to UPPERCAST TrxId
                                    trxId = trxId.toUpperCase();

                                    Log.e("SMS MANDOR TrxId", trxId);
                                    Log.e("SMS MANDOR qty", qty);
                                    Log.e("SMS MANDOR RetailerID", retailerId);

                                    Log.e("MANDOR ALL = ", trxId + "#" + qty + "#" + retailerId);

                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("senderId", "");
                                    cv.put("bodyMessage", "");
                                    cv.put("confirmCode", "1");
                                    cv.put("masonId", cMandorId);
                                    cv.put("trxId", trxId);

                                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                    String date_now = df.format(Calendar.getInstance().getTime());

                                    cv.put("dateReceived", date_now);
                                    cv.put("qty", qty);
                                    cv.put("viewed", "0");
                                    cv.put("retailerId", retailerId);

                                    Log.e("SMS MANDOR masonId", cMandorId);
                                    Log.e("SMS MANDOR trxId", "");
                                    Log.e("SMS MANDOR qty", qty);
                                    Log.e("SMS MANDOR retailerId", retailerId);

                                    db.insert("tbl_sms", null, cv);
                                    db.close();
                                    Log.e("SMS MANDOR ", "Inputed to DB menunggu");
                                }else if(msgBody.contains("berhasil")){
                                    //datanya
                                    int loc_trxid_berhasil = msgBody.indexOf("TrxId: ");
                                    String trxId_berhasil = msgBody.substring(loc_trxid_berhasil + 6, loc_trxid_berhasil + 12);

                                    //EDIT to UPPERCAST TrxId
                                    trxId_berhasil = trxId_berhasil.toUpperCase();

                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    Log.e("SMS MANDOR berhasil", "trxId = " + trxId_berhasil);
                                    cv.put("confirmCode", "2");
//                                    db.update("tbl_sms", cv, "retailerId" + " ? ", new String[]{cRetailerId});
                                    db.update("tbl_sms", cv, "trxId = ?", new String[]{trxId_berhasil});
                                    Log.e("SMS MANDOR BERHASIL", "berhasil konfirmasi 1 Updated trxId = " + trxId_berhasil);
                                    db.close();

                                }

                            } else if(msgBody.contains("Konfirmasi")){

                                if(msgBody.contains("Berhasil")){
                                    int loc_trxid_berhasil = msgBody.indexOf("TrxId: ");
                                    String trxId_berhasil = msgBody.substring(loc_trxid_berhasil + 6, loc_trxid_berhasil + 12);

                                    //EDIT to UPPERCAST TrxId
                                    trxId_berhasil = trxId_berhasil.toUpperCase();

                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    Log.e("SMS MANDOR berhasil", "trxId = " + trxId_berhasil);
                                    cv.put("confirmCode", "2");
//                                    db.update("tbl_sms", cv, "retailerId" + " ? ", new String[]{cRetailerId});
                                    db.update("tbl_sms", cv, "trxId = ?", new String[]{trxId_berhasil});
                                    Log.e("SMS MANDOR BERHASIL", "Berhasil Konfimasi 2 Updated trxId = " + trxId_berhasil);
                                    db.close();
                                }

                            } else if(patokanNews.equals("ama")){
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());
                                cv.put("dateReceived", date_now);
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_news", null, cv);
                                db.close();
                                Log.e("Sms Holcim Claim", "SMS NEWS");

                            }else if(msgBody.contains("e-Cash")){
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());
                                cv.put("dateReceived", date_now);
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_insentive", null, cv);
                                db.close();
                                Log.e("Sms Holcim Claim", "SMS INSENTIVE");
                            }else{
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                cv.put("confirmCode", "1");
                                cv.put("masonId", "0");
                                cv.put("trxId", "0");

                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", "0");
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_inbox", null, cv);
                                db.close();
                                Log.e("Sms Holcim Claim", "SMS biasa");
                            }


                        }else if(msgBody.contains("(Holcim")){
                            //untuk nomer XL dan INDOSAT
                            String patokanNews = msgBody.substring(msgBody.length()-3, msgBody.length());
                            Log.e("patokan News = ", patokanNews);

                            if (msgBody.contains("Klaim") || msgBody.contains("Claim")) {

                                if(msgBody.contains("menunggu")){
                                    //input ke DB
                                    int loc_trxid = msgBody.indexOf("TrxId: ");
                                    int loc_qty = msgBody.indexOf("sejumlah : ");
                                    int loc_qty_edge = msgBody.indexOf("Sak");

                                    int loc_retailer = msgBody.indexOf("(");
                                    int loc_retailer_edge = msgBody.indexOf(")");

                                    String trxId = msgBody.substring(loc_trxid + 6, loc_trxid + 12);
                                    String qty = msgBody.substring(loc_qty+ 10, loc_qty_edge);
                                    qty.replace(" ","");
                                    String retailerId = msgBody.substring(loc_retailer+1, loc_retailer_edge);

                                    //Editan custom to uppercase
                                    trxId = trxId.toUpperCase();

                                    Log.e("SMS MANDOR TrxId", trxId);
                                    Log.e("SMS MANDOR qty", qty);
                                    Log.e("SMS MANDOR RetailerID", retailerId);

                                    Log.e("MANDOR ALL = ", trxId + "#" + qty + "#" + retailerId);

                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("senderId", "");
                                    cv.put("bodyMessage", "");
                                    cv.put("confirmCode", "1");
                                    cv.put("masonId", cMandorId);
                                    cv.put("trxId", trxId);

                                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                    String date_now = df.format(Calendar.getInstance().getTime());

                                    cv.put("dateReceived", date_now);
                                    cv.put("qty", qty);
                                    cv.put("viewed", "0");
                                    cv.put("retailerId", retailerId);

                                    Log.e("SMS MANDOR masonId", cMandorId);
                                    Log.e("SMS MANDOR trxId", "");
                                    Log.e("SMS MANDOR qty", qty);
                                    Log.e("SMS MANDOR retailerId", retailerId);

                                    db.insert("tbl_sms", null, cv);
                                    db.close();
                                    Log.e("SMS MANDOR ", "Inputed to DB menunggu");
                                }else if(msgBody.contains("berhasil")){
                                    //datanya
                                    int loc_trxid_berhasil = msgBody.indexOf("TrxId: ");
                                    String trxId_berhasil = msgBody.substring(loc_trxid_berhasil + 6, loc_trxid_berhasil + 12);

                                    //EDIT to UPPERCAST TrxId
                                    trxId_berhasil = trxId_berhasil.toUpperCase();

                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    Log.e("SMS MANDOR berhasil", "trxId = " + trxId_berhasil);
                                    cv.put("confirmCode", "2");
//                                    db.update("tbl_sms", cv, "retailerId" + " ? ", new String[]{cRetailerId});
                                    db.update("tbl_sms", cv, "trxId = ?", new String[]{trxId_berhasil});
                                    Log.e("SMS MANDOR BERHASIL", "berhasil konfirmasi 1 Updated trxId = " + trxId_berhasil);
                                    db.close();

                                }

                            }else if(msgBody.contains("Konfirmasi")) {

                                if (msgBody.contains("Berhasil")) {
                                    int loc_trxid_berhasil = msgBody.indexOf("TrxId: ");
                                    String trxId_berhasil = msgBody.substring(loc_trxid_berhasil + 6, loc_trxid_berhasil + 12);

                                    //EDIT to UPPERCAST TrxId
                                    trxId_berhasil = trxId_berhasil.toUpperCase();

                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    Log.e("SMS MANDOR berhasil", "trxId = " + trxId_berhasil);
                                    cv.put("confirmCode", "2");
//                                    db.update("tbl_sms", cv, "retailerId" + " ? ", new String[]{cRetailerId});
                                    db.update("tbl_sms", cv, "trxId = ?", new String[]{trxId_berhasil});
                                    Log.e("SMS MANDOR BERHASIL", "Konfirmasi 2 Updated trxId = " + trxId_berhasil);
                                    db.close();
                                }

                            }else if(patokanNews.equals("ama")){
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());
                                cv.put("dateReceived", date_now);
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_news", null, cv);
                                db.close();
                                Log.e("Sms Holcim Claim", "SMS NEWS");

                            }else if(msgBody.contains("e-Cash")){
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());
                                cv.put("dateReceived", date_now);
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_insentive", null, cv);
                                db.close();
                                Log.e("Sms Holcim Claim", "SMS INSENTIVE");
                            }else{
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                cv.put("confirmCode", "1");
                                cv.put("masonId", "0");
                                cv.put("trxId", "0");

                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", "0");
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_inbox", null, cv);
                                db.close();
                                Log.e("Sms Holcim Claim", "SMS biasa");
                            }


                        }
                    }



            }catch(Exception e){
//                    Log.d("Exception caught",e.getMessage());
            }
        }
    }
}
}
