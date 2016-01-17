package com.example.Abhi.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Abhi on 04-11-2015.
 */
@Entity
public class MessageRecord {
    @Id
    Long Id;

    private String msgFromPhone;
    private String msgToPhone;
    private String message;
    //private Date msgDate;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getMsgFromPhone() {
        return msgFromPhone;
    }

    public void setMsgFromPhone(String msgFromPhone) {
        this.msgFromPhone = msgFromPhone;
    }

    public String getMsgToPhone() {
        return msgToPhone;
    }

    public void setMsgToPhone(String msgToPhone) {
        this.msgToPhone = msgToPhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }*/
}
