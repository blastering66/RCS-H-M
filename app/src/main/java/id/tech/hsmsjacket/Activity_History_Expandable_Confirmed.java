package id.tech.hsmsjacket;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.tech.orm_sugar.SLite;

/**
 * Created by macbook on 2/15/16.
 */
public class Activity_History_Expandable_Confirmed extends Fragment {
    ExpandableListAdapter adapter;
    List<String> dataHeader;
    HashMap<String, List<RowData_History>> dataChild;
    SharedPreferences sh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_history_expandablelistview, null);
        ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.expandablelistview);

        dataHeader = new ArrayList<>();
        dataChild = new HashMap<String, List<RowData_History>>();

        //penambahan Title Header
        dataHeader.add("Hari ini");
        dataHeader.add("Kemarin");
        dataHeader.add("Lainnya");

        List<RowData_History> data_Today = new ArrayList<>();
        List<RowData_History> data_yesterday = new ArrayList<>();
        List<RowData_History> data_else = new ArrayList<>();

        //Query dtabase lokal
        sh = getActivity().getSharedPreferences("sh_sms", Context.MODE_PRIVATE);
        SQLiteDatabase db = SLite.openDatabase(getActivity());
//        final Cursor c = db.rawQuery("SELECT * FROM tbl_sms",new String[] {} );
        final Cursor c = db.query("tbl_sms", new String[] {"trxId", "masonId", "qty", "confirmCode","dateReceived",
                "retailerId"},null, null, null, null,"dateReceived DESC", null);

        while (c.moveToNext()) {
//            jika pending maka masukan ke data
            String date_current = c.getString(4);
            String date_sh = sh.getString("tgl_now", "-");

            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
            String date_now = df.format(Calendar.getInstance().getTime());

            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

            Calendar ctoday = Calendar.getInstance();

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date(date_current)); // your date

            boolean isYesterday = false;
            boolean isToday = false;

            //cek apakah hari ini, kmrin, atau sebelum2nya
            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                isYesterday = true;
            }else if(ctoday.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && ctoday.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                isToday = true;
            }
            if (isToday) {
                if (c.getString(3).equals("2")) {
                    data_Today.add(new RowData_History( c.getString(0),c.getString(1),c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5)));
                }
            }else if(isYesterday){
                if (c.getString(3).equals("2")) {
                    data_yesterday.add(new RowData_History( c.getString(0),c.getString(1),c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5)));
                }
            } else{
                if (c.getString(3).equals("2")) {
                    data_else.add(new RowData_History( c.getString(0),c.getString(1),c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5)));
                }
            }


        }

        Log.e("Query Berhasil", "");
        c.close();
        db.close();

        dataChild.put(dataHeader.get(0), data_Today);
        dataChild.put(dataHeader.get(1), data_yesterday);
        dataChild.put(dataHeader.get(2), data_else);

        adapter = new ExpandableListAdapter(getActivity(), dataHeader, dataChild);
        expandableListView.setAdapter(adapter);





        return view;
    }
}
