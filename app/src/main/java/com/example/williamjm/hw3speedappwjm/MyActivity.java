package com.example.williamjm.hw3speedappwjm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    LocationListener locationListener;
    LocationManager locationManager;

    ArrayList<String> logArray;
    ListView logList;
    ArrayAdapter<String> adapter;

    SharedPreferences shared;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SeekBar speedBar;
    TextView speedSet;
    Button currentSpeed;

    final Context context = this;

    String entry;

    //for date picker
    private int mYear, mMonth, mDay;
    String txtDate;
    TextView testText;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        testText = (TextView) findViewById(R.id.testText);

        shared = getPreferences(MODE_PRIVATE);
        logArray = new ArrayList<String>(shared.getAll().keySet());
        logList = (ListView) findViewById(R.id.logList);

        speedBar = (SeekBar)findViewById(R.id.speedBar);
        speedBar.setOnSeekBarChangeListener(this);
        speedSet = (TextView)findViewById(R.id.speedSet);
        currentSpeed = (Button)findViewById(R.id.currentSpeed);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logArray);

        speedSet.setText(String.valueOf(0));

        speedCheck();
        loadPreferences();

       logList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {

               dialogClick(logList);

               return true;
           }
       });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.d("Tags", "in OnCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    //for opening up the log
    public void onButtonClick(View v)
    {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtDate =(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        testText.setText(txtDate);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void speedCheck()
    {
        Log.d("MyActivity", "In on speedcheck");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();


                float x;
                float y;
                double conversion;
                String s;
                int z;

                //getting the speed
                x = location.getSpeed();
                //for converting to MPH from MPS
                conversion = 2.2369362920544;
                //remember to reset this for the final app
                y = 50; //(float) conversion * x;
                //for storing the value of speed conversion
                s = String.valueOf(y);

                currentSpeed.setText(String.valueOf(s));

                z =  Integer.parseInt((String)speedSet.getText());

                if(y > z) {
                    Toast.makeText(MyActivity.this, "You are speeding", Toast.LENGTH_SHORT).show();
                    saveData();
                }

                Log.d("MyActivity", "In on speedcheck loop");

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}

        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);

    }

    public void saveData()
    {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        logList.setAdapter(adapter);
        String setSpeed = MyActivity.this.speedSet.getText().toString();
        String currentSpeed = MyActivity.this.currentSpeed.getText().toString();

        logArray.add(currentSpeed + " : " + setSpeed + " : " + testText.getText() + " : " + date);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(currentSpeed, setSpeed);

        editor.apply();

        loadPreferences();
    }

    private void loadPreferences()
    {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String logData = sharedPreferences.getString("log" , "");
        logArray.add(logData);
    }


    //for the speedBar
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        speedSet.setText(String.valueOf(progress));

    }

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void onStopTrackingTouch(SeekBar seekBar) {

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

                                FragmentManager fm = getFragmentManager();
                                logFragment alert = new logFragment();
                                alert.show(fm, "Dialog List");
                            }
                        })
                .setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                dialog.cancel();


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
        //ask why this allows you to delete things by clicking on them!!!!
        logList.setAdapter(adapter);
        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                sharedPreferences = getPreferences(MODE_PRIVATE);
                editor = sharedPreferences.edit();

                entry = logList.getItemAtPosition(position).toString();
                logArray.remove(entry);

                editor.remove(entry);
                editor.apply();
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag", "In on Pause");
    }

    @Override
    protected void onDestroy()
    {
        Log.d("tag", "In on Destroy");
        super.onDestroy();
       // locationManager.removeUpdates(locationListener);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
