package wreckingball.bookself;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cristianrestituyo.mycameratest.MainActivity;
import com.example.cristianrestituyo.mycameratest.MainFragmentActivity;
import com.example.cristianrestituyo.mycameratest.R;
import com.google.gson.Gson;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import datamodel.Users;
import wreckingball.bookself.sessionManagement.SessionManagement;

public class LoginActivity extends Activity {

    private TextView User_name;
    private TextView Pass_word;
    private TextView Welcome;
    private EditText textUsername;
    private EditText textPassword;
    private TextView chance;
    private Button sign_in;
    private Button sign_up;
    private Users user;
    SessionManagement session;
    int counter = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplicationContext());


        if(session.isloggedin()) {
            //Toast.makeText(getApplicationContext(), "User is :" + session.isloggedin(), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        Typeface euro_regular =Typeface.createFromAsset(getAssets(),"fonts/eurof35.ttf");
        Typeface euro_bold = Typeface.createFromAsset(getAssets(),"fonts/eurof75.ttf");
        Welcome = (TextView)findViewById(R.id.textView2);
        Welcome.setTypeface(euro_regular);
        User_name = (TextView)findViewById(R.id.textView1);
        User_name.setTypeface(euro_regular);
        Pass_word = (TextView)findViewById(R.id.textView3);
        Pass_word.setTypeface(euro_regular);
        textUsername = (EditText)findViewById(R.id.editText1);
        textPassword = (EditText)findViewById(R.id.editText2);
        //chance = (TextView)findViewById(R.id.textView4);
//        chance.setText(Integer.toString(counter));
        sign_in = (Button) findViewById(R.id.Log_In);
        sign_up = (Button) findViewById(R.id.Sign_Up);

//        chance.setText("Login Attempts: " + Integer.toString(counter));

        sign_in.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sign_in();

            }

        });

        sign_up.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sign_up();

            }

        });

    }


    private void sign_in() {


        Users user = new Users();


        user.setEmail(textUsername.getText().toString());
        user.setPassword(textPassword.getText().toString());

        String userString;
        Gson gson = new Gson();

        userString = httpTasks.sendParametersToAPI("http://wreckingballv1-dev.elasticbeanstalk.com/api/userlogin/",UniversalJsonSerializer.parseToJson(user.getClass(), new UniversalJsonSerializer.UserSerializer(),user));

       // Toast.makeText(getApplicationContext(),userString,Toast.LENGTH_LONG).show();

        if(!userString.equals("NO_NETWORK_CONNECTION")) {

            user = gson.fromJson(userString, Users.class);

            if (user.getId().equals("error")) {
                Toast.makeText(getApplicationContext(), "Wrong UserName or Password", Toast.LENGTH_LONG).show();
            } else if (user.getId().equals("Missing Parameters")) {
                Toast.makeText(getApplicationContext(), "Missing Parameters", Toast.LENGTH_LONG).show();
            } else {
                Bundle bundle = new Bundle();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                //bundle.putSerializable("USER_INSTANCE",user);
                //i.putExtras(bundle);
                session.createLoginSession(user);
                startActivity(i);
                finish();

            }
        }else
        {
            Toast.makeText(getApplicationContext(),"CHECK YOUR INTERNET CONNECTION",Toast.LENGTH_LONG).show();
        }


    }

    private void sign_up()
    {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
    }


}
