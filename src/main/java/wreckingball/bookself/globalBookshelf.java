package wreckingball.bookself;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.example.cristianrestituyo.mycameratest.MainActivity;
import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.Gson;

import net.sourceforge.zbar.Symbol;

import java.util.HashMap;
import java.util.List;

import datamodel.BookLists;
import datamodel.Books;
import wreckingball.bookself.booklist.BookShelfAdapter;
import wreckingball.bookself.booklist.ListViewActivity;
import wreckingball.bookself.booklist.dataAdapter;
import wreckingball.bookself.sessionManagement.SessionManagement;

/**
 * Created by Cristian on 4/30/2015.
 */
public class globalBookshelf {

    public static void addISBN(final Context context, final String UserID, final int ListId)
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_prompt_isbn,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


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
                                    Toast.makeText(context, "The ISBN must have a minimum of 10 and a max of 13 digits", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                } else {
                                    //h.ISBN = userInput.getText().toString();

                                    String isbn = userInput.getText().toString();
                                    if (!BooksInsideListView.da.hasISBN(isbn)) {
                                        Books book = new Books();
                                        String bookString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/books/get", UniversalJsonSerializer.convertToJason("UserId", UserID, "ISBN", isbn));

                                        if (!bookString.equals("NO_NETWORK_CONNECTION")) {

                                            Gson gson = new Gson();
                                            book = gson.fromJson(bookString, Books.class);

                                            //Toast.makeText(context, bookString, Toast.LENGTH_LONG).show();

                                            if (!book.getISBN13().equals("error") && !book.getISBN13().equals("Missing Parameters")) {
                                                // Toast.makeText(getApplicationContext(), book.getISBN13(),Toast.LENGTH_LONG).show();

                                                Bundle bundle = new Bundle();
                                                Intent i = new Intent(context, bookDescriptionActivity.class);
                                                bundle.putString("USER_INSTANCE", UserID);
                                                bundle.putSerializable("RESULT_BOOK", book);
                                                bundle.putInt("LIST_ID", ListId);
                                                bundle.putString("BOOK_ISBN", userInput.getText().toString());
                                                i.putExtras(bundle);

                                                context.startActivity(i);

                                            } else {
                                                Toast.makeText(context, "Book not found", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Cedeno was here
                                            //Book already in the list
                                        }
                                    }else
                                    {
                                        Toast.makeText(context,"CHECK INTERNET CONNECTION!",Toast.LENGTH_LONG).show();
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

    public static void deleteBookInList(final Context context, final String bookID, final String userId, final dataAdapter da, final int ListId)
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_areyou_sure, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        alertDialogBuilder.setView(promptsView);



        final TextView userPrompt = (TextView) promptsView
                .findViewById(R.id.areyousuretxt);

        userPrompt.setText("Are you sure you want to delete this book?");


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                //Toast.makeText(context,userId,Toast.LENGTH_LONG).show();
                                //Toast.makeText(context,bookID,Toast.LENGTH_LONG).show();
                                //Toast.makeText(context,delete,Toast.LENGTH_LONG).show();

                                String deletedit = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/booksinlist/deleteit", UniversalJsonSerializer.convertToJason("UserId", userId, "BookId", bookID, "ListId", Integer.toString(ListId)));

                                if (!deletedit.equals("NO_NETWORK_CONNECTION")) {
                                    for (Books book : da.booksList) {
                                        if (Integer.parseInt(bookID) == book.getBookID()) {
                                            da.booksList.remove(book);
                                            break;
                                        }
                                    }
                                    da.notifyDataSetChanged();
                                    //da.UpdateAdapter(BooksInsideListView.lv,userId,Integer.parseInt(bookID));
                                    //Toast.makeText(context,deletedit,Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "CHECK YOUR NETWORK CONNECTION!!", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                .setNegativeButton("No",
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

    public static void deleteBookList(final Context context, final String userId, final BookShelfAdapter da, final int ListId)
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_areyou_sure, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        alertDialogBuilder.setView(promptsView);



        final TextView userPrompt = (TextView) promptsView
                .findViewById(R.id.areyousuretxt);

        userPrompt.setText("Are you sure you want to delete this book list?");


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                String deletedit = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/deleteit", UniversalJsonSerializer.convertToJason("UserId", userId, "ListId", Integer.toString(ListId)));
                                if (!deletedit.equals("NO_NETWORK_CONNECTION")) {
                                    for (BookLists booklist : da.BookShelfList) {
                                        if (ListId == booklist.getListID()) {
                                            da.BookShelfList.remove(booklist);
                                            break;
                                        }
                                    }


                                    da.notifyDataSetChanged();
                                    //da.UpdateAdapter(BooksInsideListView.lv,userId,Integer.parseInt(bookID));
                                    //Toast.makeText(context,deletedit,Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    Toast.makeText(context,"CHECK INTERNET CONNECTION!!",Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                .setNegativeButton("No",
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

    public static void createBookShelf(final Context context, final String UserID)
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_prompt_list_name_and_type,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        alertDialogBuilder.setView(promptsView);

        //final TextView txtNewListNamePrompt = (TextView) promptsView.findViewById(R.id.PromptNewList);
        final EditText newListName = (EditText) promptsView.findViewById(R.id.newListNameDialogUserInput);
        final CheckBox wishlistCheck = (CheckBox)promptsView.findViewById(R.id.whishListCheck);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if(newListName.getText().toString().isEmpty())
                                {
                                    Toast.makeText(context,"You cannot create a list with no name!!!",Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }

                                if (ListViewActivity.da.hasName(newListName.getText().toString()))
                                {
                                    Toast.makeText(context, "The list name is already taken!!", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }
                                else
                                {
                                    BookLists bl = new BookLists();

                                    bl.setListName(newListName.getText().toString());
                                    CreatingBookshelf(bl.getListName(),(wishlistCheck.isChecked())?"WL":"NWL",context,UserID);

                                    dialog.cancel();


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
    }

    public static void editBookShelf(final Context context, final String UserID, final int ListID, final BookShelfAdapter da, final BookLists bookshelf,final int index)
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_prompt_list_name_and_type,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        alertDialogBuilder.setView(promptsView);

        final EditText newListName = (EditText) promptsView.findViewById(R.id.newListNameDialogUserInput);
        final CheckBox wishlistCheck = (CheckBox)promptsView.findViewById(R.id.whishListCheck);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if (da.hasName(newListName.getText().toString())) {
                                    Toast.makeText(context, "The list name is already taken!!", Toast.LENGTH_LONG).show();
                                    dialog.cancel();

                                } else {
                                    BookLists bl = new BookLists();

                                    bl.setListName((newListName.getText().toString().isEmpty())?da.BookShelfList.get(index).getListName():newListName.getText().toString());
                                    bl.setListType((wishlistCheck.isChecked()) ? "WL" : "NWL");


                                    String updateString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/edit", UniversalJsonSerializer.convertToJason("UserId", UserID, "ListId", Integer.toString(ListID), "ListName", bl.getListName(), "ListType", bl.getListType()));
                                    if (!updateString.equals("\"error\"")) {

                                        for (BookLists bookLists : da.BookShelfList)
                                        {
                                            if(bookLists.getListID() == bookshelf.getListID())
                                            {
                                                da.BookShelfList.get(index).setListName(bl.getListName());
                                                da.BookShelfList.get(index).setListType(bl.getListType());
                                            }
                                            da.notifyDataSetChanged();
                                        }
                                    }

                                    dialog.cancel();

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
    }


    public static void CreatingBookshelf(String bsName, String listType, Context context, String UserID) {

        String json = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/list/create", UniversalJsonSerializer.convertToJason("UserId", UserID, "ListType", listType, "ListName", bsName));

       // Toast.makeText(context, json, Toast.LENGTH_LONG).show();


        BookShelfAdapter b = (BookShelfAdapter) ListViewActivity.bookList.getAdapter();
        try {
            b.BookShelfList.add(BookLists.Parse(json));
            b.notifyDataSetChanged();
        }
        catch (Exception e){
            Toast.makeText(context, "CHECK YOUR INTERNET CONNECTION", Toast.LENGTH_LONG).show();

        }
    }



}
