package com.example.firebasefirestore_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasefirestore_notebook.model.Note;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity {
    //Member variables
    private EditText editTextTitle;
    private EditText editTextDescription;
    String noteId; // global to keep the document id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        //Assigning the membervariables
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        transferText();
    }

    private void transferText() {
        Intent intent = getIntent(); //Intent to get extra from MainActivity
        noteId = intent.getStringExtra("noteId"); //Gets the noteId/the reference
        String title = intent.getStringExtra("title");  //Gets text from MainActivity
        String description = intent.getStringExtra("description");
        editTextTitle.setText(title); // Sets the recieved text to the view when clicked
        editTextDescription.setText(description);


    }

    @Override //To use optionsmenu, overriding it
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater(); // A menu inflater
        menuInflater.inflate(R.menu.new_note_menu, menu); //Passing in from menu package & menu variable
        return super.onCreateOptionsMenu(menu);
    }

    @Override //To tell what to do when item selected in menu
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) //Takes the item passed from method, which is what has been clicked
        {
            //What to do if X option is clicked
            case R.id.save_note: //save_note is the ID of item in new_note_menu.xml
                saveNote(); // Execute save note method
                return true; //Tells system its done/consumed
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString(); //Gets text from the edittext fields
        String description = editTextDescription.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty()) { //trim to not allow blankspaces, if it is NOT empty create new note
            //Toast msg if nothing is entered.
            Toast.makeText(this, "Please enter a title and description!", Toast.LENGTH_SHORT).show();
            return; //leaves the save method in this case, so nothing is saved

        } else if (noteId != null) { // has to be != null to avoid nullpointer exception, checks if noteId is existing to edit and not create a new.
            saveEditedNote(title, description); //Saves editted note with method
        } else {
            createNewNote(title, description); // Creates a new note
        }

    }

    private void saveEditedNote(String title, String description) {
        DocumentReference notebookRef = FirebaseFirestore.getInstance().collection("notes").document(noteId); //Ref to the selected document with noteId variable
        notebookRef.update("title",title,"description",description);
        Toast.makeText(this, "Edit saved!", Toast.LENGTH_SHORT).show(); //Feedback to user
        finish(); // closes/returns to recyclerview
    }

    private void createNewNote(String title, String description) {
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("notes"); //Ref to the db collection, where the note should be saved
        notebookRef.add(new Note(title, description)); // Adds whats defined in model, title and description
        Toast.makeText(this, "Note added!", Toast.LENGTH_SHORT).show(); //Feedback to user
        finish(); // closes/returns to recyclerview
    }
}
