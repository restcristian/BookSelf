package wreckingball.bookself.sessionManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import datamodel.Users;
import wreckingball.bookself.LoginActivity;

/**
 * Created by Cristian on 4/25/2015.
 */
public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MyPref";
    private static final String IS_LOGGED_IN = "IsLoggedIn";

    //public static final Users k_user = new Users();

    public static final String KEY_ID = "";

    public SessionManagement(Context c)
    {
        this.context = c;
        sharedPreferences = c.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(Users u)
    {
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.putString(KEY_ID,u.getId());
        editor.commit();
    }

    public HashMap<String,String> getUserDetails()
    {
        HashMap<String,String> user = new HashMap<String,String>();
        user.put(KEY_ID, sharedPreferences.getString(KEY_ID, null));
        return user;
    }

    public boolean isloggedin()
    {
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }
    public void check_login()
    {
        if(!this.isloggedin())
        {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }

}
