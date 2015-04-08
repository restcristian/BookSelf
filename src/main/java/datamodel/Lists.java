package datamodel;

/**
 * Created by CristianRestituyo on 4/7/2015.
 */
public class Lists {
    public int getListID() {
        return ListID;
    }

    public void setListID(int listID) {
        ListID = listID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public boolean isListType() {
        return ListType;
    }

    public void setListType(boolean listType) {
        ListType = listType;
    }

    public String getListName() {
        return ListName;
    }

    public void setListName(String listName) {
        ListName = listName;
    }

    private int ListID;
    private int UserID;
    private boolean ListType;
    private String ListName;


}
