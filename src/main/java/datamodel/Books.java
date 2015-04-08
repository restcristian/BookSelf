package datamodel;

/**
 * Created by Randy on 3/4/2015.
 */
public class Books {


    private int BookID;
    private String ISBN10;
    private String ISBN13;
    private String title;
    private String AuthorName;
    private int AuthorID;
    private String ImgURI;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    private String Category;

    public int getBookID()
    {
        return BookID;
    }
    public void setID(int d)
    {
        BookID = d;
    }
    public int getAuthorID()
    {
        return AuthorID;
    }

    public void setAuthorID(int i)
    {
        AuthorID = i;
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
        return title;
    }

    public void setTitle(String t)
    {
        title = t;
    }

    public String getAuthor()
    {
        return AuthorName;
    }

    public void setAuthor(String author)
    {
        AuthorName = author;
    }

    public String getImg()
    {
        return ImgURI;
    }

    public void setImg(String img)
    {
        ImgURI = img;
    }

}
