package id.tech.hsmsjacket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by macbook on 2/11/16.
 */
public class Activity_History_Tabpager extends AppCompatActivity implements MaterialTabListener {
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_vp_tab);

        ActionBar ac = getSupportActionBar();
        ac.hide();

        ImageView img_Back = (ImageView)findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        viewPager = (ViewPager) this.findViewById(R.id.pager );

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabHost.setSelectedNavigationItem(position);
            }
        });

        //Menambah jenis Tab
        tabHost.addTab(tabHost.newTab().setText("MENUNGGU").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("BERHASIL").setTabListener(this));
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            switch (num){
                case 0:
                    return new Activity_History_Expandable_Pending();
                case 1:
                    return new Activity_History_Expandable_Confirmed();

                default:
                    return new Activity_History_Expandable_Pending();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MENUNGGU";
                case 1:
                    return "BERHASIL";
                default:
                    return"";

            }

        }
    }
}
