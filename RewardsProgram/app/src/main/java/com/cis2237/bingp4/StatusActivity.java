/*
Scott Bing
scottbing@cnm.edu
StatusActivity.java
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class StatusActivity extends AppCompatActivity {

    public static final String PREF_NAME = "myPreferences";
    public static final String NAME = "myName";
    public static final String MILES = "myMilesFlown";
    public static final String AIRLINE = "myAirline";
    public static final String STATUS = "myStatus";

    private String miles = "0";
    private String name = "None";
    private String airline = "None";
    private String status = "No Rewards";

    private RewardsDbAdapter mDbAdapter;

    public TextView txtName, txtMilesAccumulated, txtAirline, txtStatus;
    public Button btnRedeemRewards;
    public ImageView imgReward;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // instantiate Objects
        txtName = (TextView)findViewById(R.id.txtName);
        txtMilesAccumulated = (TextView)findViewById(R.id.txtMilesAccumulated);
        txtAirline = (TextView)findViewById(R.id.txtAirline);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        btnRedeemRewards = (Button)findViewById(R.id.btnRedeemRewards);
        imgReward = (ImageView) findViewById(R.id.imgReward);

        mDbAdapter = new RewardsDbAdapter(this);
        mDbAdapter = RewardsDbAdapter.getInstance();
        mDbAdapter.open();

        /*// get and display current values
        txtName.setText(sharedPrefs.getString(NAME, "None"));
        txtAirline.setText(sharedPrefs.getString(AIRLINE, "None"));
        miles = sharedPrefs.getInt(MILES,0);
        txtMilesAccumulated.setText(Integer.toString(miles));
*/
        // debug
        // Toast.makeText(getApplicationContext(), "before Determine Rewards ", Toast.LENGTH_LONG).show();
        // determine Rewards
        determineRewards();
        // debug
        // Toast.makeText(getApplicationContext(), "after Determine Rewards ", Toast.LENGTH_LONG).show();

        // Handle Redeem Rewards Button
        btnRedeemRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(StatusActivity.this, RedeemRewardsActivity.class));

            }
        });

    }


    // Activity Cycle Methods
    private void releaseStatusActivity() {
        Log.i("StatusActivity", String.valueOf(R.string.log_release_resources));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("StatusActivity", String.valueOf(R.string.log_onPause));
        releaseStatusActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("StatusActivity", String.valueOf(R.string.log_onRestart));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("StatusActivity", String.valueOf(R.string.log_onResume));
        displayPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseStatusActivity();
        Log.i("StatusActivity", String.valueOf(R.string.log_onDestroy));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("StatusActivity", String.valueOf(R.string.log_onDestroy));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("StatusActivity", String.valueOf(R.string.log_onRestoreInstanceState));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("StatusActivity", String.valueOf(R.string.log_onSaveInstanceState));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("RedeemRewards", String.valueOf(R.string.log_onCreateOptionsMenu));
        getMenuInflater().inflate(R.menu.status, menu);
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
                startActivity(new Intent(StatusActivity.this, RulesActivity.class));
                return true;

            case R.id.action_Main:
                startActivity(new Intent(StatusActivity.this, MainActivity.class));
                return true;
        }
        return false;
    }

    public void determineRewards() {

        int milesBalance = Integer.parseInt(miles);
        // determine Rewards
        if (milesBalance >= 25000 && milesBalance < 50000) {  // Bronze Status
            status = "Bronze Status";
            txtStatus.setText(status);
            imgReward.setImageResource(R.drawable.bronze);
        }
        else if (milesBalance >= 50000 && milesBalance < 75000) {  // Silver Status
            status = "Silver Status";
            txtStatus.setText(status);
            imgReward.setImageResource(R.drawable.silver);
        }
        else if (milesBalance >= 75000) {  // Gold Status
            status = "Gold Status";
            txtStatus.setText(status);
            imgReward.setImageResource(R.drawable.gold);
        }
        else {
            status = "No Rewards";
            txtStatus.setText(status);
            imgReward.setImageResource(R.drawable.rewards);
        }

        // debug
        // Toast.makeText(getApplicationContext(), "Status in Status Activity: " + sharedPrefs.getString(STATUS, ""), Toast.LENGTH_LONG).show();

    }

    private void displayPreferences() {
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("USER_NAME");
        //Customer customer = mDbAdapter.fetchCustomerByName(user_name);
        Customer customer = mDbAdapter.fetchCustomerByName("Vickie");
        name = customer.getName();
        txtName.setText(name);

        miles = customer.getMiles();
        txtMilesAccumulated.setText(miles);

        airline = customer.getAirline();
        txtAirline.setText(airline);

        status = customer.getStatus();
        txtStatus.setText(status);
    }

}
