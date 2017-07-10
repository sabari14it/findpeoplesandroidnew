package me.sabarirangan.androidapps.findpeoples.Adapter;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import io.realm.Realm;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.NewPost;
import me.sabarirangan.androidapps.findpeoples.activities.ProjectDetail;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.extras.OnLoadMoreListener;
import me.sabarirangan.androidapps.findpeoples.model.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ubuntu on 18/1/17.
 */

public class MyProjectRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Project> list;
    Realm realm;
    private android.support.v4.app.Fragment fragment;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MyProjectRVAdapter(List<Project> l, Fragment fragment,RecyclerView recyclerView)
    {
        list=l;
        this.fragment=fragment;
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
        realm=Realm.getDefaultInstance();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageButton edit;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title);
            edit=(ImageButton)itemView.findViewById(R.id.editpost);
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
                    R.layout.my_projects, parent, false);

            vh = new MyProjectRVAdapter.ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new MyProjectRVAdapter.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyProjectRVAdapter.ViewHolder) {
            final Project p = list.get(position);
            if (p.getUser().getId() != Prefs.getInt("userprofileid", 0)) {
                ((ViewHolder)holder).edit.setVisibility(View.GONE);
            }
            ((ViewHolder)holder).title.setText(p.getTitle());
            ((ViewHolder)holder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Project p1 = list.get(position);
                    Intent i = new Intent(v.getContext(), ProjectDetail.class);
                    i.putExtra("projectid", p1.getId());
                    i.putExtra("myproject", true);
                    fragment.getActivity().startActivityForResult(i, 101);
                }
            });
            ((ViewHolder)holder).edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = v;
                    final Project p1 = list.get(position);
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.postment, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.editpost) {
                                Intent i = new Intent(view.getContext(), NewPost.class);
                                i.putExtra("projectid", p1.getId());
                                i.putExtra("editpost", true);
                                fragment.startActivityForResult(i, 1);
                                return true;
                            } else {


                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(view.getContext().getString(R.string.base_url))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                FindPeoplesAPI findPeoplesAPI = retrofit.create(FindPeoplesAPI.class);

                                Call<Response<Void>> call = findPeoplesAPI.deleteProject(Prefs.getString("token", "abcd"), Integer.toString(p1.getId()));
                                final Project p=realm.where(Project.class).equalTo("id",p1.getId()).findFirst();


                                call.enqueue(new Callback<Response<Void>>() {
                                    @Override
                                    public void onResponse(Call<Response<Void>> call, Response<Response<Void>> response) {
                                        realm.beginTransaction();
                                        p.deleteFromRealm();
                                        realm.commitTransaction();
                                        list.remove(p1);
                                        notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onFailure(Call<Response<Void>> call, Throwable t) {
                                        Snackbar.make(fragment.getActivity().findViewById(R.id.profilefragmentlay),"No Internet",Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                                return true;

                            }
                        }
                    });
                    popupMenu.show();

                }
            });
        }else{
            ((MyProjectRVAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public void setLoaded() {
        loading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
