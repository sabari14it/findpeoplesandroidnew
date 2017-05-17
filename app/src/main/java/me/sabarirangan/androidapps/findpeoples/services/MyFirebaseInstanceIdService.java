package me.sabarirangan.androidapps.findpeoples.services;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pixplicity.easyprefs.library.Prefs;

import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.activities.GoogleSigninActivity;
import me.sabarirangan.androidapps.findpeoples.activities.MainActivity;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.model.Result;
import me.sabarirangan.androidapps.findpeoples.model.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sabari on 25/4/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        // Get updated InstanceID token.
        Prefs.putBoolean("isfcmtoken",true);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Prefs.putString("fcmtoken",refreshedToken);
        Log.d("firebasemy", "Refreshed token: " + refreshedToken);
        if(Prefs.getBoolean("login",false)){
            sendFCMtoken();
        }

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
    private void sendFCMtoken(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Token t=new Token();
        t.setCode(Prefs.getString("fcmtoken","abc"));
        Call<Result> call=findPeoplesAPI.sendFCMtoken(Prefs.getString("token","abcd"),t);// t is the auth code
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
            }
        });
    }
}
