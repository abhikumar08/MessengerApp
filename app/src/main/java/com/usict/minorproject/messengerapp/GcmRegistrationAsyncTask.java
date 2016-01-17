package com.usict.minorproject.messengerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.abhi.myapplication.backend.registration.Registration;
import com.example.abhi.myapplication.backend.registration.model.RegistrationRecord;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Abhi on 30-10-2015.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<String,Void,String>{
    private static Registration regService = null;

    private GoogleCloudMessaging gcm;
    private Context context;
    SQLiteDatabase db;
    RegistrationRecord friendsRecord;
    SharedPreferences sp;
    ProgressDialog pd;
    public GcmRegistrationAsyncTask(Context context) {
        this.context=context;
    }

    private static final String SENDER_ID="474080370564";

    @Override
    protected void onPreExecute() {
        pd=ProgressDialog.show(context,"Building your Contact List","Please wait while we prepare your contact list");
    }

    @Override
    protected String doInBackground(String... params) {

        if(regService==null){
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(),null)
                    .setRootUrl("https://natural-chiller-111407.appspot.com/_ah/api/");
                    /*.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });*/
            regService=builder.build();
        }

        String msg="";
        try {
            if(gcm==null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId=gcm.register(SENDER_ID);
            msg="Device registered, registration id= "+regId;
            RegistrationRecord record = new RegistrationRecord();
            record.setRegId(regId);
            record.setName(params[0]);
            record.setPhone(params[1]);
            record.setEmail(params[2]);
            regService.register(record).execute();


            sp= PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("myRegID",regId);
            editor.putString("myPhone",params[1]);
            editor.commit();
            RegistrationDbHelper dbHelper =new RegistrationDbHelper(context);
            db=dbHelper.getWritableDatabase();

            Cursor phones= context.getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
            String temp=null;
            while (phones.moveToNext()){
                String contactName=phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber=phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(phoneNumber!=null){
                    phoneNumber=phoneNumber.replaceAll("[^A-Za-z0-9]","");
                    int l=phoneNumber.length();
                    if(l>=10){
                        phoneNumber=phoneNumber.substring(l-10);

                    }
                }
                if(!phoneNumber.equals(temp)){
                    friendsRecord=regService.findMyContacts(phoneNumber).execute();
                    if(friendsRecord!=null){
                        try{
                            dbHelper.insertContact(contactName,phoneNumber,null,null);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    temp=phoneNumber;
                }
            }

        }catch (IOException ex){
            ex.printStackTrace();
            msg="Error : "+ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        pd.cancel();
        Toast.makeText(context, "Registration Successful " + msg, Toast.LENGTH_LONG).show();
        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        context.startActivity(new Intent(context,ContactsActivity.class));

    }
}
