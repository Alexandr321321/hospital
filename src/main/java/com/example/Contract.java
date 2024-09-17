package com.example;

import java.sql.Date;
import java.sql.Timestamp;

public class Contract {

    private Integer id;

    private Integer cardid;

    private String personid;

    private String serviceid;

    private Integer contractnum;

    private Timestamp timestart;

    private Timestamp timefinish;

    private Double sum;

    private Integer room;

    private String paytype;

    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardid() {
        return cardid;
    }

    public void setCardid(Integer cardid) {
        this.cardid = cardid;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public Integer getContractnum() {
        return contractnum;
    }

    public void setContractnum(Integer contractnum) {
        this.contractnum = contractnum;
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

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Contract(Integer id, Integer cardid, String personid, String serviceid, Integer contractnum,
            Timestamp timestart, Timestamp timefinish, Double sum, Integer room, String paytype, Boolean status) {
        this.id = id;
        this.cardid = cardid;
        this.personid = personid;
        this.serviceid = serviceid;
        this.contractnum = contractnum;
        this.timestart = timestart;
        this.timefinish = timefinish;
        this.sum = sum;
        this.room = room;
        this.paytype = paytype;
        this.status = status;
    }

    public Contract() {
    }

}
