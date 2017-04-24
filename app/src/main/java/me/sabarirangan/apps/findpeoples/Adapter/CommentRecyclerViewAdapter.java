package me.sabarirangan.apps.findpeoples.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.model.Comment;
import me.sabarirangan.apps.findpeoples.model.Review;


/**
 * Created by ubuntu on 25/12/16.
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    List<Comment> list;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView comment;
        RoundedImageView avatar;
        TextView createdAt;
        ViewHolder(View view){
            super(view);
            username=(TextView)view.findViewById(R.id.username);
            comment=(TextView)view.findViewById(R.id.comment);
            avatar=(RoundedImageView)view.findViewById(R.id.avatar);
            createdAt=(TextView)view.findViewById(R.id.created_at);

        }

    }
    public CommentRecyclerViewAdapter(List<Comment> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment=list.get(position);
        holder.username.setText(comment.getReview().getUser().getUser().getUsername());
        holder.comment.setText(comment.getContent());
        holder.createdAt.setText(comment.getCreated_at());
        Picasso.with(context)
                .load(comment.getReview().getUser().getAvatar())
                .resize(30, 30)
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}