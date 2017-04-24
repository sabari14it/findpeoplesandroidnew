package me.sabarirangan.apps.findpeoples.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.fragments.HomeFragment;
import me.sabarirangan.apps.findpeoples.fragments.NotificationFragment;
import me.sabarirangan.apps.findpeoples.fragments.ProfileFragment;
import me.sabarirangan.apps.findpeoples.model.Category;
import me.sabarirangan.apps.findpeoples.model.Tags;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private ArrayList<Tags> gData=new ArrayList<>();
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
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i=new Intent(MainActivity.this,SearchProjectActivity.class);
                i.putExtra("query",query);
                startActivity(i);
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
                    if (name.startsWith(newText)) {
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
                        Log.d("sabarirangan",t.getMessage());
                    }
                });

            }

            @Override
            public void onSearchViewClosed() {
                MaterialSearchView layout = (MaterialSearchView) findViewById(R.id.search_view);
// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);
                //searchView.setLayoutParams(new MaterialSearchView.LayoutParams(MaterialSearchView.LayoutParams.MATCH_PARENT, MaterialSearchView.LayoutParams.WRAP_CONTENT));
                //Do some magic
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

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }
}
