package wreckingball.bookself.booklist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianrestituyo.mycameratest.MainFragmentActivity;
import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import datamodel.*;
import wreckingball.bookself.UniversalJsonSerializer;
import wreckingball.bookself.httpTasks;
import wreckingball.bookself.sessionManagement.UniversalJsonDeserializer;


/**
 * Created by CristianRestituyo on 3/21/2015.
 */
public class dataAdapter extends BaseAdapter {

    static class ViewHolder
    {
        TextView bookTitleView;
        ImageView imageView;
    }
    public List<Books> booksList = new ArrayList<Books>();

    public boolean hasISBN(String isbn){
        for (Books book : booksList) {
            if(book.getISBN10().equals(isbn) || book.getISBN13().equals(isbn))
                return true;
        }
        return false;
    }

    @Override
    public int getCount()
    {
        return (booksList == null)?0:booksList.size();
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
        ViewHolder holder;

        if(arg1==null)
        {
            LayoutInflater inflater = LayoutInflater.from(arg2.getContext());
            arg1 = inflater.inflate(R.layout.activity_list_item, arg2,false);
            holder = new ViewHolder();
            holder.bookTitleView = (TextView)arg1.findViewById(R.id.BookTitle);
            holder.imageView = (ImageView)arg1.findViewById(R.id.icon);
            arg1.setTag(holder);
        }else
        {
            holder = (ViewHolder)arg1.getTag();
        }

        final Books book = booksList.get(arg0);
        holder.bookTitleView.setText(book.getTitle());
        if(holder.imageView != null)
        {
            new ImageDownloaderTask(holder.imageView).execute(book.getCover());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            loadBitmapFromView(holder.imageView).compress(Bitmap.CompressFormat.PNG, 100, stream);
            book.setCoverImage(stream.toByteArray());

        }

        return arg1;
    }

    public dataAdapter(int listID, String userID)
    {
        booksList = getDataForListView(listID,userID);
    }



    public static Bitmap loadBitmapFromView(View v) {

        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);

        return b;
    }


    public void UpdateAdapter(ListView lv, String id, int pressed_list)
    {
        booksList= getDataForListView(pressed_list, id);
        this.notifyDataSetChanged();
        lv.invalidateViews();

    }

    public List<Books> getDataForListView(int pressed_list_index, String userid) {
        //String json = "{\"BookID\":\"1\",\"ISBN10\":\"123456789\",\"ISBN13\":\"12345678934\",\"Title\":\"The Principles of Shuffling\",\"AuthorName\":\"Cristian Restituyo\",\"AuthorID\":\"1\"},\"imgURL\":\"WHATEVER\",\"Category\":\"Baraje\"}";


        String json = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/booksinlist/get", UniversalJsonSerializer.convertToJason("UserId", userid, "ListId", Integer.toString(pressed_list_index)));

        if(!json.equals("NO_NETWORK_CONNECTION")) {
            //Log.e("Libros",json);
        /*
        String json = "[\n" +
                "\t{\n" +
                "\t\t\"BookId\":1,\n" +
                "\t\t\"ISBN13\":\"9780062315007\",\n" +
                "\t\t\"ISBN10\":\"0062315005\",\n" +
                "\t\t\"Title\":\"The Alchemist\",\n" +
                "\t\t\"Cover\":\"https://books.google.com/books/content?id=pTr44Sx6oWQC&printsec=frontcover&img=1&zoom=5&edge=curl&imgtk=AFLRE731UwSW5d7TIP3dWcz5sOJRQlspc2AgCiBmWWOysxMRKlszi9JGsbaWTpZa5-5nMKl8smRLg9RmP66rmHMVIrGjYB3gVD2_1o4zgqC_5xt61samkNvqX1rOAx7bsOCk6aWq46gR\",\n"+
                "\t\t\"Author\":{\"firstName\":\"Paulo\",\"lastName\":\"Coelho\"},\n" +
                "\t\t\"Category\":\"Fiction\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"BookId\":2,\n" +
                "\t\t\"ISBN13\":\"9780393240184\",\n" +
                "\t\t\"ISBN10\":\"0393240185\",\n" +
                "\t\t\"Title\":\"Between You and Me\",\n" +
                "\t\t\"Cover\":\"http://books.google.com/books/content?id=yKKkoAEACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api\",\n" +
                "\t\t\"Author\":{\"firstName\":\"Mary\",\"lastName\":\"Norris\"},\n" +
                "\t\t\"Category\":\"Language Arts & Disciplines\"\n" +
                "\t}\n" +
                "]";

                */

            List<Books> bl = new ArrayList<Books>();
            if (!json.equals("\"error\"")) {

                final GsonBuilder gsonBuilder = new GsonBuilder();
                //gsonBuilder.registerTypeAdapter(Books.class, new UniversalJsonDeserializer.BookDeserializer());
                //gsonBuilder.registerTypeAdapter(Authors.class, new UniversalJsonDeserializer.AuthorsDeserializer());

                Gson gson = gsonBuilder.create();
                TypeToken<List<Books>> type = new TypeToken<List<Books>>() {
                };


                //List<Books> bl = new ArrayList<Books>();

                try {
                    //Books b = gson.fromJson(json, Books.class);
                    //bl.add(b);
                    bl = gson.fromJson(json, type.getType());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    JsonReader reader = new JsonReader(new StringReader(json));
                    reader.setLenient(true);

                    //Books b = gson.fromJson(reader,Books.class);
                    //bl.add(b);
                    bl = gson.fromJson(reader, type.getType());
                }

            } else {
                bl = new ArrayList<Books>();
            }

            return bl;
        }
        return new ArrayList<Books>();

    }
}

