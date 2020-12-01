package com.example.project;
import java.io.Serializable;
import java.util.*;

/**
 * 확진자 방문 장소 저장할 class
 */
public class VisitPlace implements Serializable {

    private Place visitPlace;
    private String visitDate;

    //Constructor
    public VisitPlace(Place place, String date) {
        this.visitDate=date;
        this.visitPlace=place;
    }

    // get -------------------------
    public Place getVisitPlace(){
        return this.visitPlace;
    }
    public String getVisitDate(){
        return this.visitDate;
    }
    //--------------------------------

    // set----------------------------
    public void setVisitPlace(Place place){
        this.visitPlace=place;
    }
    public void setVisitDate(String date){
        this.visitDate =date;
    }
    //--------------------------------

    /*직선거리 구하기 위한 함수*/
    public double Distance(double lat1, double lon1, String unit) {

        double theta = lon1 - this.visitPlace.get_placeY();
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(this.visitPlace.get_placeX())) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(this.visitPlace.get_placeX())) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }
        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
