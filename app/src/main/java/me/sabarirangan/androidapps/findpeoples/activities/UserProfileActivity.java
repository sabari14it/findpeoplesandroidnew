package me.sabarirangan.androidapps.findpeoples.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.fragments.ProfileFragment;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ProfileFragment profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        profile=(ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.profileFragment);
        profile.populateContent(getIntent().getIntExtra("userprofileid",1));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent();
            setResult(100,intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return true;
    }
}
