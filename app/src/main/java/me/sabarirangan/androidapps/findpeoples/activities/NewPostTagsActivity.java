package me.sabarirangan.androidapps.findpeoples.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.RichEditorView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.model.Category;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.model.NewProject;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import me.sabarirangan.androidapps.findpeoples.model.Result;
import me.sabarirangan.androidapps.findpeoples.model.Tags;
import me.sabarirangan.androidapps.findpeoples.model.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPostTagsActivity extends AppCompatActivity {


    ArrayList<String> newskills;
    int flag=1;
    CheckBox anonymouscheck;
    static String mycurrstring="";
    private RichEditorView editor;
    NewProject project;
    Category.CategoryLoader loader=new Category.CategoryLoader(this);
    Realm realm;
    Project p;
    private static final String BUCKET = "categories-memory";
    private static final WordTokenizerConfig tokenizerConfig = new WordTokenizerConfig
            .Builder()
            .setWordBreakChars(",")
            .setExplicitChars("")
            .setMaxNumKeywords(1)
            .setThreshold(1)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_tags);

        realm=Realm.getDefaultInstance();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        loader.getCategoryFromDjango();
        editor= (RichEditorView) findViewById(R.id.editor);
        if(getIntent().getBooleanExtra("editpost",false)){
            flag=3;
            p=realm.where(Project.class).equalTo("id",getIntent().getIntExtra("projectid",1)).findFirst();
            String setstring="";
            for(Tags tag:p.getTags()){
                setstring+=tag.getName()+",";
            }
            editor.setText(setstring);
        }
        if(getIntent().getAction().equals("editskills")){
            flag=2;
            newskills=new ArrayList<>();
            newskills.addAll(getIntent().getExtras().getStringArrayList("skills"));
            String setstring="";
            for(String s:newskills){
                setstring+=s+",";
            }
            editor.setText(setstring);

        }
        else {
            anonymouscheck= (CheckBox) findViewById(R.id.anonymous);
            anonymouscheck.setVisibility(View.VISIBLE);
            Bundle bundle = getIntent().getExtras();
            project = new NewProject();
            project.setTitle(bundle.getString("title"));
            project.setDescription(bundle.getString("description"));
            Log.w("tags", project.getTitle());
        }
        editor.setHint("tag");
        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.charAt(s.length()-1)=='\n'||s.charAt(s.length()-1)==' '){
                    mycurrstring=s.toString();
                    mycurrstring=mycurrstring.replace(' ','_');
                    mycurrstring=mycurrstring.replace('\n',',');
                    editor.setText(mycurrstring);
                    editor.setSelection(mycurrstring.length());
                }
            }
        });
        editor.setTokenizer(new WordTokenizer(tokenizerConfig));
        editor.setQueryTokenReceiver(new QueryTokenReceiver() {
            @Override
            public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
                String s=queryToken.getKeywords();
                s=s.replace(' ','_');
                Log.d("sabariabcd",s);
                List<String> buckets = Collections.singletonList(BUCKET);
                List<Category> suggestions = loader.getSuggestions(s);
                SuggestionsResult result = new SuggestionsResult(queryToken, suggestions);
                editor.onReceiveSuggestionsResult(result, BUCKET);
                return buckets;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.new_project_menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String tags=editor.getText().toString();
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(tags.split("\\s*,\\s*")));
//        for(String s: items){
//            Log.w("tags",s);
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Constants.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//            Tags t=new Tags();
//            t.setName(s);
//            Call<Tags> call=findPeoplesAPI.postTag(Prefs.getString("token","abcd"),t);
//            call.enqueue(new Callback<Tags>() {
//                @Override
//                public void onResponse(Call<Tags> call, Response<Tags> response) {
//                }
//
//                @Override
//                public void onFailure(Call<Tags> call, Throwable t) {
//
//                }
//            });
//        }


        if(flag==2){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
            Call<UserProfile> call = findPeoplesAPI.editSkills(Prefs.getString("token", "abcd"), items);
            call.enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    Log.d("jarvis",Integer.toString(response.code()));
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body());
                    realm.commitTransaction();
                    Intent intent=new Intent();
                    setResult(2,intent);
                    finish();
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    Log.d("jarvis",t.getMessage());
                }
            });
            Intent intent=new Intent();
            intent.putExtra("skills",items);
            setResult(2,intent);
            finish();//finishing activity

        }else if(flag==1){
            project.setTags(items);
            project.setAnonymous(anonymouscheck.isEnabled());
            Intent i=new Intent(this,MainActivity.class);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
            Call<Result> call = findPeoplesAPI.postProject(Prefs.getString("token", "abcd"), project);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {

                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.d("jarvis",t.getMessage());
                }
            });

            startActivity(i);
        }else {

            project.setTags(items);
            final Intent i=new Intent(this,MainActivity.class);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
            Call<Project> call = findPeoplesAPI.updateProject(Prefs.getString("token", "abcd"),project,Integer.toString(p.getId()));
            call.enqueue(new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body());
                    realm.commitTransaction();
                    Intent intent=new Intent();
                    setResult(1441,intent);
                    finish();

                }

                @Override
                public void onFailure(Call<Project> call, Throwable t) {
                    Log.d("jarvis",t.getMessage());
                }
            });
        }
        return true;
    }


}
