package id.tech.hsmsjacket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.tech.orm_sugar.SLite;

/**
 * Created by macbook on 2/7/16.
 */
public class Activity_History_Fragment_Pending extends Fragment {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<RowData_History> data;
    Context context;

    public Activity_History_Fragment_Pending(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_rv_history, null);
        rv = (RecyclerView)view.findViewById(R.id.rv);
        data = new ArrayList<>();


        context = getActivity().getApplicationContext();
        
        SQLiteDatabase db = SLite.openDatabase(context);
//        final Cursor c = db.rawQuery("SELECT * FROM tbl_sms",new String[] {} );
        final Cursor c = db.query("tbl_sms", new String[] {"trxId", "masonId", "qty", "confirmCode","dateReceived",
        "retailerId"}
                ,null, null, null, null,"dateReceived DESC", null);

        while(c.moveToNext()){
//            jika pending maka masukan ke data
            Log.e("QUERY RESULT = ", "TrxId = " + c.getString(0) + " & confirm Code = " + c.getString(3));
            if(c.getString(3).equals("1")){
                data.add(new RowData_History( c.getString(0),c.getString(1),c.getString(2), c.getString(3), c.getString(4),
                        c.getString(5)));
                Log.e("QUERY RESULT = ", "TrxId = " + c.getString(0) + " & confirm Code = " + c.getString(3));
            }

        }

        Log.e("Query Berhasil", "");
        c.close();
        db.close();

//        Collections.reverse(data);
        layoutManager = new LinearLayoutManager(context, 1, false);
        adapter = new RecyclerAdapter_History(context, data);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);


        return view;
    }
}
