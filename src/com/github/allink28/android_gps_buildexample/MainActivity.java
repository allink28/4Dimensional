package com.github.allink28.android_gps_buildexample;

import java.util.Calendar;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener{
    
  EditText longTextbox;//Longitude textbox
  EditText latTextbox;//lattitude textbox
  private long startTime = 0;
  private long endTime = 0;
  private int lap = 0;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    longTextbox = (EditText) this.findViewById(R.id.editText1);
    latTextbox = (EditText) this.findViewById(R.id.editText2);
    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates("gps", 1000 * 2, 1, this); //update after 1000ms of a move of 1m
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.i("locationChanged:gps build example", "Latitude: "+ location.getLatitude() );
    Log.i("locationChanged:gps build example", "Longitude: "+ location.getLongitude() );    
    Log.i("locationChanged:gps build example", "Altitude: "+ location.getAltitude() );
    
    longTextbox.setText(String.valueOf(location.getLongitude()));
    latTextbox.setText(String.valueOf(location.getLatitude()));
    
  }
  
  public void startTimer(View v) {
    startTime = System.currentTimeMillis();
    Log.i("StartTimer", "Start clicked");
//    TextView myTextView = (TextView) this.findViewById(R.id.fullscreen_content);
//    myTextView.setText("Started on\n"+Calendar.getInstance().getTime());
//    Button b = (Button) this.findViewById(R.id.start_button);
//    b.setText("Restart");
  }
  
  
  
  public void stopTimer(View v) {    
    endTime = System.currentTimeMillis();
    long elapsedTime = endTime - startTime;
    Log.i("stopTimer", "elapsedTime: "+elapsedTime +" ms");
//    TextView myTextView = (TextView) this.findViewById(R.id.fullscreen_content);
//    myTextView.setText("Elapsed Time: "+elapsedTime/1000.0 +"s");
    startTime = 0;
//    Button b = (Button) this.findViewById(R.id.start_button);
//    b.setText("Start");
    lap = 0;
  }

  @Override
  public void onProviderDisabled(String provider) {
    // TODO Auto-generated method stub
    Log.i("onProviderDisabled", "onProviderDisabled("+provider+") not yet implemented");
  }

  @Override
  public void onProviderEnabled(String provider) {
    // TODO Auto-generated method stub
    Log.i("onProviderEnabled", "onProviderEnabled("+provider+") not yet implemented");
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub
    Log.i("onStatusChanged", "onStatusChanged("+provider+", "+ status+", "+extras+") not yet implemented");
  }

}
