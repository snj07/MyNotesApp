package com.apps.sanjay.mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.sanjay.mynotes.dbase.DBHandler;
import com.apps.sanjay.mynotes.model.Note;
import com.google.common.base.Preconditions;

import org.apache.commons.lang3.StringUtils;

public class NoteActivity extends AppCompatActivity {
    private static final int ADD = 0;
    private static final int EDIT = 1;
    private int REQUEST;
    private DBHandler dbHandler;
    private static final String MY_PREFERENCES = "MyPrefs";
    private static final String USER_ID = "uIdKey";
    private SharedPreferences sp;
    private int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        dbHandler = new DBHandler(this, null, null, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notebook_toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        Preconditions.checkArgument(actionBar != null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        Intent intent = getIntent();
        REQUEST = intent.getIntExtra("REQUEST", -1);
        note_id = intent.getIntExtra("ID", 0);
        EditText notebook_title = (EditText) findViewById(R.id.request_notebook_title);
        EditText notebook_description = (EditText) findViewById(R.id.request_notebook_description);

        Preconditions.checkArgument(notebook_title != null && notebook_description != null);
        if (REQUEST == ADD) {
            actionBar.setTitle("Add a new note");
            notebook_title.setHint("Title");
            notebook_description.setHint("Description");
        }
        if (REQUEST == EDIT) {
            actionBar.setTitle("Edit note");
            notebook_title.setText(intent.getStringExtra("TITLE"));
            notebook_description.setText(intent.getStringExtra("DESC"));
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                processRequest();
                return true;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void processRequest() {
        final Intent intent_show_notes = NavUtils.getParentActivityIntent(this);
        final EditText notebook_title = (EditText) findViewById(R.id.request_notebook_title);
        final EditText notebook_description = (EditText) findViewById(R.id.request_notebook_description);

        Preconditions.checkArgument(notebook_title != null && notebook_description != null);

        String title = notebook_title.getText().toString();
        String description = notebook_description.getText().toString();

        if (StringUtils.isBlank(title) || StringUtils.isBlank(description)) {
            showMessage(this.getString(R.string.no_title_description));
            return;
        }

        final Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);


        switch (REQUEST) {
            case ADD:
                note.setUser_id(sp.getInt(USER_ID,0));
                dbHandler.addnote(note);
                startActivity(intent_show_notes);
                finish();
                break;
            case EDIT:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Updating note!!").setMessage("Are you sure you want to update ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note.setId(note_id);
                        dbHandler.updateNote(note);
                        showMessage(getApplicationContext().getString(R.string.note_update_message));
                        startActivity(intent_show_notes);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;

        }

    }
}