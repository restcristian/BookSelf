package datamodel;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Randy on 3/4/2015.
 */
public class Books implements Serializable{


    private int BookId;
    private String ISBN10;
    private String ISBN13;
    private String Title;
    private List<Authors> author;
    private String Description;
    private String Cover;
    private String Category;
    private List<Reviews> reviews;

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    private byte[] coverImage; //Actual Image stored in its own member

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public List<Authors> getAuthors() {
        return author;
    }


    public void setAuthors(List<Authors> authors) {
        this.author = authors;
    }


    public int getBookID()
    {
        return BookId;
    }
    public void setID(int d)
    {
        BookId = d;
    }


    public String getISBN10() {
        return ISBN10;
    }

    public void setISBN10(String i) {
        ISBN10 = i;
    }

    public String getISBN13()
    {
        return ISBN13;
    }

    public void setISBN13(String i)
    {
        ISBN13 = i;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String t)
    {
        Title = t;
    }


    public String getCover()
    {
        return Cover;
    }

    public void setCover(String img)
    {
        Cover = img;
    }

    public static Books Parse(String json){
        Books bl;
        Gson gson = new Gson();
        TypeToken<Books> type = new TypeToken<Books>() {};
        bl = gson.fromJson(json,type.getType());
        return bl;
    }
}
