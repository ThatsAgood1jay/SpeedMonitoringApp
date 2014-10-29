package com.example.williamjm.hw3speedappwjm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by user on 10/13/14.
 */
public class logFragment extends DialogFragment {
    String shareArray[] = {"Facebook", "Twitter", "MySpace"};


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Share On")
                .setItems(shareArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Sharing on " + shareArray[which] + "!", Toast.LENGTH_SHORT).show();



                    }
                });

        return builder.create();
    }


}



