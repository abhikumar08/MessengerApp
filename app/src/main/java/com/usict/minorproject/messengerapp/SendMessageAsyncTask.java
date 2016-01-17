package com.usict.minorproject.messengerapp;

import android.os.AsyncTask;

import com.example.abhi.myapplication.backend.messaging.Messaging;
import com.example.abhi.myapplication.backend.messaging.model.MessageRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by Abhi on 03-11-2015.
 */
public class SendMessageAsyncTask extends AsyncTask<String,Void,Void> {
    private static Messaging messagingService=null;
    @Override
    protected Void doInBackground(String... params) {

        if(messagingService==null){
            Messaging.Builder builder= new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(),null)
                    .setRootUrl("https://natural-chiller-111407.appspot.com/_ah/api/");
            messagingService=builder.build();

        }
        try{
            MessageRecord msg=new MessageRecord();
            msg.setMsgFromPhone(params[0]);
            msg.setMsgToPhone(params[1]);
            msg.setMessage(params[2]);

            messagingService.sendMessage(msg).execute();
        }catch (IOException ex){

        }
        return null;
    }
}
