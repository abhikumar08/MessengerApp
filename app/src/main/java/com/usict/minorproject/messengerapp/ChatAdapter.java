package com.usict.minorproject.messengerapp;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abhi on 25-11-2015.
 */
public class ChatAdapter extends BaseAdapter{
    private final List<ChatMessage> chatMessages;
    private Activity context;

    public ChatAdapter(List<ChatMessage> chatMessages, Activity context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(chatMessages!=null){
            return chatMessages.size();
        }else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if(chatMessages != null){
            return chatMessages.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage chatMessage=getItem(position);
        LayoutInflater vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView=vi.inflate(R.layout.list_item_chat_message,null);
            holder=createViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        boolean myMsg=chatMessage.isMe();
        setAlignment(holder,myMsg);
        holder.txtMessage.setText(chatMessage.getMessage());
        return convertView;
    }

    public  void add(ChatMessage message){
        chatMessages.add(message);
    }
    public void add(List<ChatMessage> messages){
        chatMessages.addAll(messages);
    }
    private void setAlignment(ViewHolder holder,boolean isme){
        if(!isme){
            holder.contentWithBg.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)holder.contentWithBg.getLayoutParams();
            layoutParams.gravity= Gravity.RIGHT;
            holder.contentWithBg.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

        }else {

            holder.contentWithBg.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBg.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBg.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

        }
    }
    private ViewHolder createViewHolder(View v){
        ViewHolder holder=new ViewHolder();
        holder.txtMessage=(TextView)v.findViewById(R.id.txtMessage);
        holder.content=(LinearLayout)v.findViewById(R.id.content);
        holder.contentWithBg=(LinearLayout)v.findViewById(R.id.contentWithBackground);
        return holder;

    }
    private static class ViewHolder {
        public TextView txtMessage;
        public LinearLayout content;
        public LinearLayout contentWithBg;
    }
}

