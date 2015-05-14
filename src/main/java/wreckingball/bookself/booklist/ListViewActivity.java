package wreckingball.bookself.booklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.example.cristianrestituyo.mycameratest.MainActivity;
import com.example.cristianrestituyo.mycameratest.MainFragmentActivity;
import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.Gson;

import net.sourceforge.zbar.Symbol;

import java.util.ArrayList;
import java.util.List;

import datamodel.BookLists;
import datamodel.Books;
import datamodel.Users;
import wreckingball.bookself.BooksInsideListView;
import wreckingball.bookself.UniversalJsonSerializer;
import wreckingball.bookself.bookDescriptionActivity;
import wreckingball.bookself.globalBookshelf;
import wreckingball.bookself.httpTasks;

public class ListViewActivity extends Activity {

   // private View context;
    public static ListView bookList;
    public static BookShelfAdapter da;
    public Users user;
    public String userId;
    ImageButton createBookshelf;
    ImageButton addBook;
    ImageButton scanBook;
    private List<BookLists> bookHolder;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        createBookshelf = (ImageButton)findViewById(R.id.addBookshelf);
        addBook = (ImageButton)findViewById(R.id.addBook);
        scanBook = (ImageButton)findViewById(R.id.scanBook);

        addBook.setVisibility(View.INVISIBLE);
        scanBook.setVisibility(View.INVISIBLE);


        bookList = (ListView)findViewById(R.id.listView1);
        //final dataAdapter da = new dataAdapter();
       // user = (Users)getIntent().getSerializableExtra("USER_INSTANCE");
        userId = getIntent().getExtras().getString("USER_TOKEN");
        da = new BookShelfAdapter(userId);

        bookList.setAdapter(da);
        bookHolder = da.BookShelfList;//new ArrayList<>();

        search =  (SearchView)findViewById(R.id.menu_search);
        search.setQueryHint("Search your bookSelf...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    da.BookShelfList = bookHolder;
                    da.notifyDataSetChanged();
                }else{
                    List<BookLists> temp = new ArrayList<BookLists>();
                    for (BookLists bookList : bookHolder) {
                        if(bookList.getListName().toLowerCase().contains(newText.toLowerCase())){
                            temp.add(bookList);
                        }
                    }
                    da.BookShelfList=temp;
                    da.notifyDataSetChanged();
                }

                return true;
            }

        });

        search.setSubmitButtonEnabled(false);

        createBookshelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalBookshelf.createBookShelf(ListViewActivity.this,userId);
            }
        });

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListViewActivity.this, BooksInsideListView.class);
                i.putExtra("PRESSED_LIST",da.getItem(position).getListID());
                i.putExtra("USER_ID",userId);
                startActivity(i);
                /*****/
            }
        });
        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                LayoutInflater li = LayoutInflater.from(ListViewActivity.this);
                final View promptsView = li.inflate(R.layout.activity_drop_down, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ListViewActivity.this);


                alertDialogBuilder.setView(promptsView);



                //final TextView userPrompt = (TextView) promptsView
                  //      .findViewById(R.id.areyousuretxt);

                //userPrompt.setText("Are you sure you want to delete this list?");



                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Delete List",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String viewID = Integer.toString(da.getItem(position).getListID());

                                       // String deletedit = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/deleteit", UniversalJsonSerializer.convertToJason("UserId",userId,"ListId",viewID));
                                        globalBookshelf.deleteBookList(ListViewActivity.this,userId,da,Integer.parseInt(viewID));
                                        //da.UpdateAdapter(bookList,userId);
                                        //Toast.makeText(getApplicationContext(),"List Deleted",Toast.LENGTH_LONG).show();


                                    }
                                })

                        .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                globalBookshelf.editBookShelf(ListViewActivity.this,userId,da.getItem(position).getListID(),da,da.getItem(position),position);
                            }
                        })
                        .setNeutralButton("Do Nothing", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                return true;
            }

        });
    }




}
