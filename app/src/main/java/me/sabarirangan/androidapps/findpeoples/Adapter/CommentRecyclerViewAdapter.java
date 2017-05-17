package me.sabarirangan.androidapps.findpeoples.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.UserProfileActivity;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Comment;


/**
 * Created by ubuntu on 25/12/16.
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Comment> list;
    Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public CommentRecyclerViewAdapter(List<Comment> list,Context context,RecyclerView recyclerView){
        this.list=list;
        this.context=context;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView comment;
        RoundedImageView avatar;
        TextView createdAt;
        LinearLayout userprofilepsot;
        ViewHolder(View view){
            super(view);
            userprofilepsot= (LinearLayout) view.findViewById(R.id.userprofilepost);
            username=(TextView)view.findViewById(R.id.username);
            comment=(TextView)view.findViewById(R.id.comment);
            avatar=(RoundedImageView)view.findViewById(R.id.avatar);
            createdAt=(TextView)view.findViewById(R.id.created_at);

        }

    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
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
                    R.layout.comment, parent, false);

            vh = new CommentRecyclerViewAdapter.ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new CommentRecyclerViewAdapter.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CommentRecyclerViewAdapter.ViewHolder) {
            final Comment comment = list.get(position);

            ((CommentRecyclerViewAdapter.ViewHolder)holder).username.setText(comment.getUser().getUser().getUsername());
            ((CommentRecyclerViewAdapter.ViewHolder)holder).comment.setText(comment.getContent());
            ((CommentRecyclerViewAdapter.ViewHolder)holder).createdAt.setText(comment.getCreated_at());
            Picasso.with(context)
                    .load(comment.getUser().getAvatar())
                    .resize(70, 70)
                    .into(((CommentRecyclerViewAdapter.ViewHolder)holder).avatar);
            ((CommentRecyclerViewAdapter.ViewHolder)holder).userprofilepsot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), UserProfileActivity.class);
                    i.putExtra("userprofiledetail", true);
                    i.putExtra("userprofileid", comment.getUser().getId());
                    ((Activity)context).startActivityForResult(i, 1000);
                }
            });
        }else{
            ((CommentRecyclerViewAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    public void setLoaded() {
        loading = false;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}