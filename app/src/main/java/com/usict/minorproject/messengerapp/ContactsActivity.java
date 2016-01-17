package com.usict.minorproject.messengerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {
    ListView contactList;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayList<String> contactNames;
    ArrayList<String> contactPhones;
    ArrayList<String> regId;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactList=(ListView)findViewById(R.id.contactListView);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        RegistrationDbHelper dbHelper=new RegistrationDbHelper(getApplicationContext());
        db=dbHelper.getWritableDatabase();
        contactNames=new ArrayList<String>();
        contactPhones=new ArrayList<String>();
        regId=new ArrayList<String>();
        cursor=db.rawQuery("SELECT * FROM contacts",null);

        while ((cursor.moveToNext())){

            contactNames.add(cursor.getString(2));
            contactPhones.add(cursor.getString(4));

        }

        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,contactNames);
        contactList.setAdapter(adapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ContactsActivity.this,ChatActivity.class);
                String toPhone=contactPhones.get(position);
                intent.putExtra("toPhone",toPhone);
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
                new RefreshContactsAsyncTask(ContactsActivity.this).execute();
                cursor=db.rawQuery("SELECT * FROM contacts",null);
                contactNames.clear();
                contactPhones.clear();
                while ((cursor.moveToNext())){
                    contactNames.add(cursor.getString(2));
                    contactPhones.add(cursor.getString(4));
                }
                adapter.notifyDataSetChanged();

            }
        });

    }


}
