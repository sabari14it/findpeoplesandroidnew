package me.sabarirangan.androidapps.findpeoples.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import me.sabarirangan.androidapps.findpeoples.R;
import me.sabarirangan.androidapps.findpeoples.model.Project;

public class NewPost extends AppCompatActivity {

    EditText title;
    EditText description;
    //EditText mentorName;
    Realm realm;
    Button submit;
    Project p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        realm=Realm.getDefaultInstance();
        title= (EditText) findViewById(R.id.project_title);
        description= (EditText) findViewById(R.id.project_description);
        Intent i=getIntent();
        if(i.getBooleanExtra("editpost",false)){
            p=realm.where(Project.class).equalTo("id",i.getIntExtra("projectid",1)).findFirst();
            title.setText(p.getTitle());
            description.setText(p.getDescription());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.new_project_menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next:
                Intent i=new Intent(this,NewPostTagsActivity.class);
                i.setAction("posttags");
                if(getIntent().getBooleanExtra("editpost",false)){
                    i.putExtra("projectid",p.getId());
                    i.putExtra("editpost",true);
                }

                i.putExtra("title",title.getText().toString());
                i.putExtra("description",description.getText().toString());
                startActivityForResult(i,1);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent i=new Intent();
        setResult(1,i);
        if(resultCode==1441)
            finish();
    }


}

