package wreckingball.bookself.booklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;

import datamodel.*;


/**
 * Created by CristianRestituyo on 3/21/2015.
 */
public class dataAdapter extends BaseAdapter {

    List<Books> booksList = getDataForListView();
    @Override
    public int getCount()
    {
        return booksList.size();
    }
    @Override
    public Books getItem(int arg0)
    {
        return booksList.get(arg0);
    }
    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2)
    {
        if(arg1==null)
        {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(arg2.getContext());
            arg1 = inflater.inflate(R.layout.activity_list_item, arg2,false);
        }
        TextView bookTitle = (TextView)arg1.findViewById(R.id.BookTitle);

        Books book = booksList.get(arg0);
        bookTitle.setText(book.getTitle());

        return arg1;
    }

    public List<Books> getDataForListView()
    {
        List<Books> BookList = new ArrayList<Books>();
        String json = "{\"ISBN\":\"123456789\",\"title\":\"The Principles of Shuffling\",\"AuthorName\":\"Cristian Restituyo\"}";

        Gson gson = new GsonBuilder().create();
        Books book = gson.fromJson(json,Books.class);
       /*
        book.setISBN("123456789");
        book.setTitle("The Principles of Shuffling");
        book.setAuthor("Cristian Restituyo");
        */
        BookList.add(book);
        return BookList;
    }


}

