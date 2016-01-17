package com.usict.minorproject.messengerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abhi on 10-11-2015.
 */
public class RegistrationDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="MessengerAppDb";
    private static final String CONTACTS_TABLE_NAME="contacts";
    private static final String CONTACTS_COLUMN_ID="_id";
    private static final String CONTACTS_COLUMN_REG_ID="regId";
    private static final String CONTACTS_COLUMN_NAME="name";
    private static final String CONTACTS_COLUMN_PHONE="phone";
    private static final String CONTACTS_COLUMN_EMAIL="email";
    private static final int DB_VERSION=1;

    public RegistrationDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CONTACTS_TABLE_NAME + " (" + CONTACTS_COLUMN_ID + " integer primary key autoincrement," +
                CONTACTS_COLUMN_REG_ID + " text," + CONTACTS_COLUMN_NAME + " text," + CONTACTS_COLUMN_EMAIL + " text,"
                + CONTACTS_COLUMN_PHONE + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(db);
    }
    public void deleteTable(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(db);
    }
    public boolean insertContact(String name,String phone,String email,String regId){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME,name);
        contentValues.put(CONTACTS_COLUMN_EMAIL,email);
        contentValues.put(CONTACTS_COLUMN_PHONE,phone);
        contentValues.put(CONTACTS_COLUMN_REG_ID,regId);
        db.insert(CONTACTS_TABLE_NAME,null,contentValues);
        return true;
    }
}
