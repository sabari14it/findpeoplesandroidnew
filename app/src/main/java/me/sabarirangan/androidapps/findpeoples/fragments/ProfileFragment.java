package me.sabarirangan.androidapps.findpeoples.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import me.sabarirangan.androidapps.findpeoples.Adapter.MyProjectRVAdapter;
import me.sabarirangan.androidapps.findpeoples.Adapter.TagAdapter;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.NewPostTagsActivity;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import me.sabarirangan.androidapps.findpeoples.model.Tags;
import me.sabarirangan.androidapps.findpeoples.model.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ubuntu on 17/1/17.
 */

public class ProfileFragment extends Fragment {
    public TagAdapter adapter;
    MyProjectRVAdapter projectadapter;
    TextView username;
    RecyclerView skillrv;
    RecyclerView projectrv;
    public CircularImageView imageView;
    private ImageButton editskills;
    public List<Project> projects;
    public UserProfile userProfile;
    private Realm realm;
    Boolean lastpage=false;
    int pageNumber=1;
    int userprofileid;
    SpacingItemDecoration space;
    ChipsLayoutManager spanLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projects=new ArrayList<>();
        realm=Realm.getDefaultInstance();
        if(!((Activity)getContext()).getIntent().getBooleanExtra("userprofiledetail",false)){
            this.userprofileid=Prefs.getInt("userprofileid",0);
            getProjects();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        username= (TextView) v.findViewById(R.id.username);
        imageView=(CircularImageView)v.findViewById(R.id.profile_pic);
        skillrv=(RecyclerView)v.findViewById(R.id.skillsrv);
        projectrv=(RecyclerView)v.findViewById(R.id.userpostrv);
        projectrv.setLayoutManager(new LinearLayoutManager(getContext()));
        projectrv.setItemAnimator(new DefaultItemAnimator());
        projectadapter=new MyProjectRVAdapter(projects,this,projectrv);
        projectrv.setAdapter(projectadapter);
        projectadapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                projects.add(null);
                projectadapter.notifyItemInserted(projects.size() - 1);
                if(!lastpage)
                    ++pageNumber;
                getProjects();
            }
        });
        spanLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        space=new SpacingItemDecoration(getContext().getResources().getDimensionPixelOffset(R.dimen.item_space),getContext().getResources().getDimensionPixelOffset(R.dimen.item_space));
        skillrv.addItemDecoration(space);
        editskills= (ImageButton) v.findViewById(R.id.editskills);

        editskills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewPostTagsActivity.class);
                i.setAction("editskills");
                ArrayList<String> skilllist = new ArrayList<String>();
                for (Tags s : userProfile.getSkills()) {
                    skilllist.add(s.getName());
                }
                i.putExtra("skills", skilllist);
                adapter.notifyDataSetChanged();
                startActivityForResult(i, 1);

            }

        });
        skillrv.addItemDecoration(space);
        skillrv.setLayoutManager(spanLayoutManager);
        skillrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
        skillrv.getRecycledViewPool().setMaxRecycledViews(1, 10);
        populateContent(Prefs.getInt("userprofileid",0));

        return v;
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisible()){
//            if(isVisibleToUser){
//                Log.d("MyTag","Profile Fragment is visible");
//            }else{
//                //Log.d("MyTag","Profile Fragment is not visible");
//            }
//        }
//    }




    private void getProjects() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Project>> call=findPeoplesAPI.getProjects(Prefs.getString("token",""),Integer.toString(this.userprofileid),Integer.toString(pageNumber));
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(pageNumber==1){
                    lastpage=false;
                    RealmResults realmResults=realm.where(Project.class).equalTo("user.id",Prefs.getInt("userprofileid",0)).findAll();
                    realm.beginTransaction();
                    realmResults.deleteAllFromRealm();
                    realm.commitTransaction();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body());
                    realm.commitTransaction();
                    projects.clear();
                    projects.addAll(response.body());
                    projectadapter.notifyDataSetChanged();

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

                    projectadapter.notifyDataSetChanged();

                }


                projectadapter.setLoaded();
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                if(((Activity)getContext()).getIntent().getBooleanExtra("userprofiledetail",false)){
                    return;
                }
                if(pageNumber==1){
                    projects.clear();
                    projectadapter.notifyDataSetChanged();
                    RealmResults realmResults=realm.where(Project.class).notEqualTo("user.id",Prefs.getInt("userprofileid",0)).findAll();
                    projects.remove(null);
                    projects.addAll(realmResults);
                    projectadapter.notifyDataSetChanged();
                    lastpage=true;
                    Snackbar.make(getView(),"No Internet",Snackbar.LENGTH_LONG).show();
                    projectadapter.setLoaded();

                }
            }
        });
    }

    public void populateContent(int userprofileid){
        if(userprofileid!=Prefs.getInt("userprofileid",0))
            editskills.setVisibility(View.GONE);

        if(Prefs.getInt("userprofileid",0)!=userprofileid){
            this.userprofileid=userprofileid;
            projects.clear();
            adapter.notifyDataSetChanged();
            getProjects();
        }
        userProfile=realm.where(UserProfile.class).equalTo("id",userprofileid).findFirst();
        username.setText(userProfile.getUser().getFirst_name());
        Picasso.with(getContext())
                .load(userProfile.getAvatar())
                .resize(70, 70)
                .into(imageView);
        adapter=new TagAdapter(userProfile.getSkills(),getActivity());
        skillrv.setAdapter(adapter);
        userProfile.getSkills().addChangeListener(new RealmChangeListener<RealmList<Tags>>() {
            @Override
            public void onChange(RealmList<Tags> element) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            projects.clear();
            pageNumber=1;
            userprofileid=Prefs.getInt("userprofileid",0);
            getProjects();
            projectadapter=new MyProjectRVAdapter(projects,this,projectrv);
            projectrv.setAdapter(projectadapter);
            projectadapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    projects.add(null);
                    projectadapter.notifyItemInserted(projects.size() - 1);
                    if(!lastpage)
                        ++pageNumber;
                    getProjects();
                }
            });
        }
    }
}
