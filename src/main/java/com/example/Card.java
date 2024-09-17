package com.example;

import java.sql.Date;

public class Card {

    private Integer id;

    private Integer cardnumber;

    private String secondname;
    
    private String firstname;

    private String family;

    private String phone;

    private Date birthday;

    private String gender;

    private Integer passseries;

    private Integer passnum;

    private Date passdateissue;

    private String passissueplace;

    private Integer passdepartmentcode;

    private String svidseries;

    private Integer svidnum;

    private Date sviddateissue;

    private String svidissueplace;

    private Integer inn;

    private Integer snils;

    private Integer enp;

    private String jobplace;

    private String profession;

    private String enterprice;

    private String education;

    private String educationplace;

    private String alergen;

    private String secondnamezag;

    private String firstnamezag;

    private Integer passserieszag;

    private Integer passnumzag;

    private Date passdateissuezag;

    private String birthplace;
    

    public Card(Integer id, Integer cardnumber, String secondname, String firstname, String family, String phone,
            Date birthday, String gender, Integer passseries, Integer passnum, Date passdateissue,
            String passissueplace, Integer passdepartmentcode, String svidseries, Integer svidnum, Date sviddateissue,
            String svidissueplace, Integer inn, Integer snils, Integer enp, String jobplace, String profession,
            String enterprice, String education, String educationplace, String alergen, String secondnamezag,
            String firstnamezag, Integer passserieszag, Integer passnumzag, Date passdateissuezag, String birthplace) {
        this.id = id;
        this.cardnumber = cardnumber;
        this.secondname = secondname;
        this.firstname = firstname;
        this.family = family;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.passseries = passseries;
        this.passnum = passnum;
        this.passdateissue = passdateissue;
        this.passissueplace = passissueplace;
        this.passdepartmentcode = passdepartmentcode;
        this.svidseries = svidseries;
        this.svidnum = svidnum;
        this.sviddateissue = sviddateissue;
        this.svidissueplace = svidissueplace;
        this.inn = inn;
        this.snils = snils;
        this.enp = enp;
        this.jobplace = jobplace;
        this.profession = profession;
        this.enterprice = enterprice;
        this.education = education;
        this.educationplace = educationplace;
        this.alergen = alergen;
        this.secondnamezag = secondnamezag;
        this.firstnamezag = firstnamezag;
        this.passserieszag = passserieszag;
        this.passnumzag = passnumzag;
        this.passdateissuezag = passdateissuezag;
        this.birthplace = birthplace;
    }

    public Card() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(Integer cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getPassseries() {
        return passseries;
    }

    public void setPassseries(Integer passseries) {
        this.passseries = passseries;
    }

    public Integer getPassnum() {
        return passnum;
    }

    public void setPassnum(Integer passnum) {
        this.passnum = passnum;
    }

    public Date getPassdateissue() {
        return passdateissue;
    }

    public void setPassdateissue(Date passdateissue) {
        this.passdateissue = passdateissue;
    }

    public String getPassissueplace() {
        return passissueplace;
    }

    public void setPassissueplace(String passissueplace) {
        this.passissueplace = passissueplace;
    }

    public Integer getPassdepartmentcode() {
        return passdepartmentcode;
    }

    public void setPassdepartmentcode(Integer passdepartmentcode) {
        this.passdepartmentcode = passdepartmentcode;
    }

    public String getSvidseries() {
        return svidseries;
    }

    public void setSvidseries(String svidseries) {
        this.svidseries = svidseries;
    }

    public Integer getSvidnum() {
        return svidnum;
    }

    public void setSvidnum(Integer svidnum) {
        this.svidnum = svidnum;
    }

    public Date getSviddateissue() {
        return sviddateissue;
    }

    public void setSviddateissue(Date sviddateissue) {
        this.sviddateissue = sviddateissue;
    }

    public String getSvidissueplace() {
        return svidissueplace;
    }

    public void setSvidissueplace(String svidissueplace) {
        this.svidissueplace = svidissueplace;
    }

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }

    public Integer getSnils() {
        return snils;
    }

    public void setSnils(Integer snils) {
        this.snils = snils;
    }

    public Integer getEnp() {
        return enp;
    }

    public void setEnp(Integer enp) {
        this.enp = enp;
    }

    public String getJobplace() {
        return jobplace;
    }

    public void setJobplace(String jobplace) {
        this.jobplace = jobplace;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEnterprice() {
        return enterprice;
    }

    public void setEnterprice(String enterprice) {
        this.enterprice = enterprice;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationplace() {
        return educationplace;
    }

    public void setEducationplace(String educationplace) {
        this.educationplace = educationplace;
    }

    public String getAlergen() {
        return alergen;
    }

    public void setAlergen(String alergen) {
        this.alergen = alergen;
    }

    public String getSecondnamezag() {
        return secondnamezag;
    }

    public void setSecondnamezag(String secondnamezag) {
        this.secondnamezag = secondnamezag;
    }

    public String getFirstnamezag() {
        return firstnamezag;
    }

    public void setFirstnamezag(String firstnamezag) {
        this.firstnamezag = firstnamezag;
    }

    public Integer getPassserieszag() {
        return passserieszag;
    }

    public void setPassserieszag(Integer passserieszag) {
        this.passserieszag = passserieszag;
    }

    public Date getPassdateissuezag() {
        return passdateissuezag;
    }

    public void setPassdateissuezag(Date passdateissuezag) {
        this.passdateissuezag = passdateissuezag;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public Integer getPassnumzag() {
        return passnumzag;
    }

    public void setPassnumzag(Integer passnumzag) {
        this.passnumzag = passnumzag;
    }


}
