package me.sabarirangan.androidapps.findpeoples.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import me.sabarirangan.androidapps.findpeoples.Adapter.PostRecyclerViewAdapter;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import me.sabarirangan.androidapps.findpeoples.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchProjectActivity extends AppCompatActivity {

    ArrayList<Project> postlist=new ArrayList<>();
    Toolbar toolbar;
    PostRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    Realm realm;
    TextView nopost;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_search_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar= (ProgressBar) findViewById(R.id.progressBar2);
        realm=Realm.getDefaultInstance();
        nopost= (TextView) findViewById(R.id.noposts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        getPosts();
        recyclerView=(RecyclerView)findViewById(R.id.post_recycler_view);
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
        Result result=new Result();
        result.setOk(getIntent().getStringExtra("query"));
        Call<List<Project>> call=findPeoplesAPI.getProjectsByTag(Prefs.getString("token","abcd"),result);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.body()!=null) {
                    postlist.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if(response.body().size()==0){
                        nopost.setVisibility(View.VISIBLE);
                    }else {
                        nopost.setVisibility(View.GONE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                nopost.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent();
            setResult(100,intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101) {
            int pos = data.getIntExtra("position", 0);
            Project p1 = realm.where(Project.class).equalTo("id", data.getIntExtra("projectid", 0)).findFirst();
            postlist.get(pos).setStatus(p1.getStatus());
            adapter = new PostRecyclerViewAdapter(postlist, recyclerView, this);
            recyclerView.setAdapter(adapter);
        }else {
            Intent intent=new Intent();
            setResult(100,intent);
            finish();
        }
    }

}
