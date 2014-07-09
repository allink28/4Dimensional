/**
 * 
 */
package com.github.allink28.android_gps_buildexample;

import java.text.DecimalFormat;

/**
 * This class is for unit conversions that otherwise clutter up the 
 * MainActivity
 * @author Allen Preville
 *
 */
public class Converter {
  private static final DecimalFormat TIME_FORMAT = new DecimalFormat("*0##");

  /**
   * @param distance Distance in meters, given by GPS
   * @return Formated distance and unit
   */
  public static String formatDistance(float distance){
    if (distance < 1000){ //Less than 1 kilometer
      return "~"+Math.round(distance)+" m";
    }
    if (distance < 100000){ //Less than 100 kilometers 
      return Math.round(distance/100)/10.0+" km";
    }
    return Math.round(distance/1000)+" km";    
  }
  
  public static String formatTime(long time){    
    int seconds = (int) time/1000;
    int hours = seconds/(3600);
    seconds -= hours * 3600;
    int minutes = seconds/60;
    seconds -= minutes * 60;
    if (time <= (1000 * 60)) { //If total time is less than a minute
      return seconds + " seconds";
    }
    if (time <= (1000 * 60 * 60)) { //If total time is less than an hour
      return TIME_FORMAT.format(minutes) + ":" + TIME_FORMAT.format(seconds);
    }
    return TIME_FORMAT.format(hours) + ":" + TIME_FORMAT.format(minutes) + 
        ":" + TIME_FORMAT.format(seconds);
  }

  public static String formatCoordinate(double coordinate, boolean useDMS){
    if (useDMS) {
      return decimalDegreesToDMS(coordinate);
    }
    return ((int)(coordinate*100000))/100000.0  +"°";
  }
  
//The whole units of degrees will remain the same (i.e. in 121.135° longitude, start with 121°).
//Multiply the decimal by 60 (i.e. .135 * 60 = 8.1).
//The whole number becomes the minutes (8').
//Take the remaining decimal and multiply by 60. (i.e. .1 * 60 = 6).
//The resulting number becomes the seconds (6"). Seconds can remain as a decimal.
//Take your three sets of numbers and put them together, using the symbols for degrees (°), minutes (‘), and seconds (") (i.e. 121°8'6" longitude)
  //examples: 121.135 degrees == 121° 8' 6"
  //          51.477222 degrees == 51° 28' 37.9986"
  public static String decimalDegreesToDMS(double coordinate){
    String[] dms = String.valueOf(coordinate).split("\\.");
    String decimal = (dms.length == 1 || dms[1].length() == 0)? "0" : dms[1];
    coordinate = Double.valueOf("."+decimal) * 60;
    int minutes = (int) coordinate;
    double seconds = (coordinate - minutes) * 60;
    
    return dms[0]+"° " + minutes+"' " + Math.round(seconds*10)/10.0+"\""; 
  }
 
  
//  public static String metricToImperial(double distance){
//   TODO 
//  }
  

}
