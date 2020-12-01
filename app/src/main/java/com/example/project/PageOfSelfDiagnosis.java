package com.example.project;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

/**
 * 
 */
public class PageOfSelfDiagnosis extends Fragment {

    private ArrayList<String> QuestionSentencesArray;
    private Integer diagnosis_result;

    /* RecyclerView와 관련된 컴포넌트 */
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AdapterOfDiagnosis adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_user_selfdiagnosis, container, false);
        QuestionSentencesArray = new ArrayList<>();
        print_UI();

        recyclerView = v.findViewById(R.id.diagnosis_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterOfDiagnosis(QuestionSentencesArray);
        recyclerView.setAdapter(adapter);
        return v;
    }

    public void print_UI() {
        QuestionSentencesArray.add("37.5도 이상의 발열이 있었나요?");
        QuestionSentencesArray.add("마른 기침이나 인후통, 일반적인 감기 증상 또는 몸살기운이 있나요?");
        QuestionSentencesArray.add("후각과 미각 이상 증상이 나타났나요?");
        QuestionSentencesArray.add("호흡곤란 또는 숨 가쁨 증상이 있거나, 가슴 통증 또는 압박감의 증상이 나타났나요?");
        QuestionSentencesArray.add("고령자, 고혈압, 심장 및 폐 문제, 당뇨병 또는 암과 같은 기저질환이 있나요?");
        QuestionSentencesArray.add("최근 14일 이내에 확진자, 접촉자와 만난 적이 있었나요?");
        QuestionSentencesArray.add("최근 14일 이내에 해외 여행을 다녀오셨거나, 다녀온 주변인을 만난 적이 있었나요?");
    }

    public void self_diagnose() {
        // TODO implement here
    }

    public void calculate_result() {
        // TODO implement here
    }



}