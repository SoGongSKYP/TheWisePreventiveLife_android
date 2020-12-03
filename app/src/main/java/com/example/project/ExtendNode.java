package com.example.project;

/**
 * 경로 검색시 사용하는 사용자 정의형 class
 * 1-9-2요약 정보 확장 노드
 * 참고: https://lab.odsay.com/guide/sustainReference#searchPubTransPath
*/
public class ExtendNode {
    private double trafficDistance; //도보를 제외한 총 이동 거리
    private int totalWalk; //총 도보 이동 거리
    private int totalTime; //총 소요시간
    private int payment; // 총 요금
    private int busTransitCount; //버스 환승 카운트
    private int subwayTransitCount; //지하철 환승 카운트
    private String mapObj; //보간점 API를 호출하기 위한 파라미터 값
    private String firstStartStation; //최초 출발역/정류장
    private String lastEndStation; //최종 도착역/정류장
    private int totalStationCount; //총 정류장 합
    private double totalDistance; //총 거리

    ExtendNode(double trafficDistance,int totalWalk,int totalTime,int payment,int busTransitCount
    ,int subwayTransitCount,String mapObj,String firstStartStation,String lastEndStation,int totalStationCount
    ,double totalDistance){
        this.trafficDistance=trafficDistance;
        this.totalWalk =totalWalk;
        this.totalTime =totalTime;
        this.payment=payment;
        this.busTransitCount=busTransitCount;
        this.subwayTransitCount=subwayTransitCount;
        this.mapObj=mapObj;
        this.firstStartStation=firstStartStation;
        this.lastEndStation=lastEndStation;
        this.totalStationCount=totalStationCount;
        this.totalDistance=totalDistance;
    }


    public double getTotalDistance() {
        return totalDistance;
    }
    public double getTrafficDistance() {
        return trafficDistance;
    }
    public int getBusTransitCount() {
        return busTransitCount;
    }
    public int getPayment() {
        return payment;
    }
    public int getSubwayTransitCount() {
        return subwayTransitCount;
    }
    public int getTotalStationCount() {
        return totalStationCount;
    }
    public int getTotalTime() {
        return totalTime;
    }
    public int getTotalWalk() {
        return totalWalk;
    }
    public String getFirstStartStation() {
        return firstStartStation;
    }
    public String getMapObj() {
        return mapObj;
    }
    public String getLastEndStation() {
        return lastEndStation;
    }
    public void setBusTransitCount(int busTransitCount) {
        this.busTransitCount = busTransitCount;
    }
    public void setFirstStartStation(String firstStartStation) {
        this.firstStartStation = firstStartStation;
    }
    public void setLastEndStation(String lastEndStation) {
        this.lastEndStation = lastEndStation;
    }
    public void setPayment(int payment) {
        this.payment = payment;
    }
    public void setMapObj(String mapObj) {
        this.mapObj = mapObj;
    }
    public void setSubwayTransitCount(int subwayTransitCount) {
        this.subwayTransitCount = subwayTransitCount;
    }
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
    public void setTotalStationCount(int totalStationCount) {
        this.totalStationCount = totalStationCount;
    }
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
    public void setTotalWalk(int totalWalk) {
        this.totalWalk = totalWalk;
    }
    public void setTrafficDistance(double trafficDistance) {
        this.trafficDistance = trafficDistance;
    }
}
