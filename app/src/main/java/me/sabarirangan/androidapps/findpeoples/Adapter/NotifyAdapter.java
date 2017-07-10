package me.sabarirangan.androidapps.findpeoples.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.ProjectDetail;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Comment;

/**
 * Created by ubuntu on 15/1/17.
 */

public class NotifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment> comments;
    Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    public NotifyAdapter(List<Comment> comments,Context context,RecyclerView recyclerView){
        this.context=context;
        this.comments= comments;
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
    class NotifyViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView content;
        LinearLayout linearLayout;
        public NotifyViewHolder(View itemView) {
            super(itemView);
            avatar= (ImageView) itemView.findViewById(R.id.avatar);
            content= (TextView) itemView.findViewById(R.id.content);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.notifycontiner);
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
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NotifyViewHolder) {
            final Comment comment = comments.get(position);
            Picasso.with(context)
                    .load(comment.getUser().getAvatar())
                    .resize(70, 70)
                    .into(((NotifyAdapter.NotifyViewHolder) holder).avatar);
            ((NotifyAdapter.NotifyViewHolder) holder).content.setText(comment.getUser().getUser().getUsername() + " wrote review on your post \""+comment.getProject().getTitle()+"\"");
            ((NotifyAdapter.NotifyViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), ProjectDetail.class);
                    i.putExtra("projectid", comment.getProject().getId());
                    view.getContext().startActivity(i);

                }
            });
        }else{
            try {
                ((CommentRecyclerViewAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }catch (Exception e){

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return comments.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.notify, parent, false);

            vh = new NotifyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

}
