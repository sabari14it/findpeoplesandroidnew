package me.sabarirangan.apps.findpeoples.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import me.sabarirangan.apps.findpeoples.R;
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
//    public SkillsRecyclerViewAdapter adapter;
//    MyProjectRVAdapter projectadapter;
//    TextView username;
//    RecyclerView skillrv;
//    PermissionsChecker checker;
//    RecyclerView projectrv;
//    public String imagePath;
//    public CircularImageView imageView;
//    private ImageButton editskills;
//    public ArrayList<ProjectTitle> projects=new ArrayList<>();
//    public ArrayList<Skill> skills=new ArrayList<>();
//    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
//    SpacingItemDecoration space;
//    ChipsLayoutManager spanLayoutManager;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null||(savedInstanceState!=null&&savedInstanceState.size()==0)) {
//            adapter = new SkillsRecyclerViewAdapter((ArrayList<Skill>) skills);
//            projectadapter = new MyProjectRVAdapter((ArrayList<ProjectTitle>) projects);
//            getPrjects();
//            getSkills();
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        /*username= (TextView) v.findViewById(R.id.username);
        imageView=(CircularImageView)v.findViewById(R.id.profile_pic);
        skillrv=(RecyclerView)v.findViewById(R.id.skillsrv);
        projectrv=(RecyclerView)v.findViewById(R.id.userpostrv);
        spanLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        checker = new PermissionsChecker(getContext());
        space=new SpacingItemDecoration(getContext().getResources().getDimensionPixelOffset(R.dimen.item_space),getContext().getResources().getDimensionPixelOffset(R.dimen.item_space));
        skillrv.addItemDecoration(space);
        editskills= (ImageButton) v.findViewById(R.id.editskills);
        editskills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), NewPostTagsActivity.class);
                i.setAction("editskills");
                ArrayList<String> skilllist=new ArrayList<String>();
                for(Skill s:skills){
                    skilllist.add(s.getName());
                }
                i.putExtra("skills",skilllist);
                startActivityForResult(i,1);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
                    startPermissionsActivity(PERMISSIONS_READ_STORAGE);
                } else {
                    // File System.
                    final Intent galleryIntent = new Intent();
                    galleryIntent.setType("image*//*");
                    galleryIntent.setAction(Intent.ACTION_PICK);

                    // Chooser of file system options.
                    final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
                    startActivityForResult(chooserIntent, 1010);
                }
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<Avatar> call=findPeoplesAPI.getProfilePic(Prefs.getString("token","abcd"));
        call.enqueue(new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                Log.d("pic",Constants.BASE_URL+response.body().getAvatar());
                Picasso.with(getActivity())
                        .load(Constants.BASE_URL+response.body().getAvatar())
                        .into(imageView);
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {

            }
        });
        projectrv.setAdapter(projectadapter);
        projectrv.setLayoutManager(new LinearLayoutManager(getContext()));
        projectrv.setItemAnimator(new DefaultItemAnimator());
        skillrv.setAdapter(adapter);
        //Toast.makeText(getContext(), Integer.toString(skills.size())+Integer.toString(projects.size()),Toast.LENGTH_LONG).show();
        skillrv.addItemDecoration(space);
        skillrv.setLayoutManager(spanLayoutManager);
        skillrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
        skillrv.getRecycledViewPool().setMaxRecycledViews(1, 10);*/
        return v;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible()){
            if(isVisibleToUser){
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
//    private void getPrjects() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//        Call<List<ProjectTitle>> call=findPeoplesAPI.getMyProjects(Prefs.getString("token",""));
//        call.enqueue(new Callback<List<ProjectTitle>>() {
//            @Override
//            public void onResponse(Call<List<ProjectTitle>> call, Response<List<ProjectTitle>> response) {
//                projects= (ArrayList<ProjectTitle>) response.body();
//                projectadapter.notifyDataSetChanged();
//                //Toast.makeText(getContext(),response.body().get(0).getName(),Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<List<ProjectTitle>> call, Throwable t) {
//                Toast.makeText(getContext(),"failure",Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void getSkills() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//        Call<List<Skill>> call=findPeoplesAPI.getUserSkills(Prefs.getString("token",""));
//        call.enqueue(new Callback<List<Skill>>() {
//            @Override
//            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
//
//                    skills = (ArrayList<Skill>) response.body();
//                    adapter.notifyDataSetChanged();
//                    //Toast.makeText(getContext(),response.body().get(0).getName(),Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Skill>> call, Throwable t) {
//                Toast.makeText(getContext(),"failure",Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

}
