package com.example.project;

import java.io.Serializable;
import java.util.Date;

public class RowOfPatient implements Serializable {


    public RowOfPatient(int patientNum, String confirmDate, int bigLocalNum, int smallLocalNum) {
        this.patientNum = patientNum;
        this.confirmDate = confirmDate;
        this.bigLocalNum = bigLocalNum;
        this.smallLocalNum = smallLocalNum;
    }

    public int getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(int patientNum) {
        this.patientNum = patientNum;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public int getBigLocalNum() {
        return bigLocalNum;
    }

    public void setBigLocalNum(int bigLocalNum) {
        this.bigLocalNum = bigLocalNum;
    }

    public int getSmallLocalNum() {
        return smallLocalNum;
    }

    public void setSmallLocalNum(int smallLocalNum) {
        this.smallLocalNum = smallLocalNum;
    }

    int patientNum;
    String confirmDate;
    int bigLocalNum;
    int smallLocalNum;
}
