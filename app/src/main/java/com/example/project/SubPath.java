package com.example.project;

import java.lang.reflect.Array;
import java.util.ArrayList;


/*
 * 1-9-3 이동 교통 수단 정보 확장 노드 1..n
 */
public class SubPath {
    SubPath(){
        this.trafficType = 0;
        this.distance=0.0;
        this.sectionTime=0;
        this.stationCount=0;

        this.laneList =new ArrayList<Lane>();

        this.startStation = null;
        this.endStation = null;
        this.way = "";
        this.wayCode = 0;
        this.door = "";

        this.startExitNo = null;
        this.endExitNo = null;
    }

    private int trafficType; //이동 수단 종류 (도보, 버스, 지하철) 1-지하철, 2-버스, 3-도보
    private double distance; //이동 거리
    private int sectionTime; //이동 소요시간
    private ArrayList<Lane> laneList;

    private int stationCount; //이동 정거장 수(지하철, 버스 경우만 필수)
    private Place startStation; // 승차정류장 역명 , 역 x(경도)좌표, 역 Y(위도 좌표)
    private Place endStation; // 하차 정류장 역명 , 역 x(경도)좌표, 역 Y(위도 좌표)
    private String way; // 방면 정보
    private int wayCode; // 방면 정보 코드 1: 상행 2: 하행
    private String door; // 지하철 빠른 환승 위치

    private Place startExitNo;
    // startExitNo 지하철 들어가는 출구 번호 (지하철인 경우에만 사용되지만 해당 태그가 없을 수도 있다.)
    // startExitX 지하철 들어가는 출구 X좌표(지하철인 경우에 만 사용되지만 해당 태그가 없을 수도 있다.)
    // startExitY 지하철 들어가는 출구 Y좌표(지하철인 경우에 만 사용되지만 해당 태그가 없을 수도 있다.)
    private Place endExitNo;
    // endExitNo 지하철 들어가는 출구 번호 (지하철인 경우에만 사용되지만 해당 태그가 없을 수도 있다.)
    // endExitX 지하철 들어가는 출구 X좌표(지하철인 경우에 만 사용되지만 해당 태그가 없을 수도 있다.)
    // endExitY 지하철 들어가는 출구 Y좌표(지하철인 경우에 만 사용되지만 해당 태그가 없을 수도 있다.)

    public Place getEndStation() {
        return endStation;
    }
    public Place getStartStation() {
        return startStation;
    }
    public String getWay() {
        return way;
    }
    public String getDoor() {
        return door;
    }
    public Place getStartExitNo() {
        return startExitNo;
    }
    public Place getEndExitNo() {
        return endExitNo;
    }
    public int getWayCode() {
        return wayCode;
    }

    public ArrayList<Lane> getLaneList() {
        return laneList;
    }

    public double getDistance() {
        return distance;
    }
    public int getSectionTime() {
        return sectionTime;
    }
    public int getStationCount() {
        return stationCount;
    }
    public int getTrafficType() {
        return trafficType;
    }

    public void addList(Lane lane){
        laneList.add(lane);
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public void setLaneList(ArrayList<Lane> laneList) {
        this.laneList = laneList;
    }

    public void setEndExitNo(Place endExitNo) {
        this.endExitNo = endExitNo;
    }


    public void setEndStation(Place endStation) {
        this.endStation = endStation;
    }

    public void setSectionTime(int sectionTime) {
        this.sectionTime = sectionTime;
    }

    public void setStartExitNo(Place startExitNo) {
        this.startExitNo = startExitNo;
    }


    public void setStartStation(Place startStation) {
        this.startStation = startStation;
    }

    public void setStationCount(int stationCount) {
        this.stationCount = stationCount;
    }

    public void setTrafficType(int trafficType) {
        this.trafficType = trafficType;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public void setWayCode(int wayCode) {
        this.wayCode = wayCode;
    }
}


