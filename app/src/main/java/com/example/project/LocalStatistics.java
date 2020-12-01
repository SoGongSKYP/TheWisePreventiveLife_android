package com.example.project;
import java.util.*;

/**
 * xml 예시
 * <item>
 * <createDt>2020-11-21 09:44:25.227</createDt> //등록일시분초
 * <deathCnt>0</deathCnt>//사망자수
 * <defCnt>457</defCnt> //누적 확진자 수
 * <gubun>경남</gubun> // 시도명 한글
 * <gubunCn>庆南</gubunCn>
 * <gubunEn>Gyeongsangnam-do</gubunEn>
 * <incDec>11</incDec>//전일대비 증감수
 * <isolClearCnt>342</isolClearCnt>//격리해제수
 * <isolIngCnt>115</isolIngCnt> // 격리중 환자수
 * <localOccCnt>11</localOccCnt>//지역 발생 수
 * <overFlowCnt>0</overFlowCnt>//해외유입수
 * <qurRate>13.60</qurRate> //10만명당 발생률
 * <seq>5800</seq>
 * <stdDay>2020년 11월 21일 00시</stdDay>//기준일시
 * <updateDt>null</updateDt>
 * </item>
 */

/* local 순서
1검역
2제주
3경남
4경북
5전남
6전북
7충남
8충북
9강원
10경기
11세종
12울산
13대전
14광주
15인천
16대구
17부산
18서울
19합계
 */

public class LocalStatistics extends Statistics {

    /**
     * Default constructor
     */
    public LocalStatistics(String staticsDate,int patientNum,int deadNum,int healerNum,
                           String localName, int increaseDecrease, int locConfirm
            ,int broadConfirm,double qurRate)
    {
        this.staticsDate=staticsDate; //stdDay 기준 일시
        this.patientNum=patientNum; //isolIngCnt 격리중인 환자
        this.deadNum =deadNum; // 사망자 deathCnt
        this.healerNum =healerNum; //isolClearCnt 격리해제
        this.localName =localName; // gubun
        this.increaseDecrease=increaseDecrease; //incDec

        this.locConfirm = locConfirm; //localOccCnt
        this.broadConfirm = broadConfirm; //overFlowCnt
        this.qurRate=qurRate; //
        this.todayConfirm=this.locConfirm+ this.broadConfirm;
        this.accumulatePatient = this.patientNum+this.healerNum;
    }
    private String localName; /*지역 이름 태그 명: gubun*/
    private int increaseDecrease; /*전일 대비 증감 수 태그 명: incDec*/
    private int locConfirm; //지역 확진 localOccCnt
    private int broadConfirm ; // 해외 확진 overFlowCnt
    private double qurRate; // 10만명당 확진률 qurRate

    private int todayConfirm; // 지역 + 해외 유입
    private int accumulatePatient; // 누적 확진자 수

    public double getQurRate() {
        return qurRate;
    }
    public int getBroadConfirm() {
        return broadConfirm;
    }
    public int getLocConfirm() {
        return locConfirm;
    }
    public int getAccumulatePatient() {
        return accumulatePatient;
    }
    public int getTodayConfirm() {
        return todayConfirm;
    }
    public String getLocalName() {
        return this.localName;
    }
    public String getStaticsDate(){
        return this.staticsDate;
    }
    public int getPatientNum() {
        return this.patientNum;
    }
    public int getDeadNum(){
        return this.deadNum;
    }
    public int getHealerNum() {
        return this.healerNum;
    }
    public int getIncreaseDecrease() {
        return increaseDecrease;
    }

    public void make_chart() {
        // TODO implement here
    }


}
