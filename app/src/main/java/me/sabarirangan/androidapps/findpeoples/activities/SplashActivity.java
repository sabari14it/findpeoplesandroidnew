package me.sabarirangan.androidapps.findpeoples.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pixplicity.easyprefs.library.Prefs;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Prefs.getBoolean("login",false)){
            //Prefs.putString("token", "Token 33c33a80cff84cb41e66c392d50547ca82c497c4");
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

        }else {
            Intent intent = new Intent(SplashActivity.this, GoogleSigninActivity.class);
            startActivity(intent);
        }

    }


}
