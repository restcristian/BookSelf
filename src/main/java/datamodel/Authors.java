package datamodel;

/**
 * Created by Randy on 3/4/2015.
 */
public class Authors {

    private int AuthorId;
    private String AuthorFName;
    private String AuthorLName;

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
        AuthorFName = fname;
    }

    public String getAuthorFName()
    {
        return AuthorFName;
    }

    public void setAuthorLName(String lName)
    {
        AuthorLName = lName;
    }

    public String getAuthorLName()
    {
        return AuthorLName;
    }
}
