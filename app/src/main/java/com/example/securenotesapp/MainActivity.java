package com.example.securenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesListManager listManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //login listener
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(onClickLoginButton);
    }

    //login
    private View.OnClickListener onClickLoginButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText usernameInput = findViewById(R.id.usernameInput);
            EditText passwordInput = findViewById(R.id.passwordInput);
            TextView errorTextView = findViewById(R.id.errorTextView);

            String userName = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            LoginManager loginManager = new LoginManager(userName, password);


            if (loginManager.hasValidCredentials()) {
                //Success
                setContentView(R.layout.notes_page);
                createNoteList();
            } else {
                //Invalid
                errorTextView.setText(getString(R.string.errmsg_Login));
                errorTextView.setVisibility(View.VISIBLE);
            }
        }
    };

    //note list - call on create
    private void createNoteList() {
        ListView notesList = findViewById(R.id.notes_list);
        ImageButton addButton = findViewById(R.id.add_note);

        listManager = new NotesListManager();
        NotesAdapter adapter = new NotesAdapter(this, listManager.getNotes());
        notesList.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClick();
            }
        });
    }

    //note list - call every time after createNoteList
    private void produceNoteList() {
        ListView notesList = findViewById(R.id.notes_list);
        ImageButton addButton = findViewById(R.id.add_note);

        NotesAdapter adapter = new NotesAdapter(this, listManager.getNotes());
        notesList.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClick();
            }
        });
    }

    //add new note (button)
    private void onAddButtonClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_note);

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setNegativeButton(
                android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Notes item = new Notes(input.getText().toString());
                listManager.addNote(item);
            }
        });

        builder.show();
    }

    private class NotesAdapter extends ArrayAdapter<Notes> {

        private Context context;
        private List<Notes> items;

        private NotesAdapter(Context context, List<Notes> items) {
            super(context, -1, items);

            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.each_note_layout, parent, false);
            }
            final TextView noteName = convertView.findViewById(R.id.noteName);
            ImageButton deleteButton = convertView.findViewById(R.id.delete_note);

            noteName.setText(items.get(position).getTitle());

            convertView.setTag(items.get(position));

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.remove(position);
                    notifyDataSetChanged();
                }
            });

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //note page call
                    setContentView(R.layout.note_textfield_layout);

                    TextView noteName_Static = findViewById(R.id.noteName_Static);
                    final EditText noteContent = findViewById(R.id.noteContent);
                    ImageButton saveButton = findViewById(R.id.saveButton);

                    noteName_Static.setText(items.get(position).getTitle());
                    noteName_Static.setVisibility(View.VISIBLE);

                    if (items.get(position).getContent() != null && items.get(position).getContent().length() > 0) {
                        noteContent.setText(items.get(position).getContent());
                        noteContent.setVisibility(View.VISIBLE);
                    }

                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            items.get(position).setContent(noteContent.getText().toString());
                            notifyDataSetChanged();
                            setContentView(R.layout.notes_page);
                            produceNoteList();
                        }
                    });
                }
            };

            convertView.setOnClickListener(onClickListener);
            noteName.setOnClickListener(onClickListener);

            return convertView;
        }
    }
}
