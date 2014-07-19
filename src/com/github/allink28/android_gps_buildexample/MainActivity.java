package com.github.allink28.android_gps_buildexample;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements LocationListener {
  public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("h:mm a EEE, MMM d");      
  EditText startTimeTB, endTimeTB;
  EditText startLatTB, startLongTB;
  EditText currentLat, currentLong;
  EditText endLatTB, endLongTB;
  TextView summaryTV;
  ToggleButton start;
  Button mark;
  private long startTime = 0, endTime = 0;
  float distance;
  SharedPreferences settings;
  LocationManager locationManager;
  Location currentLocation, startLocation, endLocation;
  
  NotificationManager notificationManager;
  private static final int NOTIFICATION_ID = 0;
  
  // -------------- Activity Lifecycle methods --------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
    init();    
  }
  private void init(){
    startTimeTB = (EditText) this.findViewById(R.id.startTime);
    startLatTB = (EditText) this.findViewById(R.id.startLatitude);
    startLongTB = (EditText) this.findViewById(R.id.startLongitude);  
    currentLat = (EditText) this.findViewById(R.id.currentLat);
    currentLong = (EditText) this.findViewById(R.id.currentLong);
    endTimeTB = (EditText) this.findViewById(R.id.endTime);
    endLatTB = (EditText) this.findViewById(R.id.endLat);
    endLongTB = (EditText) this.findViewById(R.id.endLong);
    summaryTV = (TextView) this.findViewById(R.id.summary);
    start = (ToggleButton) this.findViewById(R.id.start_button);
    
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER ,
        1000 * Integer.valueOf(settings.getString(getString(R.string.update_time), "5")),//ms to update after 
        Integer.valueOf(settings.getString(getString(R.string.update_dist), "3")),//meters to update after
        this);
  }
  

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()) {
    case R.id.action_settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // ----------------------- Activity Methods ---------------------------------
  
  @Override
  public void onLocationChanged(Location newLocation) {
    if (start.isChecked() && newLocation != null && currentLocation != null){
      distance += newLocation.distanceTo(currentLocation);
      boolean useMiles = settings.getBoolean(getString(R.string.useMiles), false);
      summaryTV.setText("Distance traveled: "+Converter.formatDistance(distance, useMiles));    
    }
    currentLocation = newLocation;
    setLocationDisplay(currentLocation, currentLat, currentLong);
  }
  
  /**
   * Executes the correct action when the start/stop button is pressed,
   * and resets values when the action is finished.
   */
  public void toggleTimer(View v){
    if (start.isChecked()){
      startTimer(v);
      endTime = 0;
      distance = 0;
    } else {
      stopTimer(v);
      startTime = 0;
    }
  }
  
  private void startTimer(View v) {
    startLocation = currentLocation;
    startTime = System.currentTimeMillis();
    Date d = new Date(startTime);
    String formattedDate = DATE_FORMAT.format(d); 
    startTimeTB.setText(formattedDate);
    setLocationDisplay(startLocation, startLatTB, startLongTB);
    clearLocationDisplay();
    setNotification(formattedDate);
  }
  
  private void stopTimer(View v) {    
    endTime = System.currentTimeMillis();        
    endTimeTB.setText(DATE_FORMAT.format(new Date(endTime)));
    endLocation = currentLocation;
    notificationManager.cancel(NOTIFICATION_ID);
    setLocationDisplay(endLocation, endLatTB, endLongTB);
    setLocationDisplay(startLocation, startLatTB, startLongTB);
 
    
    StringBuilder sb = new StringBuilder();
    boolean useMiles = settings.getBoolean(getString(R.string.useMiles), false);
    if (distance != 0){      
      sb.append("Distance traveled: "+Converter.formatDistance(distance, useMiles)+"\n");    
    }
    sb.append("Time: "+Converter.formatTime(endTime - startTime));
    if (startLocation != null && endLocation != null){
      sb.append("\nDisplacement: "+Converter.formatDistance(startLocation.distanceTo(endLocation), useMiles));
    }
    summaryTV.setText(sb.toString());    
  }
  
  @SuppressWarnings("deprecation")
  public void setNotification(String startDate){
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);    
    Notification notification = new Notification();
    notification.icon = R.drawable.ic_launcher;
    notification.tickerText = "Timer running. Started at: "+startDate;
    notification.number = 0;
    notification.setLatestEventInfo(getApplicationContext(), //api 14 compatible
        "4Dimensional - Timer running", 
        "Started at: "+startDate, 
        pendingIntent);    
//    Notification noti = new Notification.Builder(getApplicationContext()) //for api 16
//          .setContentTitle("Content title")
//          .setContentText("content text") //setSmallIcon(R.drawable.new_email).setLargeIcon(aBitmap)
//          .setContentIntent(pendingIntent)
//          .build();
    notificationManager.notify(NOTIFICATION_ID, notification);    
  }
  
  /**
   * Save the current coordinate  
   */
  public void mark(View v){
    //TODO
  }
  
  
  // ------------------------- Helper methods ---------------------------------
  
  private void setLocationDisplay(Location l, EditText lat, EditText lon){
    if (l != null) {
      boolean useDMS = settings.getBoolean(getString(R.string.useDMS), false);
      lat.setText(String.valueOf(Converter.formatCoordinate(l.getLatitude() , useDMS)));
      lon.setText(String.valueOf(Converter.formatCoordinate(l.getLongitude(), useDMS)));
    } else {
      lat.setText(null);
      lon.setText(null);
    }
  }
  private void clearLocationDisplay(){    
    String emptyString = "";
    summaryTV.setText(emptyString);
    endTimeTB.setText(emptyString);
    endLatTB.setText(emptyString);
    endLongTB.setText(emptyString);
  }
  
  
  
  // ------------------------ Unimplemented Methods ---------------------------

  @Override
  public void onProviderDisabled(String provider) {
    // TODO: blank out current location?
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
