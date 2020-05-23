package com.stan.zhangli;

public class User {
    public User() {
    }

    public User(Long id, String user) {
        this.id = id;
        this.user = user;
    }

    private Long id;
    private String user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}