package datamodel;

/**
 * Created by Randy on 3/4/2015.
 */
public class Reviews {

    private Users user;
    private String Description;
    private int reviewId;
    private String reviewDate;

    public void setUserId(Users u)
    {
        user = u;
    }

    public Users getUserId()
    {
        return user;
    }

    public void setDescription(String desc)
    {
        Description = desc;
    }

    public String getDescription()
    {
        return Description;
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
