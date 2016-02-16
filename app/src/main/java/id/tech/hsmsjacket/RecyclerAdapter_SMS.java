package id.tech.hsmsjacket;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import id.tech.orm_sugar.SLite;

public class RecyclerAdapter_SMS extends RecyclerView.Adapter<RecyclerAdapter_SMS.ViewHolder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_Sms> data;
    private SharedPreferences sh;

    public RecyclerAdapter_SMS(Context context_adapter, List<RowData_Sms> data) {
        this.context_adapter = context_adapter;
        this.data = data;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        sh = context_adapter.getSharedPreferences("sh_sms", Context.MODE_PRIVATE);

        final RowData_Sms item = data.get(position);

        String date_current = item.dateArrived;
        String date_sh = sh.getString("tgl_now", "-");

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String date_now = df.format(Calendar.getInstance().getTime());

        if(date_current.equals(date_sh) || date_current.equals("-")){
//            holder.tv_tgl.setVisibility(View.GONE);
            if(position == 0){
                holder.tv_tgl.setText(" --- Hari ini --- ");
            }else{
                holder.tv_tgl.setText("");
            }
        }else{
            holder.tv_tgl.setVisibility(View.VISIBLE);
            holder.tv_tgl.setText(" --- " + item.dateArrived + " --- ");
        }

        if(item.viewed.equals("0")){
            holder.tv_status.setText("Pesan Baru");
        }else{
            holder.tv_status.setText("");
        }

        holder.tv_sender.setText(item.sender);
        holder.tv_message.setText(item.message);

        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_status.setText("");

                Intent intent = new Intent(context_adapter, Dialog_Detail_Message.class);
                intent.putExtra("message",item.message);
                intent.putExtra("sender",item.sender);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context_adapter.startActivity(intent);

                SQLiteDatabase db = SLite.openDatabase(context_adapter);
                ContentValues cv = new ContentValues();
                cv.put("viewed", "1");
                db.update("tbl_sms_inbox", cv, "id = ?", new String[]{item._id});
                Log.e("Sms dr Holcim Confirm", "Updated inbox _id = " + item._id);
                db.close();
            }
        });
        sh.edit().putString("tgl_now", date_current).commit();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_inbox,null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_sender,tv_message,tv_tgl,tv_status;
        public View wrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_sender = (TextView)itemView.findViewById(R.id.tv_sender);
            tv_message = (TextView)itemView.findViewById(R.id.tv_message);
            tv_tgl= (TextView)itemView.findViewById(R.id.tv_tgl);
            tv_status = (TextView)itemView.findViewById(R.id.tv_status_sms);
            wrapper = (View)itemView.findViewById(R.id.wrapper);
        }
    }
}
