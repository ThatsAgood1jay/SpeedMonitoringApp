package com.example.williamjm.hw3speedappwjm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by user on 10/17/14.
 */
public class LogView extends Activity {

    ArrayList<String> logArray;
    ListView logList;

    SharedPreferences shared;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayAdapter<String> adapter;

    String entry;

    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_layout);

        shared = getPreferences(MODE_PRIVATE);
        logArray = new ArrayList<String>(shared.getAll().keySet());
        logList = (ListView) findViewById(R.id.logList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logArray);

        //get code for long click listener from homework 2

        //setup code for time picker, edit log fragment code for this

        logList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {

                dialogClick(logList);

                return true;
            }
        });

        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                String selection = logList.getItemAtPosition(position).toString();
                Uri.encode(selection);
                Intent i;
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.uta.edu/search/?q=" + selection));
                startActivity(i);

            }
        });


        loadPreferences();
    }

    public void dialogClick(View arg0) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("What would you like to do?");

        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("Share",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {


                            }
                        })
                .setNeutralButton("Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                dialog.cancel();
                                //do some stuff here......
                                //queryEdit.requestFocus();


                            }
                        })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                onDeleteClick();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void onDeleteClick()
    {
        logList.setAdapter(adapter);
        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                entry = logList.getItemAtPosition(position).toString();
                logArray.remove(entry);

                editor.remove(entry);
                editor.apply();
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void loadPreferences()
    {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String logData = sharedPreferences.getString("" , "");
        logArray.add(logData);
    }
}
