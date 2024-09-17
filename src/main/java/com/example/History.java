package com.example;

import java.sql.Date;
import java.sql.Timestamp;

public class History {

    private Integer id;

    private String type;

    private String tablename;

    private String field;

    private String previousvalue;

    private String newvalue;

    private String personid;

    private Timestamp datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getPreviousvalue() {
        return previousvalue;
    }

    public void setPreviousvalue(String previousvalue) {
        this.previousvalue = previousvalue;
    }

    public String getNewvalue() {
        return newvalue;
    }

    public void setNewvalue(String newvalue) {
        this.newvalue = newvalue;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public History(Integer id, String type, String tablename, String field, String previousvalue, String newvalue,
            String personid, Timestamp datetime) {
        this.id = id;
        this.type = type;
        this.tablename = tablename;
        this.field = field;
        this.previousvalue = previousvalue;
        this.newvalue = newvalue;
        this.personid = personid;
        this.datetime = datetime;
    }

    public History() {
    }

}
