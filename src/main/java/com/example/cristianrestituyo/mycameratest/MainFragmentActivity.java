package com.example.cristianrestituyo.mycameratest;

//import android.support.v4.app.FragmentActivity;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.*;
import android.widget.Toast;

import datamodel.Books;
import datamodel.Users;


public class MainFragmentActivity extends FragmentActivity {

    public static Users user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        /*********************NETWORK THREADS PERMISSION POLICY FORCE*************************/

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /***********************************************************************************/

        /******************GETTING THE WHOLE USER INSTANCE FROM THE LOGIN ACTIVITY***************/

        user = (Users)getIntent().getSerializableExtra("USER_INSTANCE");


        Toast.makeText(getApplicationContext(), "Welcome " + user.getFirstName(), Toast.LENGTH_LONG).show();


        /**************************************************************************************/
       /* MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager());
        ViewPager myPager = (ViewPager) findViewById(R.id.Mypanelpager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
