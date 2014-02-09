package com.github.allink28.android_gps_buildexample;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
  SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a EEE, MMM d");      
  EditText startTimeTB;
  EditText startLatTB, startLongTB;
  EditText currentLat, currentLong, currentAlt;
  Button start, mark;
  private long startTime = 0, endTime = 0;
  private static String START = "Start", STOP = "Stop";
  LocationManager locationManager;
  Location currentLocation;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    startTimeTB = (EditText) this.findViewById(R.id.startTime);
    startLatTB = (EditText) this.findViewById(R.id.startLatitude);
    startLongTB = (EditText) this.findViewById(R.id.startLongitude);    
    currentLat = (EditText) this.findViewById(R.id.currentLat);
    currentLong = (EditText) this.findViewById(R.id.currentLong);
    currentAlt = (EditText) this.findViewById(R.id.currentAlt);
    
    start = (Button) this.findViewById(R.id.start_button);
    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
    currentLocation = location;
    Log.i("locationChanged:gps build example", "Latitude: "+ location.getLatitude() 
        +", Longitude: "+ location.getLongitude() + ", Altitude: "+ location.getAltitude()+
        ", speed: "+location.getSpeed());    
//    location.distanceTo(dest)
//  Date d = new Date(location.getTime());
    currentLat.setText(String.valueOf(location.getLatitude()));
    currentLong.setText(String.valueOf(location.getLongitude()));
    currentAlt.setText(String.valueOf(location.getAltitude()));    
  }
  
  public void toggleTimer(View v){
    if (startTime==0){
      startTimer(v);
      start.setText(START);
    } else {
      stopTimer(v);
      start.setText(STOP);
      startTime = 0;
    }
  }
  
  public void setStartView(Marker m){
    
  }
  
  public void startTimer(View v) {
    startTime = System.currentTimeMillis();
    Date d = new Date(startTime);
    startTimeTB.setText(dateFormat.format(d));
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
//    Button b = (Button) this.findViewById(R.id.start_button);
//    b.setText("Start");
  }
  
  public void mark(View v){
    
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
