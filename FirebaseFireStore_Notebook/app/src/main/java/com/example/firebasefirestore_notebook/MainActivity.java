package com.example.firebasefirestore_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasefirestore_notebook.model.Note;
import com.example.firebasefirestore_notebook.noteadapter.NoteAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    //Member variables
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Reference to the database
    private CollectionReference notebookRef = db.collection("notes"); // Reference to the collection
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note); //Only need it here, so not a member variable
        buttonAddNote.setOnClickListener(new View.OnClickListener() { //Onclick listener
            @Override
            public void onClick(View v) {
                //Starts activity, new intent, to go to NewNoteActivity
                startActivity(new Intent(MainActivity.this,NewNoteActivity.class));
            }
        });

        setUpRecyclerView(); //Connect recyclerview to adapter, put in a method
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("title");
        //Is how query gets into the adapter
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();

        adapter = new NoteAdapter(options); //Assign adapter variable
        RecyclerView recyclerView = findViewById(R.id.reycler_view); // Ref to recycler_view
        recyclerView.setHasFixedSize(true); // For performance only
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //needs a layoutmanager
        recyclerView.setAdapter(adapter); // sets adapter

        //touch helper for drag and drop or swipe actions
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, //Drag dir, dont want hence 0
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { //swipe direction left or right.
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition()); //gets pos from viewholder
            }
        }) .attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() { //An annonymous inner class
            //Instead of implenting interface the "normal" way from NoteAdapter
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(MainActivity.this,NewNoteActivity.class);
                // String id =
                // intent.putExtra()
                startActivity(new Intent(MainActivity.this,NewNoteActivity.class));
            }
        });
    }

    //Need to tell when to start listening
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //Will stop listening if app goes into the background.
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
