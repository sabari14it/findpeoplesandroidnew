package me.sabarirangan.apps.findpeoples.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pixplicity.easyprefs.library.Prefs;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Prefs.getBoolean("login",true)){
            Prefs.putString("token", "Token 0dad288a61e9af0ef1f6861f87d980fe36da5798");
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

        }else {
            Intent intent = new Intent(SplashActivity.this, GoogleSigninActivity.class);
            startActivity(intent);
        }

    }


}
