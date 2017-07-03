package com.apps.sanjay.mynotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.sanjay.mynotes.dbase.DBHandlerUser;


public class LoginActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "MyPrefs";
    private static final String USER_ID = "uIdKey";

    private EditText usernameEt, passwordEt;
    private Button submit;
    private TextView register;
    private String username, pass;
    private Context context;
    private ProgressDialog progressBar;
    private DBHandlerUser dbHandlerUser;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEt = (EditText) findViewById(R.id.uname);
        passwordEt = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.btnLogin);
        register = (TextView) findViewById(R.id.btn_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sp = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        if (sp.getInt(USER_ID, -1) != -1) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;

        }
        context = this;
        dbHandlerUser = new DBHandlerUser(this, null, null, 1);
        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {


                                          username = usernameEt.getText().toString();
                                          pass = passwordEt.getText().toString();

                                          v.getContext();

                                          if (username == null || username.equals("")) {
                                              usernameEt.setError("Blank!!");
                                              return;
                                          }
                                          if (pass == null || pass.equals("")) {
                                              passwordEt.setError("Blank!!");
                                              return;
                                          }
                                          new Login().execute();
                                      }

                                  }

        );
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });


    }

    private class Login extends AsyncTask<String, String, Boolean> {

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
            if (!s.booleanValue()) {
                Toast.makeText(context, "Wrong Email or password!!", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
            int id = dbHandlerUser.getUser(username, pass);
            if (id != -1) {
                et.putInt(USER_ID, id);
                Log.d("ID---", id + "");
                et.commit();
                return true;
            }
            return false;
        }
    }
}
