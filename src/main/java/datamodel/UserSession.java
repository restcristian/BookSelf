package datamodel;

/**
 * Created by CristianRestituyo on 4/7/2015.
 */
public class UserSession {
    private int UserSessionID;
    private int UserID;
    private int GUID;

    public int getUserSessionID() {
        return UserSessionID;
    }

    public void setUserSessionID(int userSessionID) {
        UserSessionID = userSessionID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getGUID() {
        return GUID;
    }

    public void setGUID(int GUID) {
        this.GUID = GUID;
    }
}
