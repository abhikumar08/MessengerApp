package com.usict.minorproject.messengerapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Abhi on 30-10-2015.
 */
public class GcmIntentService extends IntentService{
    public GcmIntentService(){
        super("GcmIntentService");
    }
    String fromName;
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras=intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType=gcm.getMessageType(intent);
        if(extras!=null && !extras.isEmpty()){
            if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());
                showToast(extras.getString("message"));
                //showToast(extras.getString("fromPhone"));
                Intent i = new Intent(this, ChatActivity.class);
                i.putExtra("toPhone",extras.getString("fromPhone"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, i,
                        PendingIntent.FLAG_ONE_SHOT);
                String fromPhone=extras.getString("fromPhone");

                Context context=getApplicationContext();
                RegistrationDbHelper dbHelper = new RegistrationDbHelper(context);
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("Select * from contacts where phone='" + fromPhone + "'", null);
                    cursor.moveToNext();
                    fromName = cursor.getString(2);
                }catch (Exception ex){
                    fromName=fromPhone;
                }
                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_dialog_email)
                        .setContentTitle(fromName)
                        .setContentText(extras.getString("message"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
