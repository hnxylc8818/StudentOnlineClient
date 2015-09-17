package com.stuonline.entity;

/**
 * Muser entity. @author MyEclipse Persistence Tools
 */

public class Muser implements java.io.Serializable {

    // Fields

    private Integer uid;
    private String account;
    private String pwd;
    private String email;
    private String nick;
    private Integer roleId;
    private String school;
    private String area;
    private String department;
    private String UClass;
    private String uyear;
    private String photoUrl;
    private String uname;
    private Integer gender;
    private Integer authentication;
    private String bundle;


    // Constructors

    /**
     * default constructor
     */
    public Muser() {
    }

    /**
     * minimal constructor
     */
    public Muser(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    /**
     * full constructor
     */
    public Muser(String account, String pwd, String email, String nick,
                 Integer roleId, String school, String area, String department,
                 String UClass, String uyear, String photoUrl, String uname,
                 Integer gender, Integer authentication) {
        this.account = account;
        this.pwd = pwd;
        this.email = email;
        this.nick = nick;
        this.roleId = roleId;
        this.school = school;
        this.area = area;
        this.department = department;
        this.UClass = UClass;
        this.uyear = uyear;
        this.photoUrl = photoUrl;
        this.uname = uname;
        this.gender = gender;
        this.authentication = authentication;
    }

    // Property accessors

    public Integer getUid() {
        return this.uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUClass() {
        return this.UClass;
    }

    public void setUClass(String UClass) {
        this.UClass = UClass;
    }

    public String getUyear() {
        return this.uyear;
    }

    public void setUyear(String uyear) {
        this.uyear = uyear;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUname() {
        return this.uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getGender() {
        return this.gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(Integer authentication) {
        this.authentication = authentication;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
}