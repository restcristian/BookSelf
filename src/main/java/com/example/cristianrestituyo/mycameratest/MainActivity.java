package com.example.cristianrestituyo.mycameratest;

import android.app.AlertDialog;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

//Extends ActionBarActivity

public class MainActivity extends Fragment {
    private ImageButton btnScan;
    private ImageButton btnAddISBN;
    private TextView txtContent;
    private Intent intent;
    private View context;
    private static final int ZBAR_SCANNER_REQUEST = 0;
   // private static final int ZBAR_QR_SCANNER_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        context = v;
        return v;
    }
    @Override
    public void onStart() {


        btnScan = (ImageButton)context.findViewById(R.id.scan_button);
        btnAddISBN = (ImageButton)context.findViewById(R.id.add_button);
        txtContent = (TextView)context.findViewById(R.id.scan_content);


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
        super.onStart();
    }
    public void addISBN()
    {
        LayoutInflater li = LayoutInflater.from(context.getContext());
        View promptsView = li.inflate(R.layout.activity_prompt_isbn, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context.getContext());


        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (userInput.getText().length() < 10) {
                                    Toast.makeText(context.getContext(), "The ISBN must have a minimum of 10 digits", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                } else {
                                    txtContent.setText(userInput.getText());
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
    public void startScanning()
    {
        intent = new Intent(this.getActivity(), ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.EAN13});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode,  int resultCode, Intent data)
    {
        if (resultCode == getActivity().RESULT_OK)
        {
            txtContent.setText(data.getStringExtra(ZBarConstants.SCAN_RESULT).toString());
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
