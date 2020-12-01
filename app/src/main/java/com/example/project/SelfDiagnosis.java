package com.example.project;
import java.util.*;

/**
 * 
 */
public class SelfDiagnosis {

    /**
     * Default constructor
     */
    public SelfDiagnosis(String questionSentence, boolean answer) {
        this.questionSentence =questionSentence;
        this.answer = answer;
    }
    private String questionSentence;
    private boolean answer;

    public boolean isAnswer() {
        return answer;
    }
    public String getQuestionSentence() {
        return questionSentence;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
