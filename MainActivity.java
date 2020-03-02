/*
Scott Bing
scottbing@cnm.edu
MainActivity.java
*/

package com.cis2237.bingp4;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String PREF_NAME = "myPreferences";
    public static final String NAME = "myName";
    public static final String MILES = "myMilesFlown";

    private int miles = 0;
    private String name = "None";

    private Button btnFindStatus, btnRestart;
    private EditText etName, etMilesFlown;
    private TextView txtInfo;
    private RewardsDbAdapter mDbAdapter;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate the objects
        etName = (EditText) findViewById(R.id.etName);
        etMilesFlown = (EditText) findViewById(R.id.etMilesFlown);
        btnFindStatus = (Button) findViewById(R.id.btnFindStatus);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        txtInfo = (TextView) findViewById(R.id.txtInfo);


        sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Handle Find Status Button
        btnFindStatus.setOnClickListener(new View.OnClickListener() {

            // get input data
            @Override
            public void onClick(View v) {

                // get and check name
                getName(savedInstanceState);
            }

        });

        // I added this method for testing and debugging purposes
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();
            }
        });

    }

    // Activity Cycle Methods
    private void releaseRewardsProgram() {
        Log.i("RewardsProgram", String.valueOf(R.string.log_release_resources));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("RewardsProgram", String.valueOf(R.string.log_onPause));
        releaseRewardsProgram();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("RewardsProgram", String.valueOf(R.string.log_onRestart));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("RewardsProgram", String.valueOf(R.string.log_onResume));
        clearPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRewardsProgram();
        Log.i("RewardsProgram", String.valueOf(R.string.log_onDestroy));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("RewardsProgram", String.valueOf(R.string.log_onDestroy));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("RewardsProgram", String.valueOf(R.string.log_onRestoreInstanceState));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("RewardsProgram", String.valueOf(R.string.log_onSaveInstanceState));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("RedeemRewards", String.valueOf(R.string.log_onCreateOptionsMenu));
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.showRules:
                startActivity(new Intent(MainActivity.this, RulesActivity.class));
                return true;
        }
        return false;
    }

    // clear preferences
    public void clearPreferences() {
        etName.setText("");
        etMilesFlown.setText("");
        txtInfo.setText("");
    }

    // get and process the name field
    public void getName(Bundle savedInstanceState) {

        // open the database;
        mDbAdapter = RewardsDbAdapter.getInstance();
        mDbAdapter.open();
        if (savedInstanceState == null) {

            initializeDatabase();

            /*//Clear all data
            mDbAdapter.deleteAllRewards();
            //Add some data
            insertSomeRewards();*/
        }


        // set up local variables
        String temp = etName.getText().toString();
        int num = 0;
        String nameMsg = "", milesMsg ="";

        // get name and check that name is not empty
        try {
            if  (temp.isEmpty()) {
                // if entry is made then throw error
                throw new IllegalArgumentException("Name is Empty - Enter a Name.");
            } else {

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(NAME, temp);
                editor.commit();

                // name is okay, get miles flown and check that it is valid
                getMilesFlown();
            }

        }
        catch (IllegalArgumentException e){ // catch the error
            txtInfo.setText(e.getMessage());
        }
    }

    // get an process miles flown field
    public void getMilesFlown() {

        int num = 0;
        String temp = "";

        // get miles flown
        String milesFlown = etMilesFlown.getText().toString();

        // accumulate miles
        try {
            num = Integer.parseInt(milesFlown) + sharedPrefs.getInt(MILES, miles);

            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putInt(MILES, num);
            editor.commit();

            // debug
            // Toast.makeText(getApplicationContext(), "Get Miles Flown " + sharedPrefs.getInt(MILES, 0), Toast.LENGTH_SHORT).show();

            // display results in StatusActivity
            startActivity(new Intent(MainActivity.this, StatusActivity.class));

        } catch (final NumberFormatException e) {
            // Toast.makeText(getApplicationContext(), "Get Miles Flown " + num + " Invalid Number", Toast.LENGTH_LONG).show();
            txtInfo.setText("Miles Flown is Invalid or Empty");
        }
    }

    private void initializeDatabase() {
        //Clear all data
        mDbAdapter.deleteAllSubscribers();
        //Add some data
        insertSomeSubscribers();
    }


    private void insertSomeSubscribers() {
        mDbAdapter.createSubscriber("Peter", "Delta", 2500);
        mDbAdapter.createSubscriber("Alyssia", "United", 6725);
        mDbAdapter.createSubscriber("Joe", "Southwest", 5500);
        mDbAdapter.createSubscriber("Shelia", "American", 1500);
        mDbAdapter.createSubscriber("Mark", "BritishAirways", 13000);
        mDbAdapter.createSubscriber("Alexa", "United", 60600);
        mDbAdapter.createSubscriber("Ryan", "United", 18000);
        mDbAdapter.createSubscriber("John", "Southwest", 106000);
        mDbAdapter.createSubscriber("Jill", "American", 9000);
        mDbAdapter.createSubscriber("Christie", "Southwest", 15000);
        mDbAdapter.createSubscriber("Diamond", "Delta", 1500);
        mDbAdapter.createSubscriber("Tuban", "Delta", 26000);
        mDbAdapter.createSubscriber("Gloria", "American", 3500);
        mDbAdapter.createSubscriber("Vickie", "Southwest", 29500);
        mDbAdapter.createSubscriber("Ryan", "United", 6000);


    }

}
