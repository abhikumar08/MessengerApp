package com.usict.minorproject.messengerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText etName,etPhone,etMail;
    String name,phone,email;
    Button btSignUp;
    SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean firstTime=sharedPrefs.getBoolean("created",false);
        if(!firstTime){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            etName=(EditText)findViewById(R.id.etName);
            etPhone=(EditText)findViewById(R.id.etMobile);
            etMail=(EditText)findViewById(R.id.etMail);
            btSignUp=(Button)findViewById(R.id.signup_button);


            btSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = etName.getText().toString();
                    phone = etPhone.getText().toString();
                    email = etMail.getText().toString();

                    new GcmRegistrationAsyncTask(MainActivity.this).execute(name, phone, email);

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("created", true);
                    editor.commit();

                }
            });

        }else {
            startActivity(new Intent(MainActivity.this,ContactsActivity.class));
            finish();
        }




        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
