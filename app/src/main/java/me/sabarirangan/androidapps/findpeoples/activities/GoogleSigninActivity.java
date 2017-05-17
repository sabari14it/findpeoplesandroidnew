package me.sabarirangan.androidapps.findpeoples.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.extras.FindPeoplesAPI;
import me.sabarirangan.androidapps.findpeoples.model.Result;
import me.sabarirangan.androidapps.findpeoples.model.Token;
import me.sabarirangan.androidapps.findpeoples.model.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleSigninActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private Realm realm;
    public static final String TAG = "GoogleSigninActivity";
    private static final int RC_GET_AUTH_CODE = 9003;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);
        realm=Realm.getDefaultInstance();
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        validateServerClientID();
        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void getAuthCode() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }


    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "revokeAccess:onResult:" + status);
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_AUTH_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "onActivityResult:GET_AUTH_CODE:success:" + result.getStatus().isSuccess());

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String authCode = acct.getServerAuthCode();
                if(!validateEmail(acct.getEmail())){
                    Toast.makeText(this,"use kct email id",Toast.LENGTH_SHORT).show();
                    return;
                }
                Prefs.putString("name", acct.getGivenName());
                Prefs.putString("rollno",acct.getFamilyName());
                Prefs.putString("email",acct.getEmail());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
                Token t=new Token();
                t.setCode(authCode);
                //Log.d("oauth",t.getCode());
                Call<Token> call=findPeoplesAPI.loginUser(t);// t is the auth code
                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if(response.code()==400){
                            Log.d("oauth","sabari");
                        }else {
                            String token = "Token " + response.body().getCode();
                            Prefs.putString("token", token);
                            Prefs.putBoolean("login",true);
                            Log.d("sabari",token);
                            getUserProfile();



                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                    }
                });


            }
        }
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
                startActivity(new Intent(GoogleSigninActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateEmail(String email) {
        String[] words=email.split("@");//splits the string based on whitespace
        if(words[1].equals("kct.ac.in"))
            return true;
        else
            return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        getAuthCode();
    }

    private void getUserProfile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FindPeoplesAPI findPeoplesAPI=retrofit.create(FindPeoplesAPI.class);
        Call<UserProfile> call=findPeoplesAPI.getUserProfile(Prefs.getString("token","ssdd"),"0");
        com.beloo.widget.chipslayoutmanager.util.log.Log.d("sabari",Prefs.getString("token","ssdd"));
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                Prefs.putInt("userprofileid",response.body().getId());
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                if(Prefs.getBoolean("isfcmtoken",false))
                    sendFCMtoken();
                else {
                    startActivity(new Intent(GoogleSigninActivity.this, MainActivity.class));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });

    }


}
