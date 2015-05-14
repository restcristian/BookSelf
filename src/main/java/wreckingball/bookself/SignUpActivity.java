package wreckingball.bookself;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianrestituyo.mycameratest.MainFragmentActivity;
import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
//import com.google.gson.JsonElement;

//import org.json.JSONObject;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import datamodel.Authors;
import datamodel.BookLists;
import datamodel.Books;
import datamodel.Users;

public class SignUpActivity extends Activity {

    private TextView Join;
    private TextView Ft_name;
    private TextView Lt_name;
    private TextView E_Mail;
    private TextView Pass_Word;
    private EditText F_name;
    private EditText L_name;
    private EditText E_mail;
    private EditText Password;
    private Button Sign_UP;
    private Books b;
    private BookLists booklists;
    private Users new_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*********************NETWORK THREADS PERMISSION POLICY FORCE*************************/

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /***********************************************************************************/

        Typeface euro_regular= Typeface.createFromAsset(getAssets(), "fonts/eurof35.ttf");
        Typeface euro_bold = Typeface.createFromAsset(getAssets(),"fonts/eurof75.ttf");

        Join = (TextView)findViewById(R.id.join);
        Join.setTypeface(euro_regular);
        Ft_name = (TextView)findViewById(R.id.First_Name);
        Ft_name.setTypeface(euro_regular);
        Lt_name = (TextView)findViewById(R.id.Last_Name);
        Lt_name.setTypeface(euro_regular);
        E_Mail = (TextView)findViewById(R.id.Email);
        E_Mail.setTypeface(euro_regular);
        Pass_Word = (TextView)findViewById(R.id.Password);
        Pass_Word.setTypeface(euro_regular);

        F_name = (EditText)findViewById(R.id.Enter_First_Name);
        L_name = (EditText)findViewById(R.id.Enter_Last_Name);
        E_mail = (EditText)findViewById(R.id.Enter_Email);
        Password = (EditText)findViewById(R.id.Enter_Password);


        Sign_UP = (Button) findViewById(R.id.Sign_In);

        new_user = new Users();
        b = new Books();
        booklists = new BookLists();


        Sign_UP.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {



                if (filling_new_user(new_user)) {

                    String result = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/registration", UniversalJsonSerializer.parseToJson(new_user.getClass(), new UniversalJsonSerializer.UserSerializer(), new_user));
                   //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    if(result.equals("\"error\""))
                    {
                        Toast.makeText(getApplicationContext(),"Error:User Already Exists!",Toast.LENGTH_LONG).show();
                    }else {
                        if (!result.equals("\"Missing Parameters\"")) {
                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();
                            new_user.setId(result);
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                        //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();



                } else {
                    Toast.makeText(getApplicationContext(), "INVALID EMAIL_FORMAT!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public class EmailValidator
    {
        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator()
        {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(String str)
        {
           matcher = pattern.matcher(str);
           return matcher.matches();
        }

    }



    private boolean filling_new_user(Users user)
    {

        if(!new EmailValidator().validate(E_mail.getText().toString()))
        {
            return false;
        }else {


            user.setBooklists(null);
            user.setFirstName(F_name.getText().toString());
            user.setLastName(L_name.getText().toString());
            user.setEmail(E_mail.getText().toString());
            user.setPassword(Password.getText().toString());
            return true;
        }
    }






}
