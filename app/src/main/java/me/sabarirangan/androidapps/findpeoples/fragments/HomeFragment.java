package me.sabarirangan.androidapps.findpeoples.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import io.realm.Realm;
import io.realm.RealmResults;
//import me.sabarirangan.apps.findpeoples.Adapter.PostRecyclerViewAdapter;
import me.sabarirangan.androidapps.findpeoples.Adapter.PostRecyclerViewAdapter;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Project;
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
    protected Handler handler;
    public static int pageNumber;
    PostRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Project> projects;
    LinearLayoutManager layoutManager;
    static Boolean lastpage=false;
    RecyclerView recyclerView;
    static boolean called=false;
    static boolean switchtab=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        projects=new ArrayList<>();
        pageNumber=1;
        getPosts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView=(RecyclerView)v.findViewById(R.id.post_recycler_view);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new PostRecyclerViewAdapter(projects,recyclerView,this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        if(!switchtab){
            switchtab=true;
            swipeRefreshLayout.setRefreshing(true);
        }




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber=1;
                getPosts();

            }
        });
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                projects.add(null);
                adapter.notifyItemInserted(projects.size() - 1);
                if(!lastpage)
                ++pageNumber;
                getPosts();

            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){



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
        called=true;
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Log.d("pagenumber",Integer.toString(pageNumber));
        Call<List<Project>> call=findPeoplesAPI.getProjectList(Prefs.getString("token","abcd"),Integer.toString(pageNumber));
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(pageNumber==1){
                    lastpage=false;
                    RealmResults realmResults=realm.where(Project.class).notEqualTo("user.id",Prefs.getInt("userprofileid",0)).findAll();
                    realm.beginTransaction();
                    realmResults.deleteAllFromRealm();
                    realm.commitTransaction();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body());
                    realm.commitTransaction();
                    projects.clear();
                    projects.addAll(response.body());
                    adapter.notifyDataSetChanged();

                }else{
                    projects.remove(null);
                    if(projects.size()==(pageNumber-1)*5){
                        if(response.body().size()==0){
                            lastpage=true;
                        }else {
                            lastpage=false;
                            projects.addAll(response.body());
                        }
                    }

                    adapter.notifyDataSetChanged();

                }


                swipeRefreshLayout.setRefreshing(false);
                adapter.setLoaded();
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                if(pageNumber==1){
                    projects.clear();
                    adapter.notifyDataSetChanged();
                    RealmResults realmResults=realm.where(Project.class).notEqualTo("user.id",Prefs.getInt("userprofileid",0)).findAll();
                    projects.remove(null);
                    projects.addAll(realmResults);
                    adapter.notifyDataSetChanged();
                    lastpage=true;
                    Snackbar.make(getView(),"No Internet",Snackbar.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setLoaded();

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            int pos=data.getIntExtra("position",0);
            Project p1=realm.where(Project.class).equalTo("id",data.getIntExtra("projectid",0)).findFirst();
            realm.beginTransaction();
            projects.get(pos).setStatus(p1.getStatus());
            realm.commitTransaction();
            adapter=new PostRecyclerViewAdapter(projects,recyclerView,this);
            recyclerView.setAdapter(adapter);
        }else{
            projects.clear();
            pageNumber=1;
//            swipeRefreshLayout.setRefreshing(true);
            getPosts();
            adapter=new PostRecyclerViewAdapter(projects,recyclerView,this);
            recyclerView.setAdapter(adapter);
        }

    }
}
