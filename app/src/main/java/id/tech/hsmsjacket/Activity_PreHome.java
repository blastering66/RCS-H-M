package id.tech.hsmsjacket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Activity_PreHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prehome);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        Button btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_Register.class));

            }
        });

        Button btn_input = (Button)findViewById(R.id.btn_input);
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidating = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE)
                        .getBoolean(Parameter_Collections.SH_WAITING_VALIDATION, false);
                if(isValidating){
                    Toast.makeText(getApplicationContext(), "Masih Menunggu SMS Validasi dari sistem", Toast.LENGTH_LONG).show();
                }else{
                    startActivityForResult(new Intent(getApplicationContext(), Dialog_Input_MandorID.class),111);
                }

//                startActivityForResult(new Intent(getApplicationContext(), Dialog_Input_MandorID.class),111);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
//                startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                finish();
                break;
            case RESULT_CANCELED:

                break;
        }
    }
}
