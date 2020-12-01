package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterOfDiagnosis extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private ArrayList<String> questionList;
    private ArrayList<Integer> yesList;
    private int count;

    public static class DiagnosisViewHolder extends RecyclerView.ViewHolder {
        public Button YesButton;
        public Button NoButton;
        public TextView QuestionTextView;
        public DiagnosisViewHolder(View v) {
            super(v);
            Log.d("ViewHolder 생성자", ": 1");
            YesButton = v.findViewById(R.id.selfdiagnosis_YES_Button);
            NoButton = v.findViewById(R.id.selfdiagnosis_NO_Button);
            QuestionTextView = v.findViewById(R.id.selfdiagnosis_question_TextView);
        }
    }

    public static class SubmitViewHolder extends RecyclerView.ViewHolder {
        public Button SubmitButton;
        public SubmitViewHolder(View v){
            super(v);
            SubmitButton = v.findViewById(R.id.selfdiagnosis_submit_Button);
        }
    }

    public AdapterOfDiagnosis(ArrayList<String> data){
        yesList = new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 2));
        count=0;
        this.questionList = data;
    }

    @Override
    public int getItemViewType(int position){
        if (position == questionList.size())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_FOOTER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selfdiagnosis_footer, parent, false);
            holder = new SubmitViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selfdiagnosis_question, parent, false);
            holder = new DiagnosisViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        /*question row*/
        if (holder instanceof DiagnosisViewHolder){
            final DiagnosisViewHolder Qholder = (DiagnosisViewHolder) holder;
            Qholder.QuestionTextView.setText(questionList.get(position));
            Qholder.YesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(yesList.get(position) == 2){
                        yesList.set(position, 1);
                        Qholder.YesButton.setBackgroundResource(R.drawable.button_full_grey);
                        Qholder.NoButton.setBackgroundResource(R.drawable.button_outline_grey);
                        count++;
                    } else{
                        yesList.set(position, 1);
                        Qholder.YesButton.setBackgroundResource(R.drawable.button_full_grey);
                        Qholder.NoButton.setBackgroundResource(R.drawable.button_outline_grey);
                    }
                    Log.d("현재 결과 배열 추가", yesList.toString());
                    Log.d("클릭 횟수", Integer.toString(count));
                }
            });
            Qholder.NoButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (yesList.get(position) == 2){
                        yesList.set(position, 0);
                        Qholder.YesButton.setBackgroundResource(R.drawable.button_outline_grey);
                        Qholder.NoButton.setBackgroundResource(R.drawable.button_full_grey);
                        count++;
                    }else{
                        yesList.set(position, 0);
                        Qholder.YesButton.setBackgroundResource(R.drawable.button_outline_grey);
                        Qholder.NoButton.setBackgroundResource(R.drawable.button_full_grey);
                    }
                    Log.d("현재 결과 배열 삭제", yesList.toString());
                    Log.d("클릭 횟수", Integer.toString(count));
                }
            });
        }
        /*footer row*/
        else if(holder instanceof SubmitViewHolder){
            SubmitViewHolder Bholder = (SubmitViewHolder) holder;
            Bholder.SubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count==7){
                        Intent intent = new Intent(view.getContext(), PageOfSelfDiagnosisResult.class);
                        intent.putExtra("result", yesList);
                        view.getContext().startActivity(intent);
                    }else{
                        Toast.makeText(view.getContext(), "설문에 모두 답변해주세요", Toast.LENGTH_SHORT).show();
                        Log.d("클릭 횟수", Integer.toString(count));
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (null != questionList ? questionList.size()+1 : 0);
    }
}
