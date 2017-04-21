package me.sabarirangan.apps.findpeoples.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.model.Project;
import me.sabarirangan.apps.findpeoples.model.Tags;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ubuntu on 3/9/16.
 */
public class PostRecyclerViewAdapter extends UltimateViewAdapter {

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    //    private List<Project> content_list;
//    private Context context;
//    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        SpacingItemDecoration space;
//        private int originalHeight = 0;
//        private boolean isViewExpanded = false;
//        private TextView title,description,user,type,readmore,comment;
//        private ImageButton like,unlike;
//        private View descriptionh;
//        private View view;
//        private RecyclerView tagrv;
//        private RoundedImageView avatar;
//
//        public PostViewHolder(View view) {
//            super(view);
//            this.view=view;
//            title= (TextView) view.findViewById(R.id.title);
//            description=(TextView)view.findViewById(R.id.description);
//            user=(TextView)view.findViewById(R.id.username);
//            like= (ImageButton) view.findViewById(R.id.like);
//            unlike=(ImageButton)view.findViewById(R.id.unlike);
//            //comment= (TextView) view.findViewById(R.id.comment);
//            readmore=(TextView)view.findViewById(R.id.read_full);
//            descriptionh= view.findViewById(R.id.descriptionholder);
//            tagrv= (RecyclerView) view.findViewById(R.id.tagrv);
//            space=new SpacingItemDecoration(view.getContext().getResources().getDimensionPixelOffset(R.dimen.item_space),view.getContext().getResources().getDimensionPixelOffset(R.dimen.item_space));
//            tagrv.addItemDecoration(space);
//            ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(view.getContext())
//                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                    .build();
//            tagrv.setLayoutManager(spanLayoutManager);
//            tagrv.getRecycledViewPool().setMaxRecycledViews(0, 10);
//            tagrv.getRecycledViewPool().setMaxRecycledViews(1, 10);
//            avatar= (RoundedImageView) view.findViewById(R.id.avatar);
//            //view.setOnClickListener(this);
//
//            // Initialize other views, like TextView, ImageView, etc. here
//
//            // If isViewExpanded == false then set the visibility
//            // of whatever will be in the expanded to GONE
//
////            if (isViewExpanded == false) {
////                // Set Views to View.GONE and .setEnabled(false)
////                descriptionh.setVisibility(View.GONE);
////                descriptionh.setEnabled(false);
////            }
//
//        }
//
//        @Override
//        public void onClick(final View v) {
//            // If the originalHeight is 0 then find the height of the View being used
//            // This would be the height of the cardview
//            if (originalHeight == 0) {
//                originalHeight = view.getHeight();
//            }
//
//            // Declare a ValueAnimator object
//            ValueAnimator valueAnimator;
//            if (!isViewExpanded) {
//                descriptionh.setVisibility(View.VISIBLE);
//                descriptionh.setEnabled(true);
//                isViewExpanded = true;
//                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight +200); // These values in this method can be changed to expand however much you like
//            } else {
//                isViewExpanded = false;
//                valueAnimator = ValueAnimator.ofInt(originalHeight + 200, originalHeight);
//
//                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out
//
//                a.setDuration(200);
//                // Set a listener to the animation and configure onAnimationEnd
//                a.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        descriptionh.setVisibility(View.INVISIBLE);
//                        descriptionh.setEnabled(false);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//                // Set the animation on the custom view
//                descriptionh.startAnimation(a);
//            }
//            valueAnimator.setDuration(200);
//            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    Integer value = (Integer) animation.getAnimatedValue();
//                    view.getLayoutParams().height = value.intValue();
//                    view.requestLayout();
//                }
//            });
//            valueAnimator.start();
//        }
//    }
//
//
//    /*public class PostViewHolder extends RecyclerView.ViewHolder{
//        TextView title,description,user,type;
//        AppCompatImageButton like,unlike,comment,readmore;
//        PostViewHolder(View view){
//            super(view);
//            title= (TextView) view.findViewById(R.id.title);
//            description=(TextView)view.findViewById(R.id.description);
//            user=(TextView)view.findViewById(R.id.username);
//            like= (AppCompatImageButton) view.findViewById(R.id.like);
//            unlike=(AppCompatImageButton)view.findViewById(R.id.unlike);
//            comment=(AppCompatImageButton)view.findViewById(R.id.comment);
//            readmore=(AppCompatImageButton)view.findViewById(R.id.read_full);
//
//        }
//
//    }*/
//
//    public PostRecyclerViewAdapter(List<Project> list,Context context){
//        content_list=list;
//        this.context=context;
//    }
//   @Override
//    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
//       return new PostViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final PostViewHolder holder, int position) {
//
//        final Project p=content_list.get(position);
//        holder.title.setText(p.getTitle());
//        String s;
//        if(p.getDescription().length()>=50)
//        {
//            s=p.getDescription().substring(0,50);
//        }
//        else
//        s=p.getDescription();
//        s=s+"....";
//        holder.description.setText(s);
//        holder.user.setText(p.getUser().getUser().getUsername());
//        holder.like.setTag(false);
//        TagAdapter adapter=new TagAdapter(p.getTags());
//        Picasso.with(context)
//                .load(p.getUser().getAvatar())
//                .resize(70, 70)
//                .into(holder.avatar);
//
//
//        holder.tagrv.setAdapter(adapter);


//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if( (!(Boolean)view.getTag()) ){
//                    ((AppCompatImageButton)view).setImageResource(R.drawable.ic_thumb_up_black_18dp);
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(Constants.BASE_URL)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//                    FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//                    Review review=new Review();
//                    review.setComment("");
//                    review.setRating(4);
//                    review.setProject(p.getLocalId());
//                    Call<ReviewGet> call=findPeoplesAPI.postReview(Prefs.getString("token",""),review);
//                    call.enqueue(new Callback<ReviewGet>() {
//                        @Override
//                        public void onResponse(Call<ReviewGet> call, Response<ReviewGet> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ReviewGet> call, Throwable t) {
//                            //Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    view.setTag(true);
//                }
//                else {
//                    ((AppCompatImageButton) view).setImageResource(R.drawable.ic_thumb_up_white_18dp);
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(Constants.BASE_URL)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//                    FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
//                    Review review=new Review();
//                    review.setComment("");
//                    review.setRating(3);
//                    review.setProject(p.getLocalId());
//                    Call<ReviewGet> call=findPeoplesAPI.postReview(Prefs.getString("token",""),review);
//                    call.enqueue(new Callback<ReviewGet>() {
//                        @Override
//                        public void onResponse(Call<ReviewGet> call, Response<ReviewGet> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ReviewGet> call, Throwable t) {
//                            //Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    view.setTag(false);
//                }
//
//
//            }
//        });
//        holder.unlike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //unlike
//            }
//        });
////        holder.comment.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                //comment logic
////            }
////        });
//        holder.readmore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i=new Intent(view.getContext(), ProjectDetail.class);
//                Parcelable wrapped = Parcels.wrap(p);
//                i.putExtra("project",wrapped);
//                view.getContext().startActivity(i);
//            }
//        });

    }
    @Override
    public int getItemCount() {
        return content_list.size();
    }
}
