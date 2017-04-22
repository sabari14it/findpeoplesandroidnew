package me.sabarirangan.apps.findpeoples.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;
import me.sabarirangan.apps.findpeoples.Adapter.MyProjectRVAdapter;
import me.sabarirangan.apps.findpeoples.Adapter.PostRecyclerViewAdapter;
import me.sabarirangan.apps.findpeoples.Adapter.TagAdapter;
import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.activities.NewPostTagsActivity;
import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.model.Project;
import me.sabarirangan.apps.findpeoples.model.Tags;
import me.sabarirangan.apps.findpeoples.model.UserProfile;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public RealmResults<Project> projects;
    public UserProfile userProfile;
    private Realm realm;
//
    SpacingItemDecoration space;
    ChipsLayoutManager spanLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm=Realm.getDefaultInstance();
        projects=realm.where(Project.class).findAll();
        projectadapter=new MyProjectRVAdapter(projects);
//        if (savedInstanceState == null||(savedInstanceState!=null&&savedInstanceState.size()==0)) {
//            adapter = new SkillsRecyclerViewAdapter((ArrayList<Skill>) skills);
//            projectadapter = new MyProjectRVAdapter((ArrayList<ProjectTitle>) projects);
//            getPrjects();
//            getSkills();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        username= (TextView) v.findViewById(R.id.username);
        imageView=(CircularImageView)v.findViewById(R.id.profile_pic);
        getUserProfile();
        getProjects();
        skillrv=(RecyclerView)v.findViewById(R.id.skillsrv);
        projectrv=(RecyclerView)v.findViewById(R.id.userpostrv);
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
                startActivityForResult(i, 1);
                getUserProfile();
                adapter.notifyDataSetChanged();
            }

        });



        projectrv.setAdapter(projectadapter);
        projectrv.setLayoutManager(new LinearLayoutManager(getContext()));
        projectrv.setItemAnimator(new DefaultItemAnimator());
        realm=Realm.getDefaultInstance();
        if(Prefs.contains("userprofileid")){
            userProfile=realm.where(UserProfile.class).equalTo("id",Prefs.getInt("userprofileid",1)).findFirst();
            adapter=new TagAdapter(userProfile.getSkills());
            userProfile.addChangeListener(new RealmChangeListener<UserProfile>() {
                @Override
                public void onChange(UserProfile element) {
                    adapter.notifyDataSetChanged();
                }
            });

        }
        skillrv.setAdapter(adapter);
        //Toast.makeText(getContext(), Integer.toString(skills.size())+Integer.toString(projects.size()),Toast.LENGTH_LONG).show();
        skillrv.addItemDecoration(space);
        skillrv.setLayoutManager(spanLayoutManager);
        skillrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
        skillrv.getRecycledViewPool().setMaxRecycledViews(1, 10);

        return v;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){
                getUserProfile();
                Log.d("MyTag","Profile Fragment is visible");
            }else{
                //Log.d("MyTag","Profile Fragment is not visible");
            }
        }
    }




//    private void startPermissionsActivity(String[] permission) {
//        PermissionsActivity.startActivityForResult(this.getActivity(), 0, permission);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==2){
//            for(String s:data.getExtras().getStringArrayList("skills")) {
//                Skill s1=new Skill();
//                s1.setName(s);
//                skills.clear();
//                skills.add(s1);
//                adapter.notifyDataSetChanged();
//            }
//        }
//        else {
//            android.util.Log.w("test", Integer.toString(resultCode) + Integer.toString(requestCode));
//            //if (resultCode == RESULT_OK && requestCode == 1010) {
//            if (data == null) {
//                android.util.Log.w("test", "data null");
//                return;
//            }
//            Uri selectedImageUri = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
//
//            if (cursor != null) {
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imagePath = cursor.getString(columnIndex);
//
//                Picasso.with(getContext()).load(new File(imagePath))
//                        .into(imageView);
//                uploadImage();
//
//                cursor.close();
//
//            } else {
//                android.util.Log.w("test", "cursor null");
//            }
//        }
//    }
//    private void uploadImage() {
//
//        /**
//         * Progressbar to Display if you need
//         */
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
//        progressDialog.show();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//        //Create Upload Server Client
//
//        //File creating from selected URL
//        File file = new File(imagePath);
//
//        // create RequestBody instance from file
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        // MultipartBody.Part is used to send also the actual file name
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
//
//        Call<Result> resultCall = findPeoplesAPI.uploadImage(Prefs.getString("token","abcd"),body);
//
//        // finally, execute the request
//        resultCall.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//
//                progressDialog.dismiss();
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                progressDialog.dismiss();
//            }
//        });
//    }
//
    private void getProjects() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Project>> call=findPeoplesAPI.getMyProjects(Prefs.getString("token",""));
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                //Toast.makeText(getContext(),response.body().get(0).getName(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Toast.makeText(getContext(),"failure",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserProfile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<UserProfile> call=findPeoplesAPI.getUserProfile(Prefs.getString("token","ssdd"));
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                realm.beginTransaction();
                userProfile=realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                Prefs.putInt("userprofileid",userProfile.getId());
                username.setText(userProfile.getUser().getUsername());
                Picasso.with(getActivity())
                        .load(userProfile.getAvatar())
                        .into(imageView);
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });

    }

}
