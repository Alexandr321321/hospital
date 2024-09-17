package com.example;

import java.util.Date;

public class Person {

    // Идентификатор пользователя
    private Integer id;

    // Логин пользователя
    private String login;

    // Пароль пользователя
    private String password;

    private String email;

    private Date birthday;

    private String birthdayPlace;

    private String gender;

    private String role;

    private String special;

    private Long inn;

    private Long snils;

    private Boolean status;

    private String secondname;

    private String name;

    private String family;

    private String phone;

    private Long passseries;

    private Long passnum;

    private Date passdateissue;

    private String passissueplace;

    private Integer passdepartmentcode;

    public Person(Integer id, String login, String password, String email, Date birthday, String birthdayPlace,
            String gender, String role, String special, Long inn, Long snils, Boolean status, String secondname,
            String name, String family, String phone, Long passseries, Long passnum, Date passdateissue,
            String passissueplace, Integer passdepartmentcode) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.birthdayPlace = birthdayPlace;
        this.gender = gender;
        this.role = role;
        this.special = special;
        this.inn = inn;
        this.snils = snils;
        this.status = status;
        this.secondname = secondname;
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.passseries = passseries;
        this.passnum = passnum;
        this.passdateissue = passdateissue;
        this.passissueplace = passissueplace;
        this.passdepartmentcode = passdepartmentcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayPlace() {
        return birthdayPlace;
    }

    public void setBirthdayPlace(String birthdayPlace) {
        this.birthdayPlace = birthdayPlace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public Long getInn() {
        return inn;
    }

    public void setInn(Long inn) {
        this.inn = inn;
    }

    public Long getSnils() {
        return snils;
    }

    public void setSnils(Long snils) {
        this.snils = snils;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Long getPassseries() {
        return passseries;
    }

    public void setPassseries(Long passseries) {
        this.passseries = passseries;
    }

    public Long getPassnum() {
        return passnum;
    }

    public void setPassnum(Long passnum) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
