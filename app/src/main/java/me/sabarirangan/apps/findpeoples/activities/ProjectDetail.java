package me.sabarirangan.apps.findpeoples.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.sabarirangan.apps.findpeoples.Adapter.CommentRecyclerViewAdapter;
import me.sabarirangan.apps.findpeoples.Adapter.TagAdapter;
import me.sabarirangan.apps.findpeoples.R;

import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.model.Comment;
import me.sabarirangan.apps.findpeoples.model.NewReview;
import me.sabarirangan.apps.findpeoples.model.Project;
import me.sabarirangan.apps.findpeoples.model.Result;
import me.sabarirangan.apps.findpeoples.model.Review;
import me.sabarirangan.apps.findpeoples.model.Tags;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProjectDetail extends AppCompatActivity {
    List<Comment> commentlist;
    List<Tags> taglist;
    TagAdapter tagAdapter;
    CommentRecyclerViewAdapter commentAdapter;
    TextView title,description,username,createdAt;
    RecyclerView commentrv,tagrv;
    RoundedImageView avatar;
    ImageButton sendbutton;
    EditText comment;
    Toolbar toolbar;
    Project p;
    CoordinatorLayout l;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        realm=Realm.getDefaultInstance();
        title= (TextView) findViewById(R.id.title);
        avatar=(RoundedImageView)findViewById(R.id.avatar);
        username=(TextView)findViewById(R.id.username);
        createdAt=(TextView)findViewById(R.id.created_at);
        description=(TextView)findViewById(R.id.description);
        commentrv= (RecyclerView) findViewById(R.id.commentrv);
        commentrv.setItemAnimator(new DefaultItemAnimator());
        commentrv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tagrv= (RecyclerView) findViewById(R.id.tagrv);
        SpacingItemDecoration space=new SpacingItemDecoration(this.getResources().getDimensionPixelOffset(R.dimen.item_space),this.getResources().getDimensionPixelOffset(R.dimen.item_space));
        tagrv.addItemDecoration(space);
        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(this)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        tagrv.setLayoutManager(spanLayoutManager);
        tagrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
        tagrv.getRecycledViewPool().setMaxRecycledViews(1, 10);
        sendbutton= (ImageButton) findViewById(R.id.commentsendbutton);
        l= (CoordinatorLayout) findViewById(R.id.detailact);
        comment=(EditText) findViewById(R.id.commentcontent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i=getIntent();
        Bundle b=i.getExtras();
        int projectid=b.getInt("projectid");
        p=realm.where(Project.class).equalTo("id",projectid).findFirst();
        title.setText(p.getTitle());
        Picasso.with(this)
                .load(p.getUser().getAvatar())
                .resize(70, 70)
                .into(avatar);
        username.setText(p.getUser().getUser().getUsername());
        createdAt.setText(p.getCreated_at());
        description.setText(p.getDescription());
        taglist= p.getTags();
        tagAdapter=new TagAdapter(taglist);
        tagAdapter.notifyDataSetChanged();
        tagrv.setAdapter(tagAdapter);
        getComments();
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentrv.smoothScrollToPosition(commentAdapter.getItemCount()-1);
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(l.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                NewReview review=new NewReview();
                final String commentstring=comment.getText().toString();
                review.setComment(comment.getText().toString());
                comment.setText("");
                review.setRating(4);
                Call<Result> call=findPeoplesAPI.postReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                       getComments();
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void getComments() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Comment>> call=findPeoplesAPI.getProjectComment(Prefs.getString("token",""),Integer.toString(p.getId()));
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                commentAdapter=new CommentRecyclerViewAdapter(response.body(),ProjectDetail.this);
                commentrv.setAdapter(commentAdapter);
                //Toast.makeText(getContext(),response.body().get(0).getName(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                RealmResults<Review> reviews=realm.where(Review.class).equalTo("project.id",p.getId()).findAllSorted("created_at");
                ArrayList<Comment> comments=new ArrayList<Comment>();
                for(Review r:reviews){
                    comments.addAll(realm.where(Comment.class).equalTo("review.id",r.getId()).findAll());
                }
                commentAdapter=new CommentRecyclerViewAdapter(comments,ProjectDetail.this);
                commentrv.setAdapter(commentAdapter);
                Toast.makeText(ProjectDetail.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return true;
    }


}
