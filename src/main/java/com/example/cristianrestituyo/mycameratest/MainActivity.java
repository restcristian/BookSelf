package com.example.cristianrestituyo.mycameratest;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.Fragment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.sourceforge.zbar.Symbol;

import org.w3c.dom.Text;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import datamodel.BookLists;
import datamodel.Books;
import datamodel.Users;
import wreckingball.bookself.UniversalJsonSerializer;
import wreckingball.bookself.booklist.BookShelfAdapter;
import wreckingball.bookself.booklist.ListViewActivity;
import wreckingball.bookself.httpTasks;
import wreckingball.bookself.bookDescriptionActivity;
import wreckingball.bookself.sessionManagement.SessionManagement;

//Extends ActionBarActivity


public class MainActivity extends Activity {

    public static Users user;
    private ImageButton btnScan;
    private ImageButton btnAddISBN;
    private ImageButton btnCreateBS;
    private TextView txtContent;
    private Intent intent;
    private View context;
    private String UserID;
    private static final int ZBAR_SCANNER_REQUEST = 0;
    SessionManagement session;
    private BookLists new_booklist;
    private boolean CAMERA_SWITCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*********************NETWORK THREADS PERMISSION POLICY FORCE*************************/

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /***********************************************************************************/

        CAMERA_SWITCH = false;
        /******************GETTING THE WHOLE USER INSTANCE FROM THE LOGIN ACTIVITY***************/

        //user = (Users)getIntent().getSerializableExtra("USER_INSTANCE");
        //Get User data
        session = new SessionManagement(getApplicationContext());
        session.check_login();



        HashMap<String, String> user1 = session.getUserDetails();
        UserID = user1.get(SessionManagement.KEY_ID);

        String getList = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/get",UniversalJsonSerializer.convertToJason("UserId",UserID));


        if(getList.equals("NO_NETWORK_CONNECTION"))
        {
            Toast.makeText(getApplicationContext(),"CHECK YOUR NETWORK CONNECTION!",Toast.LENGTH_LONG).show();
            finish();
        }
        else
        if(!getList.equals("\"error\""))
        {
            Intent i = new Intent(MainActivity.this, ListViewActivity.class);
            Bundle bundle = new Bundle();

            //bundle.putSerializable("USER_INSTANCE",user);
            bundle.putString("USER_TOKEN",UserID);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }
        else if(getList.equals("\"error\""))
        {
            //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
          //finish();

        }






       // Toast.makeText(getApplicationContext(), "Welcome " + user.getFirstName(), Toast.LENGTH_LONG).show();



