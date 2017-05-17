package me.sabarirangan.androidapps.findpeoples.model;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ubuntu on 25/12/16.
 */

/**
 * Model representing a basic, mentionable category.
 */
public class Category implements Mentionable {


    private final String mName;



    public Category(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    // --------------------------------------------------
    // Mentionable Implementation
    // --------------------------------------------------

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return mName;
            case PARTIAL:
            case NONE:
            default:
                return "";
        }
    }

    @Override
    public MentionDeleteStyle getDeleteStyle() {
        // Note: Cities do not support partial deletion
        // i.e. "San Francisco" -> DEL -> ""
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return mName.hashCode();
    }

    @Override
    public String getSuggestiblePrimaryText() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
    }

    public Category(Parcel in) {
        mName = in.readString();
    }

    public static final Creator<Category> CREATOR
            = new Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };



    public static class CategoryLoader{
        Context context;
        public CategoryLoader(Context context){
            this.context=context;
        }
        private List<Tags> gData=new ArrayList<>();
        public void getCategoryFromDjango(){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
            Call<List<Tags>> call=findPeoplesAPI.getTags(Prefs.getString("token",""));
            call.enqueue(new Callback<List<Tags>>(){
                @Override
                public void onResponse(Call<List<Tags>> call, Response<List<Tags>> response) {
                    gData.clear();
                    gData.addAll(response.body());
                    Log.d("sabarirangan",Integer.toString(gData.size()));
                }

                @Override
                public void onFailure(Call<List<Tags>> call, Throwable t) {
                    Log.d("sabarirangan",t.getMessage());
                }
            });
        }
        public List<Category> getSuggestions(String s){
            final String prefix = s.toLowerCase();

            final ArrayList<Category> suggestions=new ArrayList<>();
            Log.d("name", Integer.toString(gData.size()));
            for (Tags suggestion : gData) {
                String name = suggestion.getName().toLowerCase();
                Log.d("name",name);
                if (name.startsWith(prefix)) {
                    suggestions.add(new Category(suggestion.getName()));
                }
            }


            return suggestions;

        }
        public ArrayList<Tags> getTags(){
            return (ArrayList<Tags>)gData;
        }



    }
}