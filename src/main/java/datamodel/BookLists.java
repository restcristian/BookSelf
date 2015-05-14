package datamodel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by CristianRestituyo on 4/7/2015.
 */
public class BookLists {


    private int ListId;
    private List<Books> booklist;
    private String ListName;
    private String ListType;

    public String getListName() {
        return ListName;
    }

    public int getListID() {
        return ListId;
    }

    public void setListID(int listID) {
        this.ListId = listID;
    }

    public void setListName(String listName) {
        ListName = listName;
    }

    public String getListType() {
        return ListType;
    }

    public void setListType(String listType) {
        ListType = listType;
    }

    public List<Books> getBooklist() {
        return booklist;
    }

    public void setBooklist(List<Books> booklist) {
        this.booklist = booklist;
    }

    public static BookLists Parse(String json){
        BookLists bl;
        Gson gson = new Gson();
        TypeToken<BookLists> type = new TypeToken<BookLists>() {};
        bl = gson.fromJson(json,type.getType());
        return bl;
    }
}
