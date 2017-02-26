package models;

import java.util.Calendar;

public class User {

    private final Long id;
    private final String login;
    private final String email;
    private final String password;
    private final Calendar createdAt;

    public User(Long id, String login, String email, String password, Calendar createdAt) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }
}
