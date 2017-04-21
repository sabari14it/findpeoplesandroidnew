package me.sabarirangan.apps.findpeoples.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.model.Tags;

/**
 * Created by ubuntu on 19/1/17.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    private List<Tags> taglist;
    public TagAdapter(List<Tags> t){
        taglist=t;
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
//                Intent i=new Intent(view.getContext(), SearchProjectActivity.class);
//                i.putExtra("query",holder.name.getText());
//                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_layout,parent,false);

        return new ViewHolder(v);
    }
}
