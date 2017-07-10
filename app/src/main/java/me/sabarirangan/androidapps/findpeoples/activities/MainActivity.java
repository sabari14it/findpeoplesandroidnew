package me.sabarirangan.androidapps.findpeoples.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.fragments.HomeFragment;
import me.sabarirangan.androidapps.findpeoples.fragments.NotificationFragment;
import me.sabarirangan.androidapps.findpeoples.fragments.ProfileFragment;
import me.sabarirangan.androidapps.findpeoples.model.Category;
import me.sabarirangan.androidapps.findpeoples.model.Result;
import me.sabarirangan.androidapps.findpeoples.model.SearchHistory;
import me.sabarirangan.androidapps.findpeoples.model.Tags;
import me.sabarirangan.androidapps.findpeoples.model.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private ArrayList<Tags> gData=new ArrayList<>();
    TabLayout tabLayout;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    NotificationFragment notificationFragment;
    Realm realm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        realm=Realm.getDefaultInstance();
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
        tabLayout= (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_home_active));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_notification_inactive));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_profile_inactive));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getSupportActionBar().setTitle("Home");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Notification");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Profile");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_home_active));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_notification_inactive));
                        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_profile_inactive));
                        findViewById(R.id.newpost).setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_home_inactive));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_notification_active));
                        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_profile_inactive));
                        findViewById(R.id.newpost).setVisibility(View.GONE);
                        break;
                    case 2:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_home_inactive));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_notification_inactive));
                        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_profile_active));
                        findViewById(R.id.newpost).setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Home");
//        FloatingActionButton post=(FloatingActionButton)findViewById(R.id.newpost);
//        post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(MainActivity.this, NewPost.class);
//                startActivity(i);
//            }
//        });
        android.support.design.widget.FloatingActionButton post= (android.support.design.widget.FloatingActionButton) findViewById(R.id.newpost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, NewPost.class);
                startActivity(i);
            }
        });
        RealmResults<SearchHistory> results=realm.where(SearchHistory.class).findAll();
        String[] history=new String[results.size()];
        int i=0;
        for(SearchHistory s:results){
            history[i++]=s.getHistory();
        }
        searchView.setSuggestions(history);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //sdasdasd
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchHistory historyobj=new SearchHistory();
                historyobj.setHistory(query);
                realm.beginTransaction();
                realm.copyToRealm(historyobj);
                realm.commitTransaction();
                Intent i=new Intent(MainActivity.this,SearchProjectActivity.class);
                i.putExtra("query",query);
                startActivityForResult(i,100);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                searchView.getLayoutParams().height=300;
//                l.removeView(viewPager);
                final ArrayList<Category> suggestions=new ArrayList<>();
                Log.d("name", Integer.toString(gData.size()));
                for (Tags suggestion : gData) {
                    String name = suggestion.getName().toLowerCase();
                    Log.d("name",name);
                    if (name.startsWith(newText)&&!name.equals(newText)) {
                        suggestions.add(new Category(suggestion.getName()));
                    }
                }
                String[] arr=new String[suggestions.size()];
                int i=0;
                for(Category c:suggestions){
                    arr[i++]=c.getName();
                }
                searchView.setSuggestions(arr);


                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                Call<List<Tags>> call=findPeoplesAPI.getTags(Prefs.getString("token",""));
                call.enqueue(new Callback<List<Tags>>(){
                    @Override
                    public void onResponse(Call<List<Tags>> call, Response<List<Tags>> response) {
                        gData.clear();
                        gData.addAll(response.body());
                        Log.d("sabarirangan",Integer.toString(gData.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Tags>> call, Throwable t) {
                        gData.clear();

                    }
                });

            }

            @Override
            public void onSearchViewClosed() {
                MaterialSearchView layout = (MaterialSearchView) findViewById(R.id.search_view);
// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);
                //searchView.setLayoutParams(new MaterialSearchView.LayoutParams(MaterialSearchView.LayoutParams.MATCH_PARENT, MaterialSearchView.LayoutParams.WRAP_CONTENT));
                //Do some magic
            }
        });
        Boolean mAction=getIntent().getBooleanExtra("notification",false);
        if(mAction){
            getSupportActionBar().setTitle("Notification");
            viewPager.setCurrentItem(1,false);
        }


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
                    homeFragment= new HomeFragment();
                    return homeFragment;
                case 1:
                    notificationFragment= new NotificationFragment();
                    return notificationFragment;
                case 2:
                    profileFragment=new ProfileFragment();
                    return profileFragment;
            }
            return new HomeFragment();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mainpagemenu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.logout){
            Prefs.putBoolean("login",false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
            Token t=new Token();
            t.setCode(Prefs.getString("fcmtoken","abc"));
            Call<Result> call=findPeoplesAPI.revokeFCM(Prefs.getString("token","abcd"),t);// t is the auth code
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    startActivity(new Intent(MainActivity.this,GoogleSigninActivity.class));
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Snackbar.make(findViewById(R.id.main_layout),"No Internet",Snackbar.LENGTH_SHORT).show();
                }
            });

            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            homeFragment.onActivityResult(requestCode,resultCode,data);
            searchView.closeSearch();
        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(homeFragment!=null){
//            homeFragment.onActivityResult(100,100,null);
//        }
//    }
}
