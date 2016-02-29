package id.tech.hsmsjacket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class Activity_Home extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar ac = getSupportActionBar();
        ac.hide();

        rv = (RecyclerView)findViewById(R.id.rv);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);

        adapter = new RecyclerAdapter_Home(this,getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new RecyclerAdapter_Home(this,getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }
}
