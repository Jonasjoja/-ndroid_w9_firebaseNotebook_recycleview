package com.example.firebasefirestore_notebook.noteadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasefirestore_notebook.R;
import com.example.firebasefirestore_notebook.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
// Adapterclass
// Subclass of recycleview adapter
// Takes care of loading data from firestore and reacting to changes

//Firestore adapter, needs class of viewholder and name of model class passed in
public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {
    private OnItemClickListener listener; //created in this class
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder noteHolder, int position, @NonNull Note note) {
        //Tells the adapter what to put in each view in layout
        noteHolder.textViewTitle.setText(note.getTitle()); //takes holder, set text, passed model and uses get.
                                                           //Tells adapter to put title of note into title
        noteHolder.textViewDescription.setText(note.getDescription());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Tells the adapter which layout to inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,
                parent,false); //Context parent, is the viewgroup passed, is the recyclerview

        return new NoteHolder(v);
    }

    public void deleteItem(int position)
    {
        //getSnapshots returns all doc snapshots
        //Get with position passed returns specific doc snapshot in the recyclerview
        //getRef. for document reference for the document, then delete.
        getSnapshots().getSnapshot(position).getReference().delete();
        //Notify dataset changed NOT needed.
        // Firestore recycleadapter automatically detects changes, update and notify
    }


    //Inner class, viewholder
    class NoteHolder extends RecyclerView.ViewHolder{
        //Variables for views in note item layout.
        TextView textViewTitle;
        TextView textViewDescription;

        public NoteHolder(@NonNull View itemView) { // Constructor of the viewholder
            super(itemView);
            //Assigning the variables
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            deleteLongClick(itemView); // long click method

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) //If item isnt valid it can crash. Safety check.
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position); //Same as in delete method
                    }
                }
            });

        }

        private void deleteLongClick(@NonNull View itemView) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() { //Deletes on longclick
                @Override
                public boolean onLongClick(View v) {
                    //int pos = getAdapterPosition();
                    deleteItem(getAdapterPosition());
                 return false;
                }
            });
        }


    }
    public interface OnItemClickListener{ //Good practise for reusability
        //can recreate whole note object, get the ID from firestore ect.
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
