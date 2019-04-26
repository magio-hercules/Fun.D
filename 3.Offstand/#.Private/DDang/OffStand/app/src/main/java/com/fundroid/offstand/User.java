package com.fundroid.offstand;

public class User {
    private String name;
    private int avatar;

    public User(String name, int i){
        this.name = name;
        this.avatar = i;
    }

    public User(){ this("Hi", 0); }
    public User(String name){ this(name, 0); }

    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }

    public void setAvatar(int avatar){ this.avatar = avatar; }
    public int getAvatar(){ return avatar; }
}
