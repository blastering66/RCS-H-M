package id.tech.hsmsjacket;

/**
 * Created by RebelCreative-A1 on 15/01/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import id.tech.orm_sugar.SLite;

public class RecyclerAdapter_Home extends RecyclerView.Adapter<RecyclerAdapter_Home.ViewHolder>{
    private Activity activity_adapter;
    private Context context_adapter;
    private SharedPreferences spf;

    public RecyclerAdapter_Home(Activity activity_adapter, Context context_adapter) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SQLiteDatabase db = SLite.openDatabase(context_adapter);

        spf = context_adapter.getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

        //Query SQL lokal untuk dapatnya Count yg belum dibaca
        final Cursor c = db.query("tbl_sms_inbox", new String[] {"viewed"}
                ,null, null, null, null,null, null);
        int total_inbox_unread = 0;

        while(c.moveToNext()){
            if(c.getString(0).equals("0")){
                total_inbox_unread++;
            }

        }

        final Cursor c2 = db.query("tbl_sms_news", new String[] {"viewed"}
                ,null, null, null, null,null, null);
        int total_news_unread = 0;

        while(c2.moveToNext()){
            if(c2.getString(0).equals("0")){
                total_news_unread++;
            }

        }

        final Cursor c3 = db.query("tbl_sms_insentive", new String[] {"viewed"}
                ,null, null, null, null,null, null);
        int total_insentive_unread = 0;

        while(c3.moveToNext()){
            if(c3.getString(0).equals("0")){
                total_insentive_unread++;
            }

        }

        // tampilkan Count SMS yg blum dibaca di menu tertentu saja
        switch (position){
            case 0:
                holder.tv_total_unread_sms.setText(String.valueOf(total_inbox_unread));
                break;
            case 1:
                holder.tv_total_unread_sms.setVisibility(View.GONE);
                break;
            case 2:
                holder.tv_total_unread_sms.setVisibility(View.GONE);
                break;
            case 3:
                holder.tv_total_unread_sms.setText(String.valueOf(total_insentive_unread));
                break;
            case 4:
                holder.tv_total_unread_sms.setText(String.valueOf(total_news_unread));
                break;
            case 5:
                holder.tv_total_unread_sms.setVisibility(View.GONE);
                break;
        }


        // Set Image Menu dan Klik Activity dari menu masing2
        switch (position){
            case 0:
                holder.img.setImageResource(R.drawable.img_item_inbox);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_Inbox.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 1:
                holder.img.setImageResource(R.drawable.img_item_claim);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_Claim.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 2:
                holder.img.setImageResource(R.drawable.img_item_history);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_History_Tabpager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 3:
                holder.img.setImageResource(R.drawable.img_item_insentif);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_Insentif.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 4:
                holder.img.setImageResource(R.drawable.img_item_news);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_News.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 5:
                holder.img.setImageResource(R.drawable.img_item_keluar);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spf.edit().clear().commit();
                        Toast.makeText(context_adapter, "Anda telah keluar dr aplikasi ini, Data Mandor ID dihapus",
                                Toast.LENGTH_LONG).show();
                        activity_adapter.finish();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView tv_total_unread_sms;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img);
            tv_total_unread_sms = (TextView)itemView.findViewById(R.id.tv_total_unread_sms);
        }
    }
}
