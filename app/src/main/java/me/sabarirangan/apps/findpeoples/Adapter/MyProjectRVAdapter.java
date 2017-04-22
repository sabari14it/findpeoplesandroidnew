package me.sabarirangan.apps.findpeoples.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.sabarirangan.apps.findpeoples.R;
import me.sabarirangan.apps.findpeoples.model.Project;

/**
 * Created by ubuntu on 18/1/17.
 */

public class MyProjectRVAdapter extends RecyclerView.Adapter<MyProjectRVAdapter.ViewHolder> {
    private List<Project> list;


    public MyProjectRVAdapter(List<Project> l){
        list=l;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Project p=list.get(position);
        holder.title.setText(p.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_projects,parent,false);
        return new MyProjectRVAdapter.ViewHolder(v);
    }
}
