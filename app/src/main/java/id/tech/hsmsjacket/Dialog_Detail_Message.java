package id.tech.hsmsjacket;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dialog_Detail_Message extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detail_sms);

        String cSender = getIntent().getStringExtra("sender");
        String cMessage = getIntent().getStringExtra("message");

        TextView tv_Sender = (TextView)findViewById(R.id.tv_sender);
        TextView tv_Message = (TextView)findViewById(R.id.tv_message);

        tv_Sender.setText(cSender);
        tv_Message.setText(cMessage);

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
