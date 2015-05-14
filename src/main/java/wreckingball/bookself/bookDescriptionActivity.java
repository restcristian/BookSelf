package wreckingball.bookself;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianrestituyo.mycameratest.R;

import java.util.ArrayList;

import datamodel.Authors;
import datamodel.Books;
import wreckingball.bookself.booklist.ImageDownloaderTask;

public class bookDescriptionActivity extends Activity {

    private TextView txtDescription;
    private TextView txtBookTitle;
    private TextView txtBookAuthors;
    private ImageView imgBookCover;
    //private Button addToBookShelfbtn;
    private ImageButton addToBookShelfbtn;
    private ImageButton addBookshelf;
    private ImageButton scannBook;
    private Bundle bundle;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);
        txtDescription =(TextView)findViewById(R.id.bookDescriptionTxt);
        txtBookTitle = (TextView)findViewById(R.id.bookTitleTxt);
        imgBookCover = (ImageView)findViewById(R.id.BookCover);
        txtBookAuthors = (TextView)findViewById(R.id.Authorstxt);
        String authors;

        addToBookShelfbtn = (ImageButton)findViewById(R.id.addBook);
        addBookshelf = (ImageButton)findViewById(R.id.addBookshelf);
        scannBook = (ImageButton)findViewById(R.id.scanBook);

        addBookshelf.setVisibility(View.INVISIBLE);
        scannBook.setVisibility(View.INVISIBLE);

        txtBookTitle.setTypeface(Typeface.create("Verdana",Typeface.BOLD));
        txtDescription.setMovementMethod(new ScrollingMovementMethod());
        bundle = getIntent().getExtras();
        final Books book = (Books)getIntent().getSerializableExtra("RESULT_BOOK");
        flag = getIntent().getIntExtra("FLAG",0);
        txtBookAuthors.setText("");

        txtDescription.setText(book.getDescription());


        addToBookShelfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = getIntent().getExtras().getString("USER_INSTANCE");
                String listId = Integer.toString(getIntent().getExtras().getInt("LIST_ID"));
                String ISBN = (book.getISBN10().equals("null") || book.getISBN10() == null)?book.getISBN13():book.getISBN10();



                String addString;
                if(flag==0) {
                    if (!BooksInsideListView.da.hasISBN(ISBN)) {
                        addString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/booksinlist/add", UniversalJsonSerializer.convertToJason("UserId", userId, "ListId", listId, "ISBN", ISBN));
                    } else
                        addString = "\"false\"";
                    if(!addString.equals("\"false\""))
                    {
                        Toast.makeText(getApplicationContext(),"BOOK ADDED",Toast.LENGTH_LONG).show();
                        //BooksInsideListView.da.booksList.add(Books.Parse(addString));
                        BooksInsideListView.da.UpdateAdapter(BooksInsideListView.lv,userId,Integer.parseInt(listId));
                        finish();
                    }
                    else
                    {
                       // Toast.makeText(getApplicationContext(),addString,Toast.LENGTH_LONG).show();

                    }
                }else
                    addString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/booksinlist/add", UniversalJsonSerializer.convertToJason("UserId", userId, "ListId", listId, "ISBN", ISBN));

                    if(addString.equals("\"false\""))
                    {
                        Toast.makeText(getApplicationContext(),"The Book is already on the list! Cannot have duplicates!",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "BOOK ADDED", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), BooksInsideListView.class);
                        i.putExtra("LIST_PRESSED", listId);
                        i.putExtra("USER_ID", userId);
                        i.putExtra("BOOK", book);

                        startActivity(i);
                        //BooksInsideListView.da.booksList.add(book);
                        //BooksInsideListView.da.notifyDataSetChanged();
                        finish();
                    }


            }
        });


        txtBookTitle.setText(book.getTitle());

        new ImageDownloaderTask(imgBookCover).execute(book.getCover());



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
