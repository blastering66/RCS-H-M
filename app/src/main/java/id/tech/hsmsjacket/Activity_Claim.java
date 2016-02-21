package id.tech.hsmsjacket;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import id.tech.orm_sugar.SLite;

/**
 * Created by RebelCreative-A1 on 15/01/2016.
 */
public class Activity_Claim extends AppCompatActivity {
    private EditText ed_Qty;
    private AutoCompleteTextView ed_Retailer;
    private String mRetailer, mQty, mMandorId;
    private Button btn_Claim;
    private SharedPreferences spf;
    private ArrayAdapter<String> adapter;
    private List<String> data_retailercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_new);

        ImageView img_Back = (ImageView)findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar ac = getSupportActionBar();
        ac.hide();

        data_retailercode = new ArrayList<>();
        SQLiteDatabase db = SLite.openDatabase(this);
//        final Cursor c = db.rawQuery("SELECT * FROM tbl_sms",new String[] {} );
        final Cursor c = db.query("tbl_sms", new String[] {"confirmCode", "retailerId"},null, null, null, null,"dateReceived DESC", null);

        while (c.moveToNext()) {
            Log.e("Hasil Query", c.getString(0) + " & " + c.getString(1));
            if(c.getString(0).equals("2")){

                data_retailercode.add(c.getString(1));
                Log.e("Add Data", c.getString(1));

            }
        }

        c.close();
        db.close();

        List<String> newList = new ArrayList<>(new LinkedHashSet(data_retailercode));

        adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, newList);

        ed_Retailer = (AutoCompleteTextView)findViewById(R.id.ed_retailer_code);
        EditText ed_MandorId = (EditText)findViewById(R.id.ed_id_mandor);

        ed_Retailer.setAdapter(adapter);

        spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
        mMandorId = spf.getString(Parameter_Collections.SH_ID_MANDOR, "0");
        ed_MandorId.setHint("Mandor ID = " + mMandorId);

        ed_Qty = (EditText)findViewById(R.id.ed_qty);
        btn_Claim = (Button)findViewById(R.id.btn_claim);
        btn_Claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetailer = ed_Retailer.getText().toString();
                mQty = ed_Qty.getText().toString();

                if(mRetailer.equals("") || mRetailer.isEmpty()){
                    ed_Retailer.setError("Harap Isi Retailer Code");
                }else if(mQty.equals("") || mQty.isEmpty()){
                    if(mQty.matches(".*\\d.*")){
                        ed_Qty.setError("Kuantitas harus angka nominal");
                    }else {
                        ed_Qty.setError("Harap Kuantitas diisi");
                    }

                }else{
//                    SQLiteDatabase db = SLite.openDatabase(getApplicationContext());
//                    ContentValues cv = new ContentValues();
//                    cv.put("senderId", "");
//                    cv.put("bodyMessage", "");
//                    cv.put("confirmCode", "1");
//                    cv.put("masonId", mMandorId);
//                    cv.put("trxId", "");
//
//                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//                    String date_now = df.format(Calendar.getInstance().getTime());
//
//                    cv.put("dateReceived", date_now);
//                    cv.put("qty", ed_Qty.getText().toString());
//                    cv.put("viewed", "0");
//                    cv.put("retailerId", ed_Retailer.getText().toString());
//
//                    spf.edit().putString(Parameter_Collections.SH_ID_RETAILER, ed_Retailer.getText().toString()).commit();
//                    spf.edit().putString(Parameter_Collections.SH_QTY, ed_Qty.getText().toString()).commit();
//
//                    Log.e("masonId", mMandorId);
//                    Log.e("trxId", "");
//                    Log.e("qty", ed_Qty.getText().toString());
//                    Log.e("retailerId",  ed_Retailer.getText().toString());
//
//                    db.insert("tbl_sms", null, cv);
//                    db.close();
//                    Log.e("Sms Holcim Claim", "Inputed to DB");

                    new AsyncTask_Claim().execute();
                }

            }
        });

    }

    private class AsyncTask_Claim extends AsyncTask<Void,Void,Boolean>{
        private DialogFragmentProgress pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new DialogFragmentProgress();
            pDialog.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Thread.sleep(3000);
            }catch (Exception e){

            }
            String mesage = "SH#" +mRetailer +"#"+ mMandorId + "#" + mQty;
            return Public_Functions.sendSMS(mesage);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            pDialog.dismiss();
            if(b){
                Toast.makeText(getApplicationContext(), "Clam berhasil dikirim, Toko akan memvalidasi claim anda", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Claim Gagal, Coba lagi nanti", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }


}
