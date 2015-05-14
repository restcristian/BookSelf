package wreckingball.bookself.booklist;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianrestituyo.mycameratest.MainActivity;
import com.example.cristianrestituyo.mycameratest.MainFragmentActivity;
import com.google.gson.stream.JsonReader;

import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import datamodel.BookLists;
import datamodel.Books;
import datamodel.*;
import wreckingball.bookself.UniversalJsonSerializer;
import wreckingball.bookself.httpTasks;

/**
 * Created by Cristian on 4/19/2015.
 */
public class BookShelfAdapter extends BaseAdapter {
    static class ViewHolder
    {
        TextView bookShelfTitleView;
        ImageView imageView;
        TextView whishList;
    }

    public  List<BookLists> BookShelfList = new ArrayList<BookLists>();

    public int getListId(String name)
    {
        for(BookLists booklists: BookShelfList)
        {
            if(booklists.getListName().equals(name))
            {
                return booklists.getListID();
            }
        }
        return -1;
    }

    public boolean hasName(String name)
    {
        for(BookLists booklists: BookShelfList)
        {
            if(booklists.getListName().equals(name))
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public int getCount() {
        return (BookShelfList == null)?0:BookShelfList.size();
    }

    @Override
    public BookLists getItem(int position) {
        return BookShelfList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        /*
            Cedeno was here.
            Change:
                Always sets the data, even when the convertView is not null.
         */

        /*if(convertView==null)
        {*/
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.activity_book_list_item, parent,false);
            holder = new ViewHolder();
            holder.bookShelfTitleView= (TextView)convertView.findViewById(R.id.BookShelfName);
            holder.bookShelfTitleView.setText(getItem(position).getListName());
            holder.imageView = (ImageView)convertView.findViewById(R.id.BookShelfIcon);
            holder.whishList = (TextView)convertView.findViewById(R.id.Whishlisttxt);

            holder.whishList.setText((getItem(position).getListType().equals("WL"))?"Wish List":"Regular List");

            holder.imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.bsicon));
            convertView.setTag(holder);
        /*}else
        {
            holder = (ViewHolder)convertView.getTag();
        }*/
        return convertView;
    }

    public void UpdateAdapter(ListView lv, String id)
    {
        BookShelfList = getBookShelfListListView(id);
        //this.notifyDataSetChanged();
        lv.invalidateViews();
        //lv.getAdapter().

    }
    public BookShelfAdapter(String id)
    {
        BookShelfList = getBookShelfListListView(id);
    }
    public static List<BookLists> getBookShelfListListView(String userid)
    {
        String json = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/get", UniversalJsonSerializer.convertToJason("UserId", userid));
        List<BookLists> bl = new ArrayList<BookLists>();


        Log.e("JSON POR AQUI:",json);
        if(!(json.equals("\"error\"")))
        {
             Gson gson = new Gson();
             TypeToken<List<BookLists>> type = new TypeToken<List<BookLists>>() {};
             try
             {
                bl = gson.fromJson(json,type.getType());
             }
             catch (JsonSyntaxException e)
            {
                e.printStackTrace();
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);

                 bl = gson.fromJson(reader, type.getType());
            }

            // return bl;
        }
        else
        {
            bl = new ArrayList<BookLists>();
        }

        //MainFragmentActivity.user.setBooklists(bl);
        return bl;
    }


}
