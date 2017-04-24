package me.sabarirangan.apps.findpeoples.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.activities.ProjectDetail;
import me.sabarirangan.apps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.apps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.apps.findpeoples.model.NewReview;
import me.sabarirangan.apps.findpeoples.model.Project;
import me.sabarirangan.apps.findpeoples.model.Result;
import me.sabarirangan.apps.findpeoples.model.Tags;
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
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        SpacingItemDecoration space;
        private int originalHeight = 0;
        private boolean isViewExpanded = false;
        private TextView title,description,user,type,readmore,comment;
        private ImageButton like,unlike;
        private View descriptionh;
        private View view;
        private RecyclerView tagrv;
        private RoundedImageView avatar;

        public PostViewHolder(View view) {
            super(view);
            this.view=view;
            title= (TextView) view.findViewById(R.id.title);
            description=(TextView)view.findViewById(R.id.description);
            user=(TextView)view.findViewById(R.id.username);
            like= (ImageButton) view.findViewById(R.id.like);
            unlike=(ImageButton)view.findViewById(R.id.unlike);
            //comment= (TextView) view.findViewById(R.id.comment);
            readmore=(TextView)view.findViewById(R.id.read_full);
            descriptionh= view.findViewById(R.id.descriptionholder);
            tagrv= (RecyclerView) view.findViewById(R.id.tagrv);
            space=new SpacingItemDecoration(view.getContext().getResources().getDimensionPixelOffset(R.dimen.item_space),view.getContext().getResources().getDimensionPixelOffset(R.dimen.item_space));
            tagrv.addItemDecoration(space);
            ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(view.getContext())
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .build();
            tagrv.setLayoutManager(spanLayoutManager);
            tagrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
            tagrv.getRecycledViewPool().setMaxRecycledViews(1, 10);
            avatar= (RoundedImageView) view.findViewById(R.id.avatar);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }



    public PostRecyclerViewAdapter(List<Project> list,RecyclerView recyclerView,Context context){
        content_list=list;
        this.context=context;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

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

    @Override
    public int getItemViewType(int position) {
        return content_list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
            vh=new PostViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof PostViewHolder) {

            final Project p = content_list.get(position);
            ((PostViewHolder)holder).title.setText(p.getTitle());
            String s;
            if (p.getDescription().length() >= 50) {
                s = p.getDescription().substring(0, 50);
            } else
                s = p.getDescription();
            s = s + "....";
            ((PostViewHolder)holder).description.setText(s);
            ((PostViewHolder)holder).user.setText(p.getUser().getUser().getUsername());
            ((PostViewHolder)holder).like.setTag(false);
            ((PostViewHolder)holder).unlike.setTag(false);
            TagAdapter adapter = new TagAdapter(p.getTags());
            Picasso.with(context)
                    .load(p.getUser().getAvatar())
                    .resize(70, 70)
                    .into(((PostViewHolder)holder).avatar);


            ((PostViewHolder)holder).tagrv.setAdapter(adapter);
            ((PostViewHolder)holder).readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i=new Intent(view.getContext(), ProjectDetail.class);
                    i.putExtra("projectid",p.getId());
                    view.getContext().startActivity(i);
                }
            });
            ((PostViewHolder)holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( (!(Boolean)view.getTag()) ){
                        ((AppCompatImageButton)view).setImageResource(R.drawable.ic_thumb_up_black_18dp);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setComment("");
                        review.setRating(4);
                        Call<Result> call=findPeoplesAPI.postReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                //Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                            }
                        });
                        ((PostViewHolder)holder).unlike.setTag(false);
                        ((AppCompatImageButton)((PostViewHolder)holder).unlike).setImageResource(R.drawable.ic_thumb_down_white_18dp);
                        view.setTag(true);
                    }
                    else {
                        ((AppCompatImageButton)view).setImageResource(R.drawable.ic_thumb_up_white_18dp);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setComment("");
                        review.setRating(3);
                        Call<Result> call=findPeoplesAPI.postReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                //Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                            }
                        });
                        view.setTag(false);
                    }


                }
            });
            ((PostViewHolder)holder).unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( (!(Boolean)view.getTag()) ){
                        ((AppCompatImageButton)view).setImageResource(R.drawable.ic_thumb_down_black_18dp);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setComment("");
                        review.setRating(1);
                        Call<Result> call=findPeoplesAPI.postReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                //Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                            }
                        });
                        ((PostViewHolder)holder).like.setTag(false);
                        ((AppCompatImageButton)((PostViewHolder)holder).like).setImageResource(R.drawable.ic_thumb_up_white_18dp);
                        view.setTag(true);
                    }
                    else {
                        ((AppCompatImageButton)view).setImageResource(R.drawable.ic_thumb_down_white_18dp);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                        NewReview review=new NewReview();
                        review.setComment("");
                        review.setRating(3);
                        Call<Result> call=findPeoplesAPI.postReview(Prefs.getString("token",""),Integer.toString(p.getId()),review);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                //Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                            }
                        });
                        view.setTag(false);
                    }


                }
            });
        }else{
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }



//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //comment logic
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return content_list.size();
    }
}
