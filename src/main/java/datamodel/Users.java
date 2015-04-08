package datamodel;

import java.util.ArrayList;

/**
 * Created by Randy on 3/4/2015.
 */
public class Users {

    private int userId;
    private String userName;
    private String Password;
    private String email;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUser() {
        return userName;
    }

    public void setUser(String u) {
        userName = u;
    }

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        userId = id;
    }

}