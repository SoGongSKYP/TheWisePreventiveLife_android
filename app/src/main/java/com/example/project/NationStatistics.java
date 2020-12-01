package com.example.project;
import java.util.*;

/**
 * 양식
 * <item>
 * <accDefRate>1.0684961451</accDefRate> //누적 확진률
 * <accExamCnt>2896746</accExamCnt> //누적 검사수
 * <accExamCompCnt>2845401</accExamCompCnt> // 누적 검사 완료
 * <careCnt>3535</careCnt> //치료중 환자수
 * <clearCnt>26365</clearCnt>//격리해제 수
 * <createDt>2020-11-21 09:35:35.158</createDt> //등록 일시분초
 * <deathCnt>503</deathCnt>//사망자수
 * <decideCnt>30403</decideCnt>//확진자수
 * <examCnt>51345</examCnt>//검사 진행수
 * <resutlNegCnt>2814998</resutlNegCnt>//결과 음성수
 * <seq>329</seq>
 * <stateDt>20201121</stateDt> // 기준 일
 * <stateTime>00:00</stateTime>//기준 시간
 * <updateDt>null</updateDt>
 * </item>
 */
public class NationStatistics extends Statistics {

    /**
     * Default constructor
     */
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

    public void make_chart() {
        // TODO implement here
    }

    /**
     * 
     */
}
