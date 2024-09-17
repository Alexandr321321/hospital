package com.example;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class Job {

    public Job(Integer id, String personid, Timestamp timestamp, Timestamp timestamp2, String special) {
        this.id = id;
        this.personid = personid;
        this.timestart = timestamp;
        this.timefinish = timestamp2;
        this.special = special;
    }

    public Job() {
    }

    private Integer id;

    private String personid;

    private Timestamp timestart;

    private Timestamp timefinish;

    private String special;

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
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

    public Timestamp getTimestart() {
        return timestart;
    }

    public void setTimestart(Timestamp timestart) {
        this.timestart = timestart;
    }

    public Timestamp getTimefinish() {
        return timefinish;
    }

    public void setTimefinish(Timestamp timefinish) {
        this.timefinish = timefinish;
    }
    

}
