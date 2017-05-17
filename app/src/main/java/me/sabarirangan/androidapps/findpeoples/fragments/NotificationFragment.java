package me.sabarirangan.androidapps.findpeoples.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import me.sabarirangan.androidapps.findpeoples.Adapter.CommentRecyclerViewAdapter;
import me.sabarirangan.androidapps.findpeoples.Adapter.NotifyAdapter;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Comment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 15/1/17.
 */

public class NotificationFragment extends Fragment {
    ArrayList<Comment> comments;
    NotifyAdapter adapter;
    Realm realm;
    static int pagenumber=1;
    static Boolean lastpage=false;
    TextView noalerts;
    private BroadcastReceiver mMyBroadcastReceiver;

    @Override
    public void onResume() {
        super.onResume();
        mMyBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                pagenumber=1;
                getMyComments();
            }
        };
        try {

            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMyBroadcastReceiver,new IntentFilter("notification"));

        } catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMyBroadcastReceiver);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comments=new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_notify,container,false);
        comments.clear();
        pagenumber=1;
        getMyComments();
        realm=Realm.getDefaultInstance();
        noalerts= (TextView) v.findViewById(R.id.noalerts);
        RecyclerView recyclerView= (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new NotifyAdapter(comments,getContext(),recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                comments.add(null);
                adapter.notifyItemInserted(comments.size() - 1);
                if(!lastpage)
                    ++pagenumber;
                getMyComments();
            }
        });

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){
                Log.d("MyTag","Notify Fragment is visible");
            }else{
                //Log.d("MyTag","My Fragment is not visible");
            }
        }
    }

    private void getMyComments() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Comment>> call=findPeoplesAPI.getMyComment(Prefs.getString("token","abcd"),Integer.toString(pagenumber));
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response)
            {
                if(pagenumber==1) {
                    RealmResults results = realm.where(Comment.class).equalTo("notify", 1).findAll();
                    realm.beginTransaction();
                    results.deleteAllFromRealm();
                    realm.commitTransaction();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body());
                    realm.commitTransaction();
                    comments.clear();
                    if(response.body().size()==0){
                        noalerts.setVisibility(View.VISIBLE);
                        lastpage=true;
                    }else{
                        noalerts.setVisibility(View.GONE);
                    }

                }else{
                    if(response.body().size()==0){
                        lastpage=true;
                    }
                    comments.remove(null);
                }
                comments.addAll(response.body());
                adapter.notifyDataSetChanged();
                adapter.setLoaded();

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                if(pagenumber==1) {
                    RealmResults results = realm.where(Comment.class).equalTo("notify", 1).findAll();
                    comments.clear();
                    comments.addAll(results);
                    adapter.notifyDataSetChanged();
                }
                comments.remove(null);
                adapter.setLoaded();

            }
        });
    }
}
