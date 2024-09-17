package com.example;

import java.sql.Date;

public class Message {

    private Integer id;

    private String personid;

    private String text;

    private Date date;

    public Message(Integer id, String personid, String text, Date date) {
        this.id = id;
        this.personid = personid;
        this.text = text;
        this.date = date;
    }

    public Message() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
