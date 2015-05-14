package wreckingball.bookself;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.Gson;

import net.sourceforge.zbar.Symbol;

import java.util.ArrayList;
import java.util.List;

import datamodel.BookLists;
import datamodel.Books;
import wreckingball.bookself.booklist.dataAdapter;

public class BooksInsideListView extends Activity {

    public static ListView lv;
    public static dataAdapter da;
    ImageButton addBook;
    ImageButton scanBook;
    ImageButton addBookshelf;
    String userID;
    int ListId;
    private static final int ZBAR_SCANNER_REQUEST = 0;
    private List<Books> booksHolder;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_inside_list_view);

        lv = (ListView)findViewById(R.id.listView2);
        addBook = (ImageButton)findViewById(R.id.addBook);
        scanBook = (ImageButton)findViewById(R.id.scanBook);
        addBookshelf = (ImageButton)findViewById(R.id.addBookshelf);

        addBookshelf.setVisibility(View.INVISIBLE);

        Bundle mbundle = getIntent().getExtras();

        ListId = mbundle.getInt("PRESSED_LIST");
        userID = mbundle.getString("USER_ID");

        //Toast.makeText(getApplicationContext(),"PRESSED LIST:"+ListId+" USERID:"+userID,Toast.LENGTH_LONG).show();

        da = new dataAdapter(ListId,userID);
        lv.setAdapter(da);
        if(mbundle.getSerializable("BOOK")!= null)
        {
            Books b =(Books)mbundle.getSerializable("BOOK");
            if(!da.hasISBN(b.getISBN10()) && !da.hasISBN(b.getISBN13()))
            {
                da.booksList.add(b);
            }

        }

        booksHolder = da.booksList;//new ArrayList<>();


        search =  (SearchView)findViewById(R.id.menu_search2);
        search.setQueryHint("Search your book...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    da.booksList = booksHolder;
                    da.notifyDataSetChanged();
                }else{
                    List<Books> temp = new ArrayList<Books>();
                    for (Books book : booksHolder) {
                        if(book.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            temp.add(book);
                        }
                    }
                    da.booksList=temp;
                    da.notifyDataSetChanged();
                }

                return true;
            }

        });

        search.setSubmitButtonEnabled(false);

        scanBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),String.valueOf(da.getItem(position).getBookID()),Toast.LENGTH_LONG).show();
                globalBookshelf.deleteBookInList(BooksInsideListView.this,String.valueOf(da.getItem(position).getBookID()),userID,da,ListId);
                return true;
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalBookshelf.addISBN(BooksInsideListView.this,userID,ListId);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BooksInsideListView.this, bookDescriptionActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("RESULT_BOOK",da.getItem(position));
                i.putExtras(bundle);
                i.putExtra("USER_INSTANCE",userID);
                i.putExtra("LIST_ID",ListId);

                startActivity(i);
            }
        });
      //  Toast.makeText(getApplicationContext(),Integer.toString(ListId) + " " + userID,Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_books_inside_list_view, menu);
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

    public void startScanning()
    {
        Intent intent = new Intent(getApplicationContext(), ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.EAN13});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);

    }

    public void onActivityResult(int requestCode,  int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK)
        {
            //txtContent.setText(data.getStringExtra(ZBarConstants.SCAN_RESULT).toString());

            if (data.getStringExtra(ZBarConstants.SCAN_RESULT).length() < 10 ||  data.getStringExtra(ZBarConstants.SCAN_RESULT).length() > 13)
            {
                Toast.makeText(getApplicationContext(), "ERROR!!!INVALID ISBN", Toast.LENGTH_LONG).show();
            }

            else {

                String isbn = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                if (!da.hasISBN(isbn)) {
                    Books book = new Books();
                    String bookString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/books/get", UniversalJsonSerializer.convertToJason("UserId", userID, "ISBN", isbn));

                    if (!bookString.contains("NO_NETWORK_CONNECTION")) {
                        Gson gson = new Gson();
                        book = gson.fromJson(bookString, Books.class);

                        //Toast.makeText(getApplicationContext(), bookString,Toast.LENGTH_LONG).show();

                        if (!book.getISBN13().equals("error") && !book.getISBN13().equals("Missing Parameters")) {
                            //Toast.makeText(getApplicationContext(), book.getISBN13(),Toast.LENGTH_LONG).show();


                            Bundle bundle = new Bundle();
                            Intent i = new Intent(getApplicationContext(), bookDescriptionActivity.class);
                            bundle.putString("USER_INSTANCE", userID);
                            bundle.putSerializable("RESULT_BOOK", book);
                            bundle.putInt("LIST_ID", ListId);
                            i.putExtras(bundle);

                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Book not found", Toast.LENGTH_LONG).show();
                        }

                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                }
            }

        }

    }

}
