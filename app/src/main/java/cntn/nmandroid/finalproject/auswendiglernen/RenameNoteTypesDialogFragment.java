package cntn.nmandroid.finalproject.auswendiglernen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class RenameNoteTypesDialogFragment extends DialogFragment {

    public int getNotetypeId() {
        return notetypeId;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface RenameNoteTypesDialogListener {
        public void onDialogPositiveClickRename(DialogFragment dialog);
        public void onDialogNegativeClickRename(DialogFragment dialog);
    }


    // Use this instance of the interface to deliver action events
    RenameNoteTypesDialogListener listener;
    private int notetypeId;

    RenameNoteTypesDialogFragment(int id){

        this.notetypeId = id;
    }

    // Override the Fragment.onAttach() method to instantiate the AddNoteTypesDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AddNoteTypesDialogListener so we can send events to the host
            listener = (RenameNoteTypesDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement RenameNoteTypesDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view =inflater.inflate(R.layout.dialog_rename_note_types,null);
        builder.setView(view);


        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClickRename(RenameNoteTypesDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClickRename(RenameNoteTypesDialogFragment.this);
                    }
                });
        return builder.create();
    }
    private ArrayList<String> convertToString(){
        ArrayList<String> strs = new ArrayList<String>();
        for(NoteType noteType : MainActivity.noteTypesArrayList) {
            strs.add(noteType.getName());
        }
        return strs;
    }


}
