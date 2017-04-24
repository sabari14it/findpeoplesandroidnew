package me.sabarirangan.apps.findpeoples.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import me.sabarirangan.apps.findpeoples.Adapter.PostRecyclerViewAdapter;
import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.model.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchProjectActivity extends AppCompatActivity {

    ArrayList<Project> postlist=new ArrayList<>();
    Toolbar toolbar;
    PostRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_search_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getPosts();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.post_recycler_view);
        adapter = new PostRecyclerViewAdapter(postlist,recyclerView,this);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager line=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(line);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void getPosts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Project>> call=findPeoplesAPI.getProjectsByTag(Prefs.getString("token","abcd"),getIntent().getStringExtra("query"));
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                postlist.addAll(response.body());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return true;
    }
}
