package datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 3/4/2015.
 */
public class Users implements Serializable {

    private String UserId;
    private String FirstName;
    private String LastName;
    private String Email;
    private String PW;
    private List<BookLists> booklists;

    public List<BookLists> getBooklists() {
        return booklists;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setBooklists(List<BookLists> booklists) {
        this.booklists = booklists;
    }

    public String getPassword() {
        return PW;
    }

    public void setPassword(String password) {
        PW = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }


    public String getId() {
        return UserId;
    }

    public void setId(String id) {
        UserId = id;
    }

}