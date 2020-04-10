package com.fund.iam.model;

public class UserInfo {

    private final int id;
    private final String email;
    private final String user_name;
    private final String nick_name;
    private final String phone;
    private final int age;
    private final int gender;



    public UserInfo(int id, String email, String user_name, String nick_name, String phone, int age, int gender) {
        this.id = id;
        this.email = email;
        this.user_name = user_name;
        this.nick_name = nick_name;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }
}

