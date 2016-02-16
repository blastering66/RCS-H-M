package id.tech.hsmsjacket;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by macbook on 2/7/16.
 */
public class Activity_History_Parent extends AppCompatActivity {
    private Button btn_Pending, btn_Confirmed;
    private boolean btn_Pending_isClicked, btn_Confirmed_isClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_new);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        ImageView img_Back = (ImageView)findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Pending = (Button)findViewById(R.id.btn_pending);
        btn_Confirmed = (Button)findViewById(R.id.btn_confirmed);

        btn_Pending_isClicked = true;
        btn_Confirmed_isClicked = false;

        btn_Confirmed.setBackgroundColor(Color.DKGRAY);


        if(savedInstanceState == null){
            Fragment fragment_Pending = new Activity_History_Fragment_Pending();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_Pending).commit();
        }

        btn_Pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btn_Pending_isClicked){
                    btn_Pending.setBackgroundColor(Color.DKGRAY);
                    btn_Confirmed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_Pending_isClicked = false;
                    btn_Confirmed_isClicked = true;
                }else{
                    btn_Pending.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_Confirmed.setBackgroundColor(Color.DKGRAY);
                    btn_Pending_isClicked = true;
                    btn_Confirmed_isClicked = false;
                }

                Fragment fragment_Pending = new Activity_History_Fragment_Pending();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_Pending).commit();
            }
        });
        btn_Confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_Confirmed_isClicked){
                    btn_Confirmed.setBackgroundColor(Color.DKGRAY);
                    btn_Pending.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_Confirmed_isClicked = true;
                    btn_Pending_isClicked = false;
                }else{
                    btn_Confirmed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_Pending.setBackgroundColor(Color.DKGRAY);
                    btn_Confirmed_isClicked = false;
                    btn_Pending_isClicked = true;
                }
                Fragment fragment_Confirmed = new Activity_History_Fragment_Confirmed();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_Confirmed).commit();
            }
        });
    }
}
