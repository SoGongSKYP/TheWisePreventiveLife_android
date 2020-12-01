package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class PageOfSelfDiagnosisResult extends AppCompatActivity {

    /*Tool Bar 관련 컴포넌트*/
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView resultMainTextView, resultContentTextView;
    private LinearLayout resultLinearLayout;
    private ArrayList<Integer> result = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diagnosis_result);

        /*Tool Bar 연결*/
        toolbar = findViewById(R.id.diagnosis_result_Toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        result = (ArrayList<Integer>) intent.getSerializableExtra("result");
        Log.d("결과 스트링", result.toString());

        resultMainTextView = findViewById(R.id.result_main_TextView);
        resultContentTextView = findViewById(R.id.result_content_TextView);
        resultLinearLayout = findViewById(R.id.result_LinearLayout);

        printResult();
    }

    public void printResult(){
        if((result.get(0) == 1||result.get(1) == 1||result.get(2) == 1)
            &&(result.get(3) == 1||result.get(4) == 1)){
            if(result.get(5) == 1||result.get(6) == 1){
                resultMainTextView.setText("감염 고위험군");
                resultContentTextView.setText("아래의 코로나19 증상을 확인하고, 심각한 증상이 있다면 즉시 의료진의 도움을 받아야 합니다. " +
                        "의료 기관을 방문하기 전에는 항상 미리 연락을 취해야 하며, 외출시 마스크 착용은 필수 입니다. " +
                        "가급적 실내에서도 마스크를 착용해주시기 바랍니다.\n" +
                        "'선별진료소' 탭을 선택하여 가까운 선별진료소를 확인하세요.");
                resultLinearLayout.setBackgroundResource(R.drawable.result_box_red);
                resultMainTextView.setTextColor(Color.parseColor("#EE6161"));
            } else{
                resultMainTextView.setText("감염 위험군");
                resultContentTextView.setText("평소에 건강이 좋지 않은 경우라면 감염에 각별히 주의하셔야 합니다. " +
                        "감염자와의 접촉은 없었지만 증상이 계속된다면 가까운 선별진료소에 가셔서 검사를 받으세요. " +
                        "의료 기관 방문 전에는 항상 미리 연락을 취해야 합니다. 외출시 마스크 착용은 필수 입니다. \n" +
                        "'선별진료소' 탭을 선택하여 가까운 선별진료소를 확인하세요.");
                resultLinearLayout.setBackgroundResource(R.drawable.result_box_yellow);
                resultMainTextView.setTextColor(Color.parseColor("#FFA000"));
            }
        }else if((result.get(0) == 1||result.get(1) == 1||result.get(2) == 1)
            &&(result.get(5) == 1||result.get(6) == 1)){
            resultMainTextView.setText("감염 위험군");
            resultContentTextView.setText("건강상 불편이 없는 경증 환자는 자택에서 증상을 관리해야 합니다. " +
                    "점점 증상이 심해진다면 꼭 가까운 선별진료소에 가셔서 검사를 받으세요. " +
                    "의료 기관 방문 전에는 항상 미리 연락을 취해야 합니다. 외출시 마스크 착용은 필수 입니다. \n" +
                    "'선별진료소' 탭을 선택하여 가까운 선별진료소를 확인하세요.");
            resultLinearLayout.setBackgroundResource(R.drawable.result_box_yellow);
            resultMainTextView.setTextColor(Color.parseColor("#FFA000"));
        }else{
            resultMainTextView.setText("감염 저위험군");
            resultContentTextView.setText("외출 시 마스크 착용은 당연시 해야하고, 실내에서의 착용은 더욱 중요합니다." +
                    "인구가 밀집한 지역은 피하시는 것이 바람직합니다. " +
                    "또한 다른 사람과의 거리는 2m(최소 1m) 이상 거리 두기 어려운 곳은 피하며, " +
                    "지하 등 환기가 어려운 밀폐된 공간에 가는 것은 자제해야 합니다.");
            resultLinearLayout.setBackgroundResource(R.drawable.result_box_green);
            resultMainTextView.setTextColor(Color.parseColor("#43A047"));
        }
    }
}