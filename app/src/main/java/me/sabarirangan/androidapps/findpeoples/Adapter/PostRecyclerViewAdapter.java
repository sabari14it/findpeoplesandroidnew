package me.sabarirangan.androidapps.findpeoples.Adapter;


import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.util.List;


import io.realm.Realm;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.ProjectDetail;
import me.sabarirangan.androidapps.findpeoples.activities.UserProfileActivity;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.NewReview;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ubuntu on 3/9/16.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Project> content_list;
    private Context context;
    private Fragment fragment;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Realm realm;
    public PostRecyclerViewAdapter(List<Project> list, RecyclerView recyclerView, final Context context){
        content_list=list;
        this.context=context;
        this.fragment=null;
        realm=Realm.getDefaultInstance();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
//                            if (dy > 0) {
//                                ((MainActivity)context).findViewById(R.id.newpost).setVisibility(View.GONE);
//                            } else {
//                                ((MainActivity)context).findViewById(R.id.newpost).setVisibility(View.VISIBLE);
//                            }

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }


    public PostRecyclerViewAdapter(List<Project> list, RecyclerView recyclerView, final Fragment fragment){
        content_list=list;
        this.context=fragment.getContext();
        this.fragment=fragment;
        realm=Realm.getDefaultInstance();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
//                            if (dy > 0) {
//                                ((MainActivity)context).findViewById(R.id.newpost).setVisibility(View.GONE);
//                            } else {
//                                ((MainActivity)context).findViewById(R.id.newpost).setVisibility(View.VISIBLE);
//                            }

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        SpacingItemDecoration space;
        private int originalHeight = 0;
        private boolean isViewExpanded = false;
        private TextView title,description,user,type,readmore,comment,created_at;
        private ImageButton like,unlike;
        private View descriptionh;
        private View view;
        private RecyclerView tagrv;
        private RoundedImageView avatar;
        private RelativeLayout deslayout;
        private LinearLayout userprofilepsot;

        public PostViewHolder(View view) {
            super(view);
            this.view=view;
            title= (TextView) view.findViewById(R.id.title);
            description=(TextView)view.findViewById(R.id.description);
            user=(TextView)view.findViewById(R.id.username);
            like= (ImageButton) view.findViewById(R.id.like);
            unlike=(ImageButton)view.findViewById(R.id.unlike);
            readmore=(TextView)view.findViewById(R.id.read_full);
            descriptionh= view.findViewById(R.id.descriptionholder);
            tagrv= (RecyclerView) view.findViewById(R.id.tagrv);
            created_at=(TextView)view.findViewById(R.id.created_at);
            space=new SpacingItemDecoration(view.getContext().getResources().getDimensionPixelOffset(R.dimen.item_space),view.getContext().getResources().getDimensionPixelOffset(R.dimen.item_space));
            tagrv.addItemDecoration(space);
            ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(view.getContext())
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .build();
            tagrv.setLayoutManager(spanLayoutManager);
            tagrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
            tagrv.getRecycledViewPool().setMaxRecycledViews(1, 10);
            avatar= (RoundedImageView) view.findViewById(R.id.avatar);
            deslayout=(RelativeLayout)view.findViewById(R.id.descriptionlayout);
            userprofilepsot=(LinearLayout)view.findViewById(R.id.userprofilepost);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.post, parent, false);

            vh = new PostViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof PostViewHolder) {

            final Project p = content_list.get(position);
            ((PostViewHolder)holder).created_at.setText(p.getCreated_at());
            ((PostViewHolder)holder).title.setText(p.getTitle());
            String s;
            if (p.getDescription().length() >= 135) {
                s = p.getDescription().substring(0, 135);
                s = s + "....";
            } else {
                s = p.getDescription();
                ((PostViewHolder)holder).readmore.setVisibility(View.GONE);
            }
            ((PostViewHolder)holder).description.setText(s);
            if(p.getAnonymous()){
                ((PostViewHolder)holder).user.setText("anonymous");
                Picasso.with(context)
                        .load(R.drawable.user_default)
                        .resize(70, 70)
                        .into(((PostViewHolder)holder).avatar);
            } else{
                Picasso.with(context)
                        .load(p.getUser().getAvatar())
                        .resize(70, 70)
                        .into(((PostViewHolder)holder).avatar);
                ((PostViewHolder)holder).user.setText(p.getUser().getUser().getUsername());
            }

            ((PostViewHolder)holder).like.setTag(false);
            ((PostViewHolder)holder).unlike.setTag(false);
            TagAdapter adapter;
            if(fragment==null)
                adapter = new TagAdapter(p.getTags(),context);
            else
                adapter = new TagAdapter(p.getTags(),fragment);



            ((PostViewHolder)holder).tagrv.setAdapter(adapter);
            if(p.getStatus()!=null&&p.getStatus()==3) {
                ((PostViewHolder) holder).like.setImageResource(R.drawable.ic_like_active);
                ((PostViewHolder) holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                ((PostViewHolder) holder).like.setTag(true);
                ((PostViewHolder) holder).unlike.setTag(false);
            }
            else if(p.getStatus()!=null&&p.getStatus()==1) {
                ((PostViewHolder) holder).unlike.setImageResource(R.drawable.ic_unlike_active);
                ((PostViewHolder) holder).like.setImageResource(R.drawable.ic_like_inactive);
                ((PostViewHolder) holder).unlike.setTag(true);
                ((PostViewHolder) holder).like.setTag(false);
            }
            ((PostViewHolder)holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( (!(Boolean)view.getTag()) ){
                        final boolean temp1= (boolean) ((PostViewHolder)holder).like.getTag();
                        final boolean temp2=(boolean) ((PostViewHolder)holder).unlike.getTag();
                        ((AppCompatImageButton) view).setImageResource(R.drawable.ic_like_active);
                        view.setTag(true);
                        ((AppCompatImageButton) ((PostViewHolder)holder).unlike).setImageResource(R.drawable.ic_unlike_inactive);
                        ((PostViewHolder)holder).unlike.setTag(false);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setRating(3);
                        if(p.getStatus()!=null&&p.getStatus()==0) {
                            Call<NewReview> call = findPeoplesAPI.postReview(Prefs.getString("token", ""), Integer.toString(p.getId()), review);
                            call.enqueue(new Callback<NewReview>() {
                                @Override
                                public void onResponse(Call<NewReview> call, Response<NewReview> response) {

                                }

                                @Override
                                public void onFailure(Call<NewReview> call, Throwable t) {
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp1){
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_active);
                                        ((PostViewHolder)holder).like.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                                        ((PostViewHolder)holder).like.setTag(false);
                                    }
                                    if(temp2){
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_active);
                                        ((PostViewHolder)holder).unlike.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                        ((PostViewHolder)holder).unlike.setTag(false);
                                    }
                                }
                            });
                        }else{
                            Call<NewReview> call=findPeoplesAPI.updatePostReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                            call.enqueue(new Callback<NewReview>() {
                                @Override
                                public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                    realm.beginTransaction();
                                    p.setStatus(3);
                                    realm.commitTransaction();
                                }

                                @Override
                                public void onFailure(Call<NewReview> call, Throwable t) {
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp1){
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_active);
                                        ((PostViewHolder)holder).like.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                                        ((PostViewHolder)holder).like.setTag(false);
                                    }
                                    if(temp2){
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_active);
                                        ((PostViewHolder)holder).unlike.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                        ((PostViewHolder)holder).unlike.setTag(false);
                                    }

                                }
                            });
                        }
                    }
                    else {
                        final boolean temp1= (boolean) view.getTag();
                        ((AppCompatImageButton) view).setImageResource(R.drawable.ic_like_inactive);
                        view.setTag(false);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setRating(2);
                        if(p.getStatus()!=null&&p.getStatus()==0) {
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
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp1){
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_active);
                                        ((PostViewHolder)holder).like.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                                        ((PostViewHolder)holder).like.setTag(false);
                                    }
                                }
                            });
                        }else{
                            Call<NewReview> call=findPeoplesAPI.updatePostReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                            call.enqueue(new Callback<NewReview>() {
                                @Override
                                public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                    realm.beginTransaction();
                                    p.setStatus(2);
                                    realm.commitTransaction();

                                }

                                @Override
                                public void onFailure(Call<NewReview> call, Throwable t) {
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp1){
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_active);
                                        ((PostViewHolder)holder).like.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                                        ((PostViewHolder)holder).like.setTag(false);
                                    }
                                }
                            });
                        }
                    }


                }
            });
            ((PostViewHolder)holder).unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( (!(Boolean)view.getTag()) ){
                        final boolean temp1= (boolean) ((PostViewHolder)holder).like.getTag();
                        final boolean temp2=(boolean) ((PostViewHolder)holder).unlike.getTag();
                        ((AppCompatImageButton) view).setImageResource(R.drawable.ic_unlike_active);
                        view.setTag(true);
                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                        ((PostViewHolder)holder).like.setTag(false);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setRating(1);
                        if(p.getStatus()!=null&&p.getStatus()==0) {
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
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp1){
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_active);
                                        ((PostViewHolder)holder).like.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                                        ((PostViewHolder)holder).like.setTag(false);
                                    }
                                    if(temp2){
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_active);
                                        ((PostViewHolder)holder).unlike.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                        ((PostViewHolder)holder).unlike.setTag(false);
                                    }
                                }
                            });
                        }else{
                            Call<NewReview> call=findPeoplesAPI.updatePostReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                            call.enqueue(new Callback<NewReview>() {
                                @Override
                                public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                    realm.beginTransaction();
                                    p.setStatus(1);
                                    realm.commitTransaction();
                                }

                                @Override
                                public void onFailure(Call<NewReview> call, Throwable t) {
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp1){
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_active);
                                        ((PostViewHolder)holder). like.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).like.setImageResource(R.drawable.ic_like_inactive);
                                        ((PostViewHolder)holder).like.setTag(false);
                                    }
                                    if(temp2){
                                        ((PostViewHolder)holder). unlike.setImageResource(R.drawable.ic_unlike_active);
                                        ((PostViewHolder)holder).unlike.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                        ((PostViewHolder)holder).unlike.setTag(false);
                                    }
                                }
                            });
                        }
                    }
                    else {
                        final boolean temp2= (boolean) view.getTag();
                        ((AppCompatImageButton) view).setImageResource(R.drawable.ic_unlike_inactive);
                        view.setTag(false);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setRating(2);
                        if(p.getStatus()!=null&&p.getStatus()==0) {
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
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp2){
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_active);
                                        ((PostViewHolder)holder).unlike.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                        ((PostViewHolder)holder).unlike.setTag(false);
                                    }
                                }
                            });
                        }else{
                            Call<NewReview> call=findPeoplesAPI.updatePostReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                            call.enqueue(new Callback<NewReview>() {
                                @Override
                                public void onResponse(Call<NewReview> call, Response<NewReview> response) {
                                    realm.beginTransaction();
                                    p.setStatus(2);
                                    realm.commitTransaction();

                                }

                                @Override
                                public void onFailure(Call<NewReview> call, Throwable t) {
                                    Snackbar.make(((Activity)context).findViewById(R.id.swipe_refresh_layout),"No Connection",Snackbar.LENGTH_SHORT).show();
                                    if(temp2){
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_active);
                                        ((PostViewHolder)holder).unlike.setTag(true);
                                    }else{
                                        ((PostViewHolder)holder).unlike.setImageResource(R.drawable.ic_unlike_inactive);
                                        ((PostViewHolder)holder).unlike.setTag(false);
                                    }
                                }
                            });
                        }
                    }


                }
            });
            ((PostViewHolder)holder).deslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(v.getContext(), ProjectDetail.class);
                    Project p1=realm.where(Project.class).equalTo("id",p.getId()).findFirst();
                    if(p1==null) {
                        i.putExtra("delete",true);
                    }
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(p);
                    realm.commitTransaction();
                    i.putExtra("position",position);
                    i.putExtra("projectid",p.getId());
                    if(fragment==null)
                        ((Activity)context).startActivityForResult(i,101);
                    else
                        fragment.startActivityForResult(i,101);
                }
            });
            ((PostViewHolder)holder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(v.getContext(), ProjectDetail.class);
                    Project p1=realm.where(Project.class).equalTo("id",p.getId()).findFirst();
                    if(p1==null) {
                        i.putExtra("delete",true);
                    }
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(p);
                    realm.commitTransaction();
                    i.putExtra("position",position);
                    i.putExtra("projectid",p.getId());
                    if(fragment==null)
                        ((Activity)context).startActivityForResult(i,101);
                    else
                        fragment.startActivityForResult(i,101);
                }
            });
            if(!p.getAnonymous()) {
                ((PostViewHolder) holder).userprofilepsot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), UserProfileActivity.class);
                        i.putExtra("userprofiledetail", true);
                        i.putExtra("userprofileid", p.getUser().getId());
                        if (fragment != null)
                            fragment.startActivityForResult(i, 1000);
                        else
                            ((Activity) context).startActivityForResult(i, 1000);
                    }
                });
            }
        }else{
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }



    @Override
    public int getItemViewType(int position) {
        return content_list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }





    public void setLoaded() {
        loading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return content_list.size();
    }
}
