package com.example.project;
import java.util.*;

/**
 * 전국 통계를 담을 사용자 정의형 class
 */
public class NationStatistics extends Statistics {
    
    public NationStatistics(String staticsDate,int patientNum, int deadNum,int healerNum,
                            int testNum, ArrayList<LocalStatistics> localStatistics,int careNum,
                            int testNeg, int testCnt, int testCntComplete, double accDefRate) {
        this.staticsDate=staticsDate; //stateDt+stateTime
        this.patientNum=patientNum; // decideCnt 누적 확진자수
        this.deadNum=deadNum; // deathCnt 사망자수
        this.healerNum=healerNum; // clearCnt 격리 해제
        this.testNum=testNum; //examCnt 검사 진행 수(오늘)
        this.localStatistics=localStatistics;
        this.careNum=careNum; //careCnt 치료중 환자 수
        this.testNeg=testNeg; //resutlNegCnt 결과 음성
        this.testCnt=testCnt; //누적 검사 수 accExamCnt
        this.testCntComplete=testCntComplete; // accExamCompCnt 누적 검사 완료수
        this.accDefRate=accDefRate; // 누적 확진률 accDefRate
    }
    private int testNum; /*검사진행 수*/
    private ArrayList<LocalStatistics> localStatistics;/*지역별 통계*/
    private int careNum; /*치료중 환자수*/
    private int testNeg;/*결과 음성 수*/
    private int testCnt; /*누적 검사 수*/
    private int testCntComplete; /*누적 검사 완료수*/
    private double accDefRate; /*누적 확진률*/

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

    public ArrayList<LocalStatistics> getLocalStatistics() {
        return localStatistics;
    }
    public double getAccDefRate() {
        return accDefRate;
    }
    public int getCareNum() {
        return careNum;
    }
    public int getTestCnt() {
        return testCnt;
    }
    public int getTestCntComplete() {
        return testCntComplete;
    }
    public int getTestNeg() {
        return testNeg;
    }
    public int getTestNum() {
        return testNum;
    }

}
