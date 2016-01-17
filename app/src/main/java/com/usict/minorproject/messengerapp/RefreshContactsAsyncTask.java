package com.usict.minorproject.messengerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.example.abhi.myapplication.backend.registration.Registration;
import com.example.abhi.myapplication.backend.registration.model.RegistrationRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Created by Abhi on 16-11-2015.
 */
public class RefreshContactsAsyncTask extends AsyncTask<Void,Void,Void> {
    private static Registration refreshService = null;
    Context context;
    RegistrationRecord friendsRecord;
    SQLiteDatabase db;
    ProgressDialog pd;
    public RefreshContactsAsyncTask(Context context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        pd=ProgressDialog.show(context,"Synchronizing contacts","Please wait while your contacts are being synchronized");
    }

    @Override
    protected Void doInBackground(Void... params) {

        if(refreshService==null){
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
            refreshService=builder.build();
        }
        RegistrationDbHelper dbHelper = new RegistrationDbHelper(context);
        dbHelper.deleteTable();

        db = dbHelper.getWritableDatabase();

        Cursor phones= context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        try {
            String temp=null;
            while (phones.moveToNext()) {
                String contactName = phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if (phoneNumber != null) {
                    phoneNumber = phoneNumber.replaceAll("[^A-Za-z0-9]", "");
                    int l = phoneNumber.length();
                    if (l >= 10) {
                        phoneNumber = phoneNumber.substring(l - 10);
                    }
                }

                if(!phoneNumber.equals(temp)){
                    friendsRecord = refreshService.findMyContacts(phoneNumber).execute();
                    if (friendsRecord != null) {
                        try {
                            dbHelper.insertContact(contactName, phoneNumber, null, null);
                            temp=phoneNumber;

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        pd.cancel();
    }
}
