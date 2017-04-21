package me.sabarirangan.apps.findpeoples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
//import me.sabarirangan.apps.findpeoples.Adapter.PostRecyclerViewAdapter;
import io.realm.Sort;
import me.sabarirangan.apps.findpeoples.Adapter.PostRecyclerViewAdapter;
import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.activities.MainActivity;
import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.model.Project;
import me.sabarirangan.apps.findpeoples.model.Tags;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ubuntu on 14/1/17.
 */

public class HomeFragment extends Fragment {

    private Realm realm;


    PostRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private static int firstVisibleInListview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPosts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        realm = Realm.getDefaultInstance();
        RealmResults<Project> projects=realm.where(Project.class).findAll().sort("created_at", Sort.DESCENDING);

        UltimateRecyclerView recyclerView=(UltimateRecyclerView) v.findViewById(R.id.post_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        adapter=new PostRecyclerViewAdapter(projects,getContext());
        recyclerView.setAdapter(adapter);
        projects.addChangeListener(new RealmChangeListener<RealmResults<Project>>() {
            @Override
            public void onChange(RealmResults<Project> element) {
                adapter.notifyDataSetChanged();
                Log.d("sabari123",Integer.toString(element.size()));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        final LinearLayoutManager line=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(line);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        firstVisibleInListview = line.findFirstVisibleItemPosition();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ((MainActivity)getActivity()).findViewById(R.id.newpost).setVisibility(View.GONE);
                } else {
                    ((MainActivity)getActivity()).findViewById(R.id.newpost).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){
                Log.d("MyTag","home Fragment is visible");


            }else{
                //Log.d("MyTag","My Fragment is not visible");
            }
        }
    }

    private void getPosts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Project>> call=findPeoplesAPI.getProjectList(Prefs.getString("token","abcd"));
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

            }
        });
    }


}
