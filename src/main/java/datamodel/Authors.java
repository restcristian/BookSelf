package datamodel;

/**
 * Created by Randy on 3/4/2015.
 */
public class Authors {

    private int AuthorId;
    private String firstName;
    private String lastName;


    public void setAuthorId(int id)
    {
        AuthorId = id;
    }

    public int getAuthorId()
    {
        return AuthorId;
    }

    public void setAuthorFName(String fname)
    {
        firstName = fname;
    }

    public String getAuthorFName()
    {
        return firstName;
    }

    public void setAuthorLName(String lName)
    {
        lastName = lName;
    }

    public String getAuthorLName()
    {
        return lastName;
    }
}
