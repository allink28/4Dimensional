package com.github.allink28.android_gps_buildexample;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener {
  public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("h:mm a EEE, MMM d");      
  EditText startTimeTB, endTimeTB;
  EditText startLatTB, startLongTB, startAltTB;
  EditText currentLat, currentLong, currentAlt;
  EditText endLatTB, endLongTB, endAltTB;
  TextView summaryTV;
  Button start, mark;
  private long startTime = 0, endTime = 0;
  private static String START = "Start", STOP = "Stop";
  LocationManager locationManager;
  Location currentLocation, startLocation, endLocation;
  
  NotificationManager notificationManager;
  private static final int NOTIFICATION_ID = 0;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates("gps", 1000 * 2, 1, this); //update after 1000ms of a move of 1m
  }
  
  private void init(){
    startTimeTB = (EditText) this.findViewById(R.id.startTime);
    startLatTB = (EditText) this.findViewById(R.id.startLatitude);
    startLongTB = (EditText) this.findViewById(R.id.startLongitude);  
    startAltTB = (EditText) this.findViewById(R.id.startAlt);
    currentLat = (EditText) this.findViewById(R.id.currentLat);
    currentLong = (EditText) this.findViewById(R.id.currentLong);
    currentAlt = (EditText) this.findViewById(R.id.currentAlt);
    endTimeTB = (EditText) this.findViewById(R.id.endTime);
    endLatTB = (EditText) this.findViewById(R.id.endLat);
    endLongTB = (EditText) this.findViewById(R.id.endLong);
    endAltTB = (EditText) this.findViewById(R.id.endAlt);   
    summaryTV = (TextView) this.findViewById(R.id.summary);
    start = (Button) this.findViewById(R.id.start_button);
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
    setLocationDisplay(currentLocation, currentLat, currentLong, currentAlt);
  }
  
  public void toggleTimer(View v){
    if (startTime==0){
      startTimer(v);
      start.setText(STOP);      
    } else {
      stopTimer(v);
      start.setText(START);
      startTime = 0;
    }
  }
  
  public void startTimer(View v) {
    startLocation = currentLocation;
    startTime = System.currentTimeMillis();
    Date d = new Date(startTime);
    String formattedDate = DATE_FORMAT.format(d); 
    startTimeTB.setText(formattedDate);
    setLocationDisplay(startLocation, startLatTB, startLongTB, startAltTB);
    clearLocationDisplay();
    setNotification(formattedDate);
  }
  
  public void stopTimer(View v) {    
    endTime = System.currentTimeMillis();        
    long elapsedTime = endTime - startTime;    
    endTimeTB.setText(DATE_FORMAT.format(new Date(endTime)));
    endLocation = currentLocation;
    setLocationDisplay(endLocation, endLatTB, endLongTB, endAltTB);
    int seconds = (int) (elapsedTime/1000);
    int minutes = seconds/60;
    seconds = seconds%60;
    final DecimalFormat timeFormat = new DecimalFormat("*0##");
//    summaryTV.setText("Time: "+minutes/60 +":"+minutes%60+":"+seconds);
    summaryTV.setText("Time: "+minutes/60 +":"+timeFormat.format(minutes%60)+":"+timeFormat.format(seconds));
    notificationManager.cancel(NOTIFICATION_ID);
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
    notificationManager.notify(
        NOTIFICATION_ID, notification);    
  }
  
  public void mark(View v){
    
  }
  
  private void setLocationDisplay(Location l, EditText lat, EditText lon, EditText alt){
    if (l != null) {
      lat.setText(String.valueOf( ((int)(l.getLatitude()*100000))/100000.0));
      lon.setText(String.valueOf( ((int)(l.getLongitude()*100000))/100000.0));
      alt.setText(String.valueOf( Math.round(l.getAltitude())));
    }
  }
  private void clearLocationDisplay(){    
    String emptyString = "";
    endTimeTB.setText(emptyString);
    endLatTB.setText(emptyString);
    endLongTB.setText(emptyString);
    endAltTB.setText(emptyString);
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
