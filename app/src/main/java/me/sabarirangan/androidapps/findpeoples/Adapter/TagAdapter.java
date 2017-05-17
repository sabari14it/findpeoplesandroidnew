package me.sabarirangan.androidapps.findpeoples.Adapter;

import android.app.Activity;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.SearchProjectActivity;
import me.sabarirangan.androidapps.findpeoples.model.Tags;

/**
 * Created by ubuntu on 19/1/17.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    private List<Tags> taglist;
    Context context;
    Fragment fragment;
    public TagAdapter(List<Tags> t,Context context){
        taglist=t;
        this.context=context;
    }
    public TagAdapter(List<Tags> t,Fragment fragment){
        taglist=t;
        this.context=fragment.getActivity();
        this.fragment=fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private RelativeLayout r;

        public ViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tagname);
            r=(RelativeLayout)itemView.findViewById(R.id.tagcontainer);
        }
    }

    @Override
    public int getItemCount() {
        return taglist.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Tags s=taglist.get(position);
        holder.name.setText(s.getName());
        holder.r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), SearchProjectActivity.class);
                i.putExtra("query",holder.name.getText());
                if(context instanceof SearchProjectActivity){
                    ((Activity)context).startActivityForResult(i,100);
                    ((SearchProjectActivity) context).finish();
                }else{
                    if(fragment!=null){
                        fragment.startActivityForResult(i,100);
                    }else {
                        ((Activity)context).startActivityForResult(i,100);
                    }
                }
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_layout,parent,false);

        return new ViewHolder(v);
    }
}
