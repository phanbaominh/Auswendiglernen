package cntn.nmandroid.finalproject.auswendiglernen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AddNoteTypesDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddNoteTypesDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddNoteTypesDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the AddNoteTypesDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AddNoteTypesDialogListener so we can send events to the host
            listener = (AddNoteTypesDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddNoteTypesDialogListener");
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
        View view =inflater.inflate(R.layout.dialog_add_note_types,null);
        builder.setView(view);

        Spinner spinner = view.findViewById(R.id.spinner_note_types_dialog_note_types);
        createSpinner(spinner,convertToString());

        builder.setTitle(R.string.dialog_title_note_types)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(AddNoteTypesDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(AddNoteTypesDialogFragment.this);
                    }
                });
        return builder.create();
    }
    private ArrayList<String> convertToString(){
        ArrayList<String> strs = new ArrayList<String>();
        for(Data data : MainActivity.dataArrayList) {
            strs.add(data.getText());
        }
        return strs;
    }
    private void createSpinner(Spinner spinner,ArrayList<String> items){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.support_simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
    }

}
