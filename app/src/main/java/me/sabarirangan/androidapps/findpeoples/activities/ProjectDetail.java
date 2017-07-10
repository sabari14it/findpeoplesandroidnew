package me.sabarirangan.androidapps.findpeoples.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import me.sabarirangan.androidapps.findpeoples.Adapter.CommentRecyclerViewAdapter;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Comment;
import me.sabarirangan.androidapps.findpeoples.model.NewComment;
import me.sabarirangan.androidapps.findpeoples.model.NewReview;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import me.sabarirangan.androidapps.findpeoples.model.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProjectDetail extends AppCompatActivity {
    List<Comment> commentlist;
    CommentRecyclerViewAdapter commentAdapter;
    TextView title,description,username,createdAt;
    RecyclerView commentrv;
    RoundedImageView avatar;
    ImageButton sendbutton,like,unlike;
    EditText comment;
    Toolbar toolbar;
    Project p;
    CoordinatorLayout l;
    Realm realm;
    LinearLayout interestedlayout;
    int pagenumber;
    boolean lastpage=false;
    Intent resInent;
    LinearLayout userprofilepost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        try {
        resInent=new Intent();
        userprofilepost= (LinearLayout) findViewById(R.id.userprofilepost);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        realm=Realm.getDefaultInstance();
        pagenumber=1;
        title= (TextView) findViewById(R.id.title);
        avatar=(RoundedImageView)findViewById(R.id.avatar);
        username=(TextView)findViewById(R.id.username);
        like= (ImageButton) findViewById(R.id.like);
        unlike=(ImageButton)findViewById(R.id.unlike);
        createdAt=(TextView)findViewById(R.id.created_at);
        description=(TextView)findViewById(R.id.description);
        commentrv= (RecyclerView) findViewById(R.id.commentrv);
        commentrv.setItemAnimator(new DefaultItemAnimator());
        commentrv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentlist=new ArrayList<>();
        commentAdapter=new CommentRecyclerViewAdapter(commentlist,ProjectDetail.this,commentrv);
        commentrv.setAdapter(commentAdapter);
        commentAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                commentlist.add(null);
                commentAdapter.notifyItemInserted(commentlist.size() - 1);
                if(!lastpage)
                    ++pagenumber;
                getComments();

            }
        });

        sendbutton= (ImageButton) findViewById(R.id.commentsendbutton);
        l= (CoordinatorLayout) findViewById(R.id.detailact);
        comment=(EditText) findViewById(R.id.commentcontent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i=getIntent();
        p= realm.where(Project.class).equalTo("id",getIntent().getIntExtra("projectid",0)).findFirst();
        resInent.putExtra("delete",i.getBooleanExtra("delete",false));
        resInent.putExtra("projectid",i.getIntExtra("projectid",0));

            if (i.getBooleanExtra("myproject", false) && Prefs.getInt("userprofileid", 0) == p.getUser().getId()) {
                interestedlayout = (LinearLayout) findViewById(R.id.interestedlayout);
                interestedlayout.setVisibility(View.GONE);
            } else {
                like.setTag(false);
                unlike.setTag(false);
                if (p.getStatus() != null && p.getStatus() != null && p.getStatus() != null && p.getStatus() == 3) {
                    like.setImageResource(R.drawable.ic_like_active);
                    like.setTag(true);
                } else if (p.getStatus() != null && p.getStatus() == 1) {
                    unlike.setImageResource(R.drawable.ic_unlike_active);
                    unlike.setTag(true);
                }
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if ((!(Boolean) view.getTag())) {
                            final boolean temp1 = (boolean) like.getTag();
                            final boolean temp2 = (boolean) unlike.getTag();
                            ((AppCompatImageButton) view).setImageResource(R.drawable.ic_like_active);
                            view.setTag(true);
                            ((AppCompatImageButton) unlike).setImageResource(R.drawable.ic_unlike_inactive);
                            unlike.setTag(false);

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(getString(R.string.base_url))
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
                            NewReview review = new NewReview();
                            review.setRating(3);

                            if (p.getStatus() != null && p.getStatus() == 0) {


                                Call<NewReview> call = findPeoplesAPI.postReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(3);
                                        realm.commitTransaction();

                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp1) {
                                            like.setImageResource(R.drawable.ic_like_active);
                                            like.setTag(true);
                                        } else {
                                            like.setImageResource(R.drawable.ic_like_inactive);
                                            like.setTag(false);
                                        }
                                        if (temp2) {
                                            unlike.setImageResource(R.drawable.ic_unlike_active);
                                            unlike.setTag(true);
                                        } else {
                                            unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                            unlike.setTag(false);
                                        }


                                    }
                                });
                            } else {

                                Call<NewReview> call = findPeoplesAPI.updatePostReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(3);
                                        realm.commitTransaction();
                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp1) {
                                            like.setImageResource(R.drawable.ic_like_active);
                                            like.setTag(true);
                                        } else {
                                            like.setImageResource(R.drawable.ic_like_inactive);
                                            like.setTag(false);
                                        }
                                        if (temp2) {
                                            unlike.setImageResource(R.drawable.ic_unlike_active);
                                            unlike.setTag(true);
                                        } else {
                                            unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                            unlike.setTag(false);
                                        }

                                    }
                                });
                            }


                        } else {
                            final boolean temp1 = (boolean) view.getTag();
                            ((AppCompatImageButton) view).setImageResource(R.drawable.ic_like_inactive);
                            view.setTag(false);
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(getString(R.string.base_url))
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
                            NewReview review = new NewReview();
                            review.setRating(2);

                            if (p.getStatus() != null && p.getStatus() == 0) {

                                Call<NewReview> call = findPeoplesAPI.postReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(2);
                                        realm.commitTransaction();

                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp1) {
                                            like.setImageResource(R.drawable.ic_like_active);
                                            like.setTag(true);
                                        } else {
                                            like.setImageResource(R.drawable.ic_like_inactive);
                                            like.setTag(false);
                                        }

                                    }
                                });
                            } else {

                                Call<NewReview> call = findPeoplesAPI.updatePostReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(2);
                                        realm.commitTransaction();

                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp1) {
                                            like.setImageResource(R.drawable.ic_like_active);
                                            like.setTag(true);
                                        } else {
                                            like.setImageResource(R.drawable.ic_like_inactive);
                                            like.setTag(false);
                                        }
                                    }
                                });
                            }
                        }


                    }
                });
                unlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((!(Boolean) view.getTag())) {
                            final boolean temp1 = (boolean) like.getTag();
                            final boolean temp2 = (boolean) unlike.getTag();
                            ((AppCompatImageButton) view).setImageResource(R.drawable.ic_unlike_active);
                            view.setTag(true);
                            like.setImageResource(R.drawable.ic_like_inactive);
                            like.setTag(false);

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(getString(R.string.base_url))
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
                            NewReview review = new NewReview();
                            review.setRating(1);
                            if (p.getStatus() != null && p.getStatus() == 0) {

                                Call<NewReview> call = findPeoplesAPI.postReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(1);
                                        realm.commitTransaction();

                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp1) {
                                            like.setImageResource(R.drawable.ic_like_active);
                                            like.setTag(true);
                                        } else {
                                            like.setImageResource(R.drawable.ic_like_inactive);
                                            like.setTag(false);
                                        }
                                        if (temp2) {
                                            unlike.setImageResource(R.drawable.ic_unlike_active);
                                            unlike.setTag(true);
                                        } else {
                                            unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                            unlike.setTag(false);
                                        }
                                    }
                                });
                            } else {

                                Call<NewReview> call = findPeoplesAPI.updatePostReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(1);
                                        realm.commitTransaction();
                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp1) {
                                            like.setImageResource(R.drawable.ic_like_active);
                                            like.setTag(true);
                                        } else {
                                            like.setImageResource(R.drawable.ic_like_inactive);
                                            like.setTag(false);
                                        }
                                        if (temp2) {
                                            unlike.setImageResource(R.drawable.ic_unlike_active);
                                            unlike.setTag(true);
                                        } else {
                                            unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                            unlike.setTag(false);
                                        }
                                    }
                                });
                            }


                        } else {
                            final boolean temp2 = (boolean) view.getTag();
                            ((AppCompatImageButton) view).setImageResource(R.drawable.ic_unlike_inactive);
                            view.setTag(false);
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(getString(R.string.base_url))
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);
                            NewReview review = new NewReview();
                            review.setRating(2);

                            if (p.getStatus() != null && p.getStatus() == 0) {

                                Call<NewReview> call = findPeoplesAPI.postReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(2);
                                        realm.commitTransaction();
                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp2) {
                                            unlike.setImageResource(R.drawable.ic_unlike_active);
                                            unlike.setTag(true);
                                        } else {
                                            unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                            unlike.setTag(false);
                                        }
                                    }
                                });
                            } else {

                                Call<NewReview> call = findPeoplesAPI.updatePostReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                                call.enqueue(new Callback<NewReview>() {
                                    @Override
                                    public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                        realm.beginTransaction();
                                        p.setStatus(2);
                                        realm.commitTransaction();
                                    }

                                    @Override
                                    public void onFailure(Call<NewReview> call, Throwable t) {
                                        Snackbar.make(findViewById(R.id.detailact), "No Connection", Snackbar.LENGTH_SHORT).show();
                                        if (temp2) {
                                            unlike.setImageResource(R.drawable.ic_unlike_active);
                                            unlike.setTag(true);
                                        } else {
                                            unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                            unlike.setTag(false);
                                        }
                                    }
                                });
                            }

                            pagenumber = 1;
                            getComments();
                        }


                    }
                });
            }

        getComments();
        title.setText(p.getTitle());
        if(p.getAnonymous()){
            username.setText("anonymous");
            Picasso.with(getApplicationContext())
                    .load(R.drawable.user_default)
                    .resize(70, 70)
                    .into(avatar);
        } else{
            Picasso.with(this)
                    .load(p.getUser().getAvatar())
                    .resize(70, 70)
                    .into(avatar);
            username.setText(p.getUser().getUser().getUsername());
        }
        if(!p.getAnonymous()) {
            userprofilepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), UserProfileActivity.class);
                    i.putExtra("userprofiledetail", true);
                    i.putExtra("userprofileid", p.getUser().getId());
                    startActivityForResult(i, 1000);
                }
            });
        }
        createdAt.setText(p.getCreated_at());
        description.setText(p.getDescription());
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(l.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                final String commentstring=comment.getText().toString();
                final Comment c=new Comment();
                c.setCreated_at("now");
                c.setContent(commentstring);
                c.setUser(realm.where(UserProfile.class).equalTo("id",Prefs.getInt("userprofileid",0)).findFirst());
                c.setProject(p);
                c.setId(0);
                commentlist.add(c);
                commentrv.scrollToPosition(commentlist.size()-1);
                commentAdapter.notifyDataSetChanged();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                NewComment review=new NewComment();
                review.setComment(commentstring);
                comment.setText("");
                Call<NewComment> call=findPeoplesAPI.postComment(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                call.enqueue(new Callback<NewComment>() {
                    @Override
                    public void onResponse(Call<NewComment> call, Response<NewComment> response) {
                        pagenumber=1;
                        getComments();
                    }

                    @Override
                    public void onFailure(Call<NewComment> call, Throwable t) {
                        commentlist.remove(c);
                        commentlist.remove(null);
                        commentAdapter.notifyDataSetChanged();
                        Snackbar.make(findViewById(R.id.detailact),"No Connection",Snackbar.LENGTH_SHORT).show();


                    }
                });
            }
        });
        }catch (Exception e){
            Toast.makeText(this,"No Internet",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void getComments() {
        final Project p1=p;
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<List<Comment>> call=findPeoplesAPI.getProjectComment(Prefs.getString("token",""),Integer.toString(p1.getId()),Integer.toString(pagenumber));
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                commentlist.remove(null);
                if(response.body().size()==0){
                    lastpage=true;
                }else {

                    if(pagenumber==1){

                        commentlist.clear();
                        commentlist.addAll(response.body());
                    }
                    else {
                        commentlist.addAll(response.body());

                    }
                }
                commentAdapter.notifyDataSetChanged();
                commentAdapter.setLoaded();

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            resInent.putExtra("position",getIntent().getIntExtra("position",0));
            setResult(101,resInent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        resInent.putExtra("position",getIntent().getIntExtra("position",0));
        setResult(101,resInent);
        finish();
    }
}
