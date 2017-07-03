package com.apps.sanjay.mynotes.adapter;
/**
 * Created by sanjay on 13/6/17.
 */


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.sanjay.mynotes.NoteActivity;
import com.apps.sanjay.mynotes.R;
import com.apps.sanjay.mynotes.dbase.DBHandler;
import com.apps.sanjay.mynotes.model.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    DBHandler dbHandler;
    private List<Note> mNotes;
    private Context context;

    public NotesAdapter(Context context, List<Note> Notes) {
        mNotes = Notes;
        this.context = context;
        dbHandler = new DBHandler(context, null, null, 1);
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void delete(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete note").setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler.deleteNote(mNotes.get(position).getId());
                mNotes.remove(position);
                notifyItemRemoved(position);
                showMessage(context.getString(R.string.note_delete_message));
                NotesAdapter.this.notifyItemRemoved(position);
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void edit(int position) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("notebookId", mNotes.get(position).getId());
        intent.putExtra("REQUEST", 1); // EDIT
        intent.putExtra("ID", mNotes.get(position).getId());
        intent.putExtra("TITLE", mNotes.get(position).getTitle());
        intent.putExtra("DESC", mNotes.get(position).getDescription());
        context.startActivity(intent);
        ((Activity) context).finish();
        notifyItemChanged(position);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView contentTextView;
        public ImageView optionsButton;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.note_title);
            contentTextView = (TextView) itemView.findViewById(R.id.note_content);
            optionsButton = (ImageView) itemView.findViewById(R.id.note_options_button);
        }

        public void bind(Note note, final int position) {
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, optionsButton);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    edit(getAdapterPosition());
                                    break;
                                case R.id.delete:
                                    delete(getAdapterPosition());
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View NoteView = inflater.inflate(R.layout.item_note, parent, false);

        return new ViewHolder(NoteView, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mNotes.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
}