        /**************************************************************************************/
    }
    @Override
    public void onStart() {

        btnScan = (ImageButton)findViewById(R.id.scan_button);
        btnAddISBN = (ImageButton)findViewById(R.id.add_button);
        btnCreateBS = (ImageButton)findViewById(R.id.add_bookshelf);



        btnAddISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addISBN();
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });
        btnCreateBS.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                createBookShelf();
            }
        });


        super.onStart();
    }
    public void createBookShelf()
    {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.activity_prompt_list_name_and_type,null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);


        alertDialogBuilder.setView(promptsView);

        //final TextView txtNewListNamePrompt = (TextView) promptsView.findViewById(R.id.PromptNewList);
        final EditText newListName = (EditText) promptsView.findViewById(R.id.newListNameDialogUserInput);
        final CheckBox wishlistCheck = (CheckBox)promptsView.findViewById(R.id.whishListCheck);



       // userInput.setInputType(InputType.TYPE_CLASS_NUMBER);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if (newListName.getText().toString().isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "You cannot create a list with no name!!!", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }

                                new_booklist = new BookLists();
                                //user.getBooklists().add(bl);
                                new_booklist.setListName(newListName.getText().toString());
                                new_booklist = CreatingBookshelf(new_booklist.getListName(), (wishlistCheck.isChecked()) ? "WL" : "NWL");
                                Intent i = new Intent(MainActivity.this, ListViewActivity.class);
                                i.putExtra("USER_TOKEN", UserID);
                                dialog.cancel();
                                startActivity(i);
                                finish();

                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public BookLists CreatingBookshelf(String bsName, String listType)
    {


        String json = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/create",UniversalJsonSerializer.convertToJason("UserId",UserID,"ListType",listType,"ListName",bsName));

        return BookLists.Parse(json);



    }


    public void addISBN()
    {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.activity_prompt_isbn,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);


        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (userInput.getText().length() < 10 || userInput.getText().length() > 13) {
                                    Toast.makeText(getBaseContext(), "The ISBN must have a minimum of 10 and a max of 13 digits", Toast.LENGTH_LONG).show();
                                    dialog.cancel();

                                } else {
                                    //h.ISBN = userInput.getText().toString();

                                    String isbn = userInput.getText().toString();
                                    Books book = new Books();
                                    String bookString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/books/get", UniversalJsonSerializer.convertToJason("UserId", UserID, "ISBN", isbn));


                                    Gson gson = new Gson();
                                    book = gson.fromJson(bookString, Books.class);

                                    if (!bookString.equals("NO_NETWORK_CONNECTION") && !book.getISBN13().equals("error")) {
                                        new_booklist = CreatingBookshelf("DEFAULT_LIST", "NL");

                                        //Toast.makeText(getApplicationContext(),"DEFAULT_LIST Created",Toast.LENGTH_LONG).show();


                                        //Toast.makeText(getApplicationContext(), bookString,Toast.LENGTH_LONG).show();

                                        if (!book.getISBN13().equals("error") && !book.getISBN13().equals("Missing Parameters")) {
                                            // Toast.makeText(getApplicationContext(), book.getISBN13(),Toast.LENGTH_LONG).show();

                                            Bundle bundle = new Bundle();
                                            Intent i = new Intent(getApplicationContext(), bookDescriptionActivity.class);
                                            bundle.putString("USER_INSTANCE", UserID);
                                            bundle.putSerializable("RESULT_BOOK", book);
                                            bundle.putInt("LIST_ID", new_booklist.getListID());
                                            bundle.putInt("FLAG", 1);
                                            i.putExtras(bundle);

                                            Intent lIntent = new Intent(getApplicationContext(), ListViewActivity.class);
                                            lIntent.putExtra("USER_TOKEN", UserID);

                                            startActivity(lIntent);
                                            startActivity(i);
                                            finish();

                                        }
                                    } else if (book.getISBN13().equals("error")) {
                                        Toast.makeText(getApplicationContext(), "Book not found", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "CHECK YOUR NETWORK CONNECTION", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }
    })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        Log.e("CLICK ON ADD ISBN - ", "Clicked");
    }
    public void startScanning()
    {

        intent = new Intent(this.getApplicationContext(), ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.EAN13});
        CAMERA_SWITCH = true;
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode,  int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {

            CAMERA_SWITCH = false;
            if (data.getStringExtra(ZBarConstants.SCAN_RESULT).length() < 10 ||  data.getStringExtra(ZBarConstants.SCAN_RESULT).length() > 13)
            {
                Toast.makeText(getApplicationContext(), "ERROR!!!INVALID ISBN", Toast.LENGTH_LONG).show();
            }

            else
            {

                String isbn = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                Books book = new Books();
                String bookString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/books/get", UniversalJsonSerializer.convertToJason("UserId", UserID, "ISBN", isbn));

                if(!bookString.equals("NO_NETWORK_CONNECTION")) {
                    new_booklist = CreatingBookshelf("DEFAULT_LIST", "NL");

                    Gson gson = new Gson();
                    book = gson.fromJson(bookString, Books.class);

                    // Toast.makeText(getApplicationContext(), bookString,Toast.LENGTH_LONG).show();

                    if (!book.getISBN13().equals("error") && !book.getISBN13().equals("Missing Parameters")) {
                        //Toast.makeText(getApplicationContext(), book.getISBN13(),Toast.LENGTH_LONG).show();


                        Bundle bundle = new Bundle();
                        Intent i = new Intent(getApplicationContext(), bookDescriptionActivity.class);
                        bundle.putString("USER_INSTANCE", UserID);
                        bundle.putSerializable("RESULT_BOOK", book);
                        bundle.putInt("LIST_ID", new_booklist.getListID());
                        bundle.putInt("FLAG", 1);
                        i.putExtras(bundle);

                        Intent lIntent = new Intent(getApplicationContext(), ListViewActivity.class);
                        lIntent.putExtra("USER_TOKEN", UserID);

                        startActivity(lIntent);

                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Book not found", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"CHECK YOUR INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                }
            }


        }

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(!CAMERA_SWITCH) {
            finish();
        }
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
