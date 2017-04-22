package me.sabarirangan.apps.findpeoples.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import io.realm.RealmResults;
import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.model.Category;
import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.model.NewProject;
import me.sabarirangan.apps.findpeoples.model.Result;
import me.sabarirangan.apps.findpeoples.model.Tags;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPostTagsActivity extends AppCompatActivity {


    ArrayList<String> newskills;
    boolean flag=false;
    private RichEditorView editor;
    NewProject project;
    Category.CategoryLoader loader=new Category.CategoryLoader();
    Realm realm;
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
        if(getIntent().getAction().equals("editskills")){
            flag=true;
            newskills=new ArrayList<>();
            newskills.addAll(getIntent().getExtras().getStringArrayList("skills"));
            String setstring="";
            for(String s:newskills){
                setstring+=s+",";
            }
            editor.setText(setstring);

        }
        else {
            Bundle bundle = getIntent().getExtras();
            project = new NewProject();
            project.setTitle(bundle.getString("title"));
            project.setDescription(bundle.getString("description"));
            Log.w("tags", project.getTitle());
        }
        editor.setHint("tag");
        editor.setTokenizer(new WordTokenizer(tokenizerConfig));
        editor.setQueryTokenReceiver(new QueryTokenReceiver() {
            @Override
            public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
                List<String> buckets = Collections.singletonList(BUCKET);
                List<Category> suggestions = loader.getSuggestions(queryToken);
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


        if(flag==true){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
            Call<Result> call = findPeoplesAPI.editSkills(Prefs.getString("token", "abcd"), items);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.d("jarvis",Integer.toString(response.code()));
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.d("jarvis",t.getMessage());
                }
            });
            Intent intent=new Intent();
            intent.putExtra("skills",items);
            setResult(2,intent);
            finish();//finishing activity

        }else {
            project.setTags(items);
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
        }
        return true;
    }
}
