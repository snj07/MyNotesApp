package com.apps.sanjay.mynotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.sanjay.mynotes.dbase.DBHandlerUser;
import com.apps.sanjay.mynotes.model.User;

public class SignupActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "MyPrefs";
    private static final String USER_ID = "uIdKey";


    private EditText username, pass, confirmPassword;
    private SharedPreferences sp;
    private Button registration;
    private Context context;
    private DBHandlerUser dbHandlerUser;
    private ProgressDialog progressBar;

    private String str_username, str_password, str_confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHandlerUser = new DBHandlerUser(this, null, null, 1);
        sp = getSharedPreferences(MY_PREFERENCES,
                Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registration = (Button) findViewById(R.id.btnSignUp);
        username = (EditText) findViewById(R.id.user_name);
        pass = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.cpassword);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                str_username = username.getText().toString().trim();
                str_password = pass.getText().toString().trim();
                str_confirm_password = confirmPassword.getText().toString().trim();


                if (str_username.equals("")) {
                    username.setError("Please enter your username!");
                    return;
                } else if (str_password.equals("")) {
                    pass.setError("Please enter your password!");
                    return;
                } else if (!str_password.equals(str_confirm_password)) {
                    confirmPassword.setError("Password and confirm password didn't match!");
                    return;
                }
                new Register().execute();
            }
        });


    }


    private class Register extends AsyncTask<String, String, Boolean> {


        @Override
        protected void onPreExecute() {
            progressBar = new ProgressDialog(context);
            progressBar.setMessage("Please Wail..");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            progressBar.cancel();
            if (!s) {
                Toast.makeText(getApplicationContext(), "User Already exist!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "User Created!!", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

        @Override
        protected void onCancelled() {

            progressBar.cancel();

            Toast.makeText(context, "Wrong Email or password!!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Boolean doInBackground(String... para) {

            SharedPreferences.Editor et = sp.edit();
            User user = new User();
            user.setUsername(str_username);
            user.setPassword(str_password);
            int id = dbHandlerUser.addUser(user);
            if (id != -1) {
                et.putInt(USER_ID, id);
                Log.d("NEW USER--", id + "");
                et.commit();
                return true;
            }
            return false;


        }
    }

}
