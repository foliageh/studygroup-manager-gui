package com.labproject.models;

public class User implements Model {
    private long id;
    private String username;
    private String password;

    public User() {}
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank())
            throw new RuntimeException("Invalid username.");
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new RuntimeException("Invalid password.");
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return username;
    }
}
