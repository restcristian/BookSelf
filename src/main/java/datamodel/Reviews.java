package datamodel;

/**
 * Created by Randy on 3/4/2015.
 */
public class Reviews {

    private int userId;
    private String Description;
    private int BookId;
    private int reviewId;
    private String reviewDate;

    public void setUserId(int user)
    {
        userId = user;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setDescription(String desc)
    {
        Description = desc;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setBookId(int b)
    {
        BookId = b;
    }

    public int getBookId()
    {
        return BookId;
    }

    public void setReviewId(int id)
    {
        reviewId = id;
    }

    public int getReviewId()
    {
        return reviewId;
    }

    public void setReviewDate(String da)
    {
        reviewDate = da;
    }

    public String getReviewDate()
    {
        return reviewDate;
    }

}
