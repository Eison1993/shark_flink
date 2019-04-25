package com._51doit.pojo;


public class User {

    private String cookieid;

    private String account;

    private String email;

    private String phoneNbr;

    private String birthday;

    private String isRegistered;

    private String isLogin;

    private String addr;

    private String gender;

    private Phone phone;

    private Location loc;

    private App app;

    private String sessionId;


    public String getCookieid() {
        return cookieid;
    }

    public void setCookieid(String cookieid) {
        this.cookieid = cookieid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "User{" +
                "cookieid='" + cookieid + '\'' +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", phoneNbr='" + phoneNbr + '\'' +
                ", birthday='" + birthday + '\'' +
                ", isRegistered='" + isRegistered + '\'' +
                ", isLogin='" + isLogin + '\'' +
                ", addr='" + addr + '\'' +
                ", gender='" + gender + '\'' +
                ", phone=" + phone +
                ", loc=" + loc +
                ", app=" + app +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
