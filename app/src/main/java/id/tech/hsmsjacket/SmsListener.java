package id.tech.hsmsjacket;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
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

        // Listener incoming SMS
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    // Get SMS, bila multi sms maka, append SMS tiap2 sms
                    StringBuffer content_buff = new StringBuffer();
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String msgBody = msgs[i].getMessageBody();
                        content_buff.append(msgBody);
                    }

                    // Get Messagenya
                    String msgBody = content_buff.toString();
                    // Get Pengirimnya
                    msg_from = msgs[0].getOriginatingAddress();

                    //cek mandor id dr lokal bila tdk ada balikannya 0
                    String cMandorId = sharedPreferences.getString(Parameter_Collections.SH_ID_MANDOR, "0");

                    //Cek apakah SMS dari Holcim
                    if (msg_from.equals(Parameter_Collections.nomer_holcim) ||
                            msg_from.equals(Parameter_Collections.nomer_holcim_2)) {

                        //set Patokannya bila dari News
                        String patokanNews = msgBody.substring(msgBody.length() - 3, msgBody.length());
                        Log.e("patokan News = ", patokanNews);

                        //Cek apakah SMS proses CLAIM
                        if (msgBody.contains("Klaim") || msgBody.contains("Claim")) {

                            if (msgBody.contains("menunggu")) {
                                //input ke DB
                                int loc_trxid = msgBody.indexOf("TrxId: ");
                                int loc_qty = msgBody.indexOf("sejumlah : ");
                                int loc_qty_edge = msgBody.indexOf("Sak");

                                int loc_retailer = msgBody.indexOf("(");
                                int loc_retailer_edge = msgBody.indexOf(")");

                                String trxId = msgBody.substring(loc_trxid + 6, loc_trxid + 12);
                                String qty = msgBody.substring(loc_qty + 10, loc_qty_edge);
                                qty.replace(" ", "");
                                String retailerId = msgBody.substring(loc_retailer + 1, loc_retailer_edge);

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

                                //Format Tanggal yg disimpan di DB
                                SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", qty);
                                cv.put("viewed", "0");
                                cv.put("retailerId", retailerId);

//                                Log.e("SMS MANDOR masonId", cMandorId);
//                                Log.e("SMS MANDOR trxId", "");
//                                Log.e("SMS MANDOR qty", qty);
//                                Log.e("SMS MANDOR retailerId", retailerId);

                                db.insert("tbl_sms", null, cv);
                                db.close();
                                Log.e("SMS MANDOR ", "Inputed to DB menunggu");
                            } else if (msgBody.contains("berhasil")) {
                                //datanya jika berhasil
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

                        } else if (msgBody.contains("MasonId")) {
                            //Ini SMS Validasi pendaftaran Mason
                            Log.e("SMS validasi", "Mason");

                            if (msgBody.contains("tidak")) {
                                //tidak berhasil divalidasi
                                Log.e("Mason Id = ", "Tidak Valid");
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_WAITING_VALIDATION, false).commit();
                            } else {
                                //Berhasil di validasi
                                int loc_MasonId = msgBody.indexOf("MasonId ");
                                String mason_id = msgBody.substring(loc_MasonId + 8, loc_MasonId + 17);
                                Log.e("Mason Id = ", mason_id);

                                //Simpan di lokal
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_WAITING_VALIDATION, false).commit();

                                sharedPreferences.edit().putString(Parameter_Collections.SH_ID_MANDOR, mason_id).commit();
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_MANDOR_INPUTED, true).commit();
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_lOGGED, true).commit();

                                //Pindah Otomatis ke Activity Home
                                Intent load_MenuUtama = new Intent(context, Activity_Home.class);
                                load_MenuUtama.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(load_MenuUtama);

                            }

                        } else if (msgBody.contains("Konfirmasi")) {

                            //INi SMS Konfirmasi claim
                            if (msgBody.contains("Berhasil")) {
                                //Jika berhasil update ke db
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

                        } else if (patokanNews.equals("ama")) {
                            //Ini SMS News

                            SQLiteDatabase db = SLite.openDatabase(context);
                            ContentValues cv = new ContentValues();
                            cv.put("senderId", msg_from);
                            cv.put("bodyMessage", msgBody);
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                            String date_now = df.format(Calendar.getInstance().getTime());
                            cv.put("dateReceived", date_now);
                            cv.put("viewed", "0");
                            db.insert("tbl_sms_news", null, cv);
                            db.close();
                            Log.e("Sms Holcim Claim", "SMS NEWS");

                        } else if (msgBody.contains("e-Cash")) {
                            //Jika ada e-Cash maka ini SMS Insentive

                            SQLiteDatabase db = SLite.openDatabase(context);
                            ContentValues cv = new ContentValues();
                            cv.put("senderId", msg_from);
                            cv.put("bodyMessage", msgBody);
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                            String date_now = df.format(Calendar.getInstance().getTime());
                            cv.put("dateReceived", date_now);
                            cv.put("viewed", "0");
                            db.insert("tbl_sms_insentive", null, cv);
                            db.close();
                            Log.e("Sms Holcim Claim", "SMS INSENTIVE");
                        } else {
                            //Ini sms biasa masuk ke inbox

                            SQLiteDatabase db = SLite.openDatabase(context);
                            ContentValues cv = new ContentValues();
                            cv.put("senderId", msg_from);
                            cv.put("bodyMessage", msgBody);
                            cv.put("confirmCode", "1");
                            cv.put("masonId", "0");
                            cv.put("trxId", "0");

                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                            String date_now = df.format(Calendar.getInstance().getTime());

                            cv.put("dateReceived", date_now);
                            cv.put("qty", "0");
                            cv.put("viewed", "0");
                            db.insert("tbl_sms_inbox", null, cv);
                            db.close();
                            Log.e("Sms Holcim Claim", "SMS biasa");
                        }


                        //Jika SMS di body ada (Holcim) maka dari longnumber
                        //Methodnya saa spt diatas
                    } else if (msgBody.contains("(Holcim")) {
                        //untuk nomer XL dan INDOSAT
                        String patokanNews = msgBody.substring(msgBody.length() - 3, msgBody.length());
                        Log.e("patokan News = ", patokanNews);

                        if (msgBody.contains("Klaim") || msgBody.contains("Claim")) {

                            if (msgBody.contains("menunggu")) {
                                //input ke DB
                                int loc_trxid = msgBody.indexOf("TrxId: ");
                                int loc_qty = msgBody.indexOf("sejumlah : ");
                                int loc_qty_edge = msgBody.indexOf("Sak");

                                int loc_retailer = msgBody.indexOf("(");
                                int loc_retailer_edge = msgBody.indexOf(")");

                                String trxId = msgBody.substring(loc_trxid + 6, loc_trxid + 12);
                                String qty = msgBody.substring(loc_qty + 10, loc_qty_edge);
                                qty.replace(" ", "");
                                String retailerId = msgBody.substring(loc_retailer + 1, loc_retailer_edge);

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

                                SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
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
                            } else if (msgBody.contains("berhasil")) {
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

                        } else if (msgBody.contains("Konfirmasi")) {

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

                        } else if (msgBody.contains("MasonId")) {
                            Log.e("SMS validasi", "Mason");

                            if (msgBody.contains("tidak")) {
                                //tidak berhasil
                                Log.e("Mason Id = ", "Tidak Valid");
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_WAITING_VALIDATION, false).commit();
                            } else {
                                int loc_MasonId = msgBody.indexOf("MasonId ");
                                String mason_id = msgBody.substring(loc_MasonId + 8, loc_MasonId + 17);
                                Log.e("Mason Id = ", mason_id);
                                sharedPreferences.edit().putString(Parameter_Collections.SH_ID_MANDOR, mason_id).commit();
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_MANDOR_INPUTED, true).commit();
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_lOGGED, true).commit();
                                sharedPreferences.edit().putBoolean(Parameter_Collections.SH_WAITING_VALIDATION, false).commit();

                                Intent load_MenuUtama = new Intent(context, Activity_Home.class);
                                context.startActivity(load_MenuUtama);

                            }

                        } else if (patokanNews.equals("ama")) {
                            SQLiteDatabase db = SLite.openDatabase(context);
                            ContentValues cv = new ContentValues();
                            cv.put("senderId", msg_from);
                            cv.put("bodyMessage", msgBody);
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                            String date_now = df.format(Calendar.getInstance().getTime());
                            cv.put("dateReceived", date_now);
                            cv.put("viewed", "0");
                            db.insert("tbl_sms_news", null, cv);
                            db.close();
                            Log.e("Sms Holcim Claim", "SMS NEWS");

                        } else if (msgBody.contains("e-Cash")) {
                            SQLiteDatabase db = SLite.openDatabase(context);
                            ContentValues cv = new ContentValues();
                            cv.put("senderId", msg_from);
                            cv.put("bodyMessage", msgBody);
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                            String date_now = df.format(Calendar.getInstance().getTime());
                            cv.put("dateReceived", date_now);
                            cv.put("viewed", "0");
                            db.insert("tbl_sms_insentive", null, cv);
                            db.close();
                            Log.e("Sms Holcim Claim", "SMS INSENTIVE");
                        } else {
                            SQLiteDatabase db = SLite.openDatabase(context);
                            ContentValues cv = new ContentValues();
                            cv.put("senderId", msg_from);
                            cv.put("bodyMessage", msgBody);
                            cv.put("confirmCode", "1");
                            cv.put("masonId", "0");
                            cv.put("trxId", "0");

                            SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa dd MMM yyyy");
                            String date_now = df.format(Calendar.getInstance().getTime());

                            cv.put("dateReceived", date_now);
                            cv.put("qty", "0");
                            cv.put("viewed", "0");
                            db.insert("tbl_sms_inbox", null, cv);
                            db.close();
                            Log.e("Sms Holcim Claim", "SMS biasa");
                        }


                    }


                } catch (Exception e) {
//                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
