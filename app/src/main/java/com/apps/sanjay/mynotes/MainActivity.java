package com.apps.sanjay.mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.sanjay.mynotes.adapter.NotesAdapter;
import com.apps.sanjay.mynotes.dbase.DBHandler;
import com.apps.sanjay.mynotes.model.Note;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private NotesAdapter adapter;
    private static final String MY_PREFERENCES = "MyPrefs";
    private static final String USER_ID = "uIdKey";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        dbHandler = new DBHandler(this, null, null, 1);
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        RecyclerView rvNotebooks = (RecyclerView) findViewById(R.id.notebooks_list);
        TextView noNotebooks = (TextView) findViewById(R.id.no_notebooks);

        sp = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        Log.d("ID_SEL --", sp.getInt(USER_ID, 0) + "");
        ArrayList<Note> notes_list = dbHandler.getnotes(sp.getInt(USER_ID, 0));

        Preconditions.checkArgument(rvNotebooks != null && mainLayout != null && noNotebooks != null);

        if (!notes_list.isEmpty()) {
            noNotebooks.setVisibility(View.INVISIBLE);
            adapter = new NotesAdapter(this, notes_list);
            rvNotebooks.setAdapter(adapter);
            rvNotebooks.setLayoutManager(new LinearLayoutManager(this));

        } else {
            rvNotebooks.setVisibility(View.INVISIBLE);
            noNotebooks.setText(R.string.no_notebooks_message);
        }

        FloatingActionButton add_notebook_FAB = (FloatingActionButton) findViewById(R.id.add_notebook_FAB);
        Preconditions.checkArgument(add_notebook_FAB != null);
        add_notebook_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("REQUEST", 0); // ADD
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyItemInserted(adapter.getItemCount());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), About.class));
                return true;

            case R.id.action_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logging out!!").setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
