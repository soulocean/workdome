package com.example.soulocean1.workdome;

import cn.bmob.v3.BmobUser;

/**
 * Created by 海洋 on 2018/8/24.
 */

public class MyUser extends BmobUser {

    private Boolean sex;
    private String nick;
    private Integer age;

    private String user,passw,StudentNumber;

    public String getUser() {
        return this.user;
    }
    public String getPassw() {
        return this.passw;
    }
    public String getStuNumber() {
        return this.StudentNumber;
    }

    public void setUser(String User) {
        this.user = User;
    }
    public void setPassw(String Passw) {
        this.passw = Passw;
    }
    public void setStuNumber(String StuNumber) {
        this.StudentNumber = StuNumber;
    }


    public boolean getSex() {
        return this.sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}