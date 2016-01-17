package com.usict.minorproject.messengerapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    String myRegId;
    String toPhone;
    String myPhone;
    EditText chatBox;
    Button chatButton;
    ListView messagesContainer;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;


    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle=getIntent().getExtras();

        toPhone=bundle.getString("toPhone");
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myPhone=sp.getString("myPhone", null);

        chatBox=(EditText)findViewById(R.id.message_EditText);
        chatButton=(Button)findViewById(R.id.button_chat);
        messagesContainer=(ListView)findViewById(R.id.messagesContainer);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        chatHistory = new ArrayList<ChatMessage>();
        ChatMessage msg= new ChatMessage();
        msg.setIsMe(false);
        msg.setMessage("hey");
        chatHistory.add(msg);

        adapter= new ChatAdapter(new ArrayList<ChatMessage>(),ChatActivity.this);
        messagesContainer.setAdapter(adapter);


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= chatBox.getText().toString();
                new SendMessageAsyncTask().execute(myPhone, toPhone, message);

                ChatMessage chatMessage=new ChatMessage();
                chatMessage.setMessage(message);
                chatMessage.setIsMe(true);
                chatBox.setText(null);
                displayMessage(chatMessage);
            }
        });

    }
    public void displayMessage(ChatMessage message){
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }
    public void scroll(){
        messagesContainer.setSelection(messagesContainer.getCount()-1);
    }

}
