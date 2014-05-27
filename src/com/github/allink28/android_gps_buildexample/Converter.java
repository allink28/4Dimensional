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

//  public static String formateCoordinate(double coordinate){
//    TODO
//  }
  
//  public static String decimalDegreesToDMS(double coordinate){
//   TODO 
//  }
  
//  public static String metricToImperial(double distance){
//   TODO 
//  }
  

}
