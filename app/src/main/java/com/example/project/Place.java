package com.example.project;
import java.io.Serializable;

/**
 * 장소 관련 정보 저장 class
 * x가 경도 y가 위도
 */
public class Place implements Serializable {

    private String placeAddress; //장소에 대한 간단한 주소나 장소 이름
    private String placeDetailAddress; // 장소에 대한 디테일한 주소
    private double placeX;//경도
    private double placeY;//위도

    //Constructor
    public Place(String place_address, double placeX, double placeY ) {
        this.placeAddress=place_address;
        this.placeDetailAddress = "";
        this.placeX=placeX;
        this.placeY=placeY;
    }

    public Place(String place_address, String detail_address, double placeX, double placeY ) {
        this.placeAddress=place_address;
        this.placeDetailAddress = detail_address;
        this.placeX=placeX;
        this.placeY=placeY;
    }

    // get -------------------------
    public double get_placeX(){
        return this.placeX;
    }
    public double get_placeY(){
        return this.placeY;
    }
    public String get_placeAddress(){
        return this.placeAddress;
    }
    public String get_placeDetailAddress() {return this.placeDetailAddress;}
    //----------------------------

    // set -------------------------
    public void set_placeX(double placeX){
        this.placeX = placeX;
    }
    public void set_placeY(double placeY){
        this.placeY = placeY;
    }
    public void set_placeAddress(String placeAddress){
        this.placeAddress = placeAddress;
    }
    public void set_placeDetailAddress(String placeDetailAddress) {this.placeDetailAddress = placeDetailAddress;}
    //--------------------------------
}
