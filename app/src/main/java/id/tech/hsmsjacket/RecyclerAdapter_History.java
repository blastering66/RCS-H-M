package id.tech.hsmsjacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RecyclerAdapter_History extends RecyclerView.Adapter<RecyclerAdapter_History.ViewHolder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_History> data;
    private SharedPreferences sh;

    public RecyclerAdapter_History(Context context_adapter, List<RowData_History> data) {
        this.context_adapter = context_adapter;
        this.data = data;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        sh = context_adapter.getSharedPreferences("sh_sms", Context.MODE_PRIVATE);

        final RowData_History item = data.get(position);

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

        }else {
            holder.tv_tgl.setVisibility(View.VISIBLE);
            holder.tv_tgl.setText(" --- " + item.dateArrived + " --- ");
        }

        holder.tv_retailer_id.setText(item.retailerId);

        holder.tv_transaction_id.setText(item.tv_transaction_id);
        holder.tv_qty.setText(item.tv_qty);
        if(item.tv_status.equals("1")){
            holder.tv_status.setText("Menunggu");
            holder.tv_status.setTextColor(Color.BLUE);
        }else {
            holder.tv_status.setText("Berhasil");
            holder.tv_status.setTextColor(Color.GREEN);
        }
        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context_adapter, Dialog_Konfirmasi_Transaksi.class);
//                Intent intent = new Intent(context_adapter, Activity_History_Detail.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("tv_transaction_id", item.tv_transaction_id);
//                intent.putExtra("tv_mason_id",item.tv_mason_id);
//                intent.putExtra("tv_qty",item.tv_qty);
//                context_adapter.startActivity(intent);
            }
        });

        sh.edit().putString("tgl_now", date_current).commit();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_mandor,null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_tgl,tv_transaction_id,tv_retailer_id,tv_qty,tv_status;
        public View wrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_tgl = (TextView)itemView.findViewById(R.id.tv_tgl);

            tv_transaction_id = (TextView)itemView.findViewById(R.id.tv_transaction_id);
            tv_retailer_id = (TextView)itemView.findViewById(R.id.tv_retailer_id);
            tv_qty= (TextView)itemView.findViewById(R.id.tv_qty);
            tv_status = (TextView)itemView.findViewById(R.id.tv_status);
            wrapper= (View)itemView.findViewById(R.id.wrapper);
        }
    }
}
