package id.tech.hsmsjacket;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog_Input_MandorID extends AppCompatActivity {
    private SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_mandor_id);

        spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

        final EditText ed_mandor_id = (EditText)findViewById(R.id.ed_mandor_id);

        Button btn_positive = (Button)findViewById(R.id.btn_positive);
        Button btn_negative = (Button)findViewById(R.id.btn_negative);

        btn_positive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Kirim sms untuk cek mason dgn formatnya
                String message = "checkmason#" + ed_mandor_id.getText().toString();
                boolean sms_sent = Public_Functions.sendSMS(message);
                Log.e("SMS SENT", message);

                //Cek jika berhasil dikirim sms
                if(sms_sent){
                    spf.edit().putBoolean(Parameter_Collections.SH_WAITING_VALIDATION, true).commit();
                    Toast.makeText(getApplicationContext(), "Kami akan validasi Mason Id anda...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Sms validasi Mason Id anda gagal, Coba lagi", Toast.LENGTH_LONG).show();
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        btn_negative.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //User Cancel input
                setResult(RESULT_CANCELED);
                spf.edit().clear().commit();
                finish();
            }
        });
    }
}
