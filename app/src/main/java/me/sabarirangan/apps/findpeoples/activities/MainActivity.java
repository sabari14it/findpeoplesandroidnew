package me.sabarirangan.apps.findpeoples.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.fragments.HomeFragment;
import me.sabarirangan.apps.findpeoples.fragments.NotificationFragment;
import me.sabarirangan.apps.findpeoples.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewPager viewPager= (ViewPager)findViewById(R.id.viewpager);
        searchView=(MaterialSearchView)findViewById(R.id.search_view);
        MainPageFragmentPagerAdapter adapter=new MainPageFragmentPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        findViewById(R.id.newpost).setVisibility(View.VISIBLE);
                        break;
                    default:
                        findViewById(R.id.newpost).setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final TabLayout tabLayout= (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_account_circle_black_24dp));
        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.toolbar));
        FloatingActionButton post=(FloatingActionButton)findViewById(R.id.newpost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, NewPost.class);
                startActivity(i);
            }
        });
    }


    class MainPageFragmentPagerAdapter extends FragmentPagerAdapter {
        Context context;
        public MainPageFragmentPagerAdapter(FragmentManager fm, Context context){
            super(fm);
            this.context=context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    //findViewById(R.id.newpost).setVisibility(View.VISIBLE);
                    return new HomeFragment();
                case 1:
                    //findViewById(R.id.newpost).setVisibility(View.GONE);
                    return new NotificationFragment();
                case 2:
                    //findViewById(R.id.newpost).setVisibility(View.GONE);
                    return new ProfileFragment();
            }
            return new HomeFragment();
        }

    }
}
