package me.sabarirangan.apps.findpeoples.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import me.sabarirangan.apps.findpeoples.R;

public class NewPost extends AppCompatActivity {

    EditText title;
    EditText description;
    //EditText mentorName;

    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));

        title= (EditText) findViewById(R.id.project_title);
        description= (EditText) findViewById(R.id.project_description);
        //mentorName=(EditText)findViewById(R.id.project_mentor_name);
        //submit= (Button) findViewById(R.id.submit);


//        submit.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MyProjectBrief projectBrief=new MyProjectBrief();
//                projectBrief.setTitle(title.getText().toString());
//                projectBrief.setTitle(description.getText().toString());
//                projectBrief.setMentorName(mentorName.getText().toString());
//                projectBrief.save();
//                int s=MyProjectBrief.getAll().size();
//
//            }
//        });
//



        /*Bundle bundle=getIntent().getExtras();
        if(bundle.getString("type").equals("P")) {
            setContentView(R.layout.activity_new_project);
        }
        else if(bundle.getString("type").equals("N")){
            setContentView(R.layout.activity_new_news);
        }
        else if(bundle.getString("type").equals("C")){
            setContentView(R.layout.activity_new_course);
        }
        else if(bundle.getString("type").equals("E")){
            setContentView(R.layout.activity_new_event);
        }
        else if(bundle.getString("type").equals("T")){
            setContentView(R.layout.activity_new_team);
        }*/
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
                i.putExtra("title",title.getText().toString());
                i.putExtra("description",description.getText().toString());
                startActivity(i);
        }
        return true;
    }

}

