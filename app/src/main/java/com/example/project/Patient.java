package com.example.project;
import java.io.Serializable;
import java.util.*;

/**
 * 전염병 감염 환자 사용자 정의형 class
 */
public class Patient implements Serializable {

    private int smallLocalNum; // 시군 단위 지역 번호
    private int bigLocalNum; // 광역 자치단체(도, 광역시,특별시)
    private String patientNum; // 환자 번호
    private String confirmDate; // 확진 날짜
    private ArrayList<VisitPlace> visitPlaceList;// 환자의 확진자 동선

    public Patient(int smallLocalNum, int bigLocalNum, String patientNum, String confirmDate, ArrayList<VisitPlace> visitPlaceList) {
        this.smallLocalNum = smallLocalNum;
        this.bigLocalNum = bigLocalNum;
        this.patientNum = patientNum;
        this.confirmDate = confirmDate;
        this.visitPlaceList = visitPlaceList;
    }

    public int getSmallLocalNum() {
        return smallLocalNum;
    }

    public void setSmallLocalNum(int smallLocalNum) {
        this.smallLocalNum = smallLocalNum;
    }

    public int getBigLocalNum() {
        return bigLocalNum;
    }

    public void setBigLocalNum(int bigLocalNum) {
        this.bigLocalNum = bigLocalNum;
    }

    public String getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(String patientNum) {
        this.patientNum = patientNum;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public ArrayList<VisitPlace> getVisitPlaceList() {
        return visitPlaceList;
    }

    public void setVisitPlaceList(ArrayList<VisitPlace> visitPlaceList) {
        this.visitPlaceList = visitPlaceList;
    }


}
