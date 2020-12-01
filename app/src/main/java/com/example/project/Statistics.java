package com.example.project;
import java.util.*;

/**
 * 
 */
abstract class Statistics {

    /**
     * Default constructor
     */
    public Statistics() {
    }

    protected String staticsDate; /*기준 날짜*/
    protected int patientNum;/*확진자수*/
    protected int deadNum;/*사망자수*/
    protected int healerNum; /*완치자수==격리해제수*/

    public abstract void make_chart();

}
