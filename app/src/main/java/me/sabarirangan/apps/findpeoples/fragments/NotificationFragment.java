package me.sabarirangan.apps.findpeoples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.sabarirangan.apps.findpeoples.R;
import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ubuntu on 15/1/17.
 */

public class NotificationFragment extends Fragment {
//    ArrayList<Comment> reviewget=new ArrayList<>();
//    NotifyAdapter adapter;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null||(savedInstanceState!=null&&savedInstanceState.size()==0)){
//            adapter = new NotifyAdapter(reviewget);
//            getReview();
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_notify,container,false);
//        RecyclerView recyclerView= (RecyclerView) v.findViewById(R.id.recycler_view);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

//    private void getReview() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//        Call<List<Comment>> call=findPeoplesAPI.getReview(Prefs.getString("token","abcd"));
//        call.enqueue(new Callback<List<Comment>>() {
//            @Override
//            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//                reviewget.addAll( response.body());
//                android.util.Log.d("notify1",Integer.toString(reviewget.size()));
//                Log.d("notify",Integer.toString(reviewget.size()));
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<List<Comment>> call, Throwable t) {
//
//            }
//        });
//    }
}
