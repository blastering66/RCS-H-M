package id.tech.hsmsjacket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by RebelCreative-A1 on 15/01/2016.
 */
public class Activity_Register extends AppCompatActivity {
    private EditText ed_Nama, ed_Retailer, ed_Kota;
    private Button btn_Send;
    private String mNama, mRetailer, mkota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView img_Back = (ImageView)findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar ac = getSupportActionBar();
        ac.hide();

        ed_Nama = (EditText)findViewById(R.id.ed_nama);
        ed_Retailer = (EditText)findViewById(R.id.ed_retailer_code);
        ed_Kota = (EditText)findViewById(R.id.ed_kota);
        btn_Send = (Button)findViewById(R.id.btn_register);

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNama = ed_Nama.getText().toString();
                mkota = ed_Kota.getText().toString();
                mRetailer = ed_Retailer.getText().toString();

                //validasi

                if(mkota.equals("") || mkota.isEmpty() ){
                    ed_Kota.setError("Masukan Nama Kota");
                }else if(mNama.equals("") ||mNama.isEmpty()){

                    if(mNama.matches(".*\\d.*") || !mNama.matches("[a-zA-Z]")){
                        ed_Nama.setError("Nama anda harus alphabet saja");
                    }else{
                        ed_Nama.setError("Masukana Nama Anda");
                    }

                }else if( mRetailer.equals("")|| mRetailer.isEmpty()){
                    ed_Retailer.setError("Masukan Kode Retailer yg Valid");
                }else{
                    new AsyncTask_Register().execute();
                }

            }
        });
    }

    private class AsyncTask_Register extends AsyncTask<Void,Void,Void>{
        private DialogFragmentProgress pDialog;
        private boolean sms_sent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new DialogFragmentProgress();
            pDialog.show(getSupportFragmentManager(),"");

        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Thread.sleep(3000);
            }catch (Exception e){

            }

            //format daftar mason ke retailer
            String message = "DAFTAR#" + mRetailer + "#" + mNama + "#" + mkota;
            sms_sent = Public_Functions.sendSMS(message);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            if(sms_sent){
                Toast.makeText(getApplicationContext(),"Registrasi berhasil, Tunggu SMS konfirmasi dari kami", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Sms Registrasi gagal, Coba lagi", Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }

}
