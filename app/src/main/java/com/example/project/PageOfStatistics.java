package com.example.project;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 *
 */
public class PageOfStatistics extends Fragment {
    ImageButton btn_seoul, btn_busan, btn_daegu, btn_incheon, btn_gwangju, btn_daejeon, btn_ulsan, btn_sejong,
            btn_gyeonggido, btn_gangwondo, btn_chungbuk, btn_chungnam, btn_jeonbuk, btn_jeonnam, btn1_gyeongbuk,
            btn2_gyeongbuk, btn1_gyeongnam, btn2_gyeongnam, btn_jeju;
    int localNum = 17;
    String locName="서울";
    ArrayList<PieEntry> values = new ArrayList<PieEntry>();
    PieChart pieChart;
    TextView nation1TextView, nation2TextView, nation3TextView, nation4TextView,nation5TextView,
            local1TextView, local2TextView, local3TextView, local4TextView, localTitleTextView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        findLoc();
    }
    public void findLoc(){
        for(int i =0; i < nationStatistic.getLocalStatistics().size();i++){
            if(nationStatistic.getLocalStatistics().get(i).getLocalName().equals(locName)){
                localNum=i;
                break;
            }
        }
    }
    public void dataView(PieChart pieC, ArrayList<PieEntry> value,int localNum){
        value.removeAll(value);
        pieC.setUsePercentValues(true);
        pieC.getDescription().setEnabled(false);
        pieC.setExtraOffsets(0, 1, 0, 0);

        pieC.setDragDecelerationFrictionCoef(0.95f);

        pieC.setDrawHoleEnabled(false);
        pieC.setHoleColor(Color.BLACK);
        pieC.setTransparentCircleRadius(65f);
        //누적확진비율
        value.add(new PieEntry(nationStatistic.getLocalStatistics().get(localNum).getAccumulatePatient(), nationStatistic.getLocalStatistics().get(localNum).getLocalName()));
        value.add(new PieEntry(nationStatistic.getPatientNum()-nationStatistic.getLocalStatistics().get(localNum).getAccumulatePatient(),"그 외 지역"));

        Description description = new Description();
        description.setText("");
        description.setTextSize(13);
        pieC.setDescription(description);

        pieC.setEntryLabelColor(Color.BLACK);
        pieC.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(value,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(4f);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_user_statistics, container, false);
        pieChart = (PieChart) v.findViewById(R.id.piechart);
        dataView(pieChart,values,localNum);

        btn_gyeonggido = v.findViewById(R.id.gyeonggi_Button);
        btn_gangwondo = v.findViewById(R.id.gangwondo_Button);
        btn_chungbuk = v.findViewById(R.id.chungBuk_Button);
        btn_chungnam = v.findViewById(R.id.chungNam_Button);
        btn_jeonbuk = v.findViewById(R.id.jeonBuk_Button);
        btn_jeonnam = v.findViewById(R.id.jeonNam_Button);
        btn1_gyeongbuk = v.findViewById(R.id.gyeongBuk1_Button);
        btn2_gyeongbuk = v.findViewById(R.id.gyeongBuk2_Button);
        btn1_gyeongnam = v.findViewById(R.id.gyeongNam1_Button);
        btn2_gyeongnam = v.findViewById(R.id.gyeongNam2_Button);
        btn_jeju = v.findViewById(R.id.jeju_Button);

        btn_seoul = v.findViewById(R.id.seoul_Button);
        btn_busan = v.findViewById(R.id.busan_Button);
        btn_daegu = v.findViewById(R.id.daegu_Button);
        btn_incheon = v.findViewById(R.id.incheon_Button);
        btn_gwangju = v.findViewById(R.id.gwangju_Button);
        btn_daejeon = v.findViewById(R.id.daejeon_Button);
        btn_ulsan = v.findViewById(R.id.ulsan_Button);
        btn_sejong = v.findViewById(R.id.sejong_Button);

        nation1TextView = v.findViewById(R.id.nation_1_TextView);
        nation2TextView = v.findViewById(R.id.nation_2_TextView);
        nation3TextView = v.findViewById(R.id.nation_3_TextView);
        nation4TextView = v.findViewById(R.id.nation_4_TextView);
        nation5TextView=v.findViewById(R.id.nation_5_TextView);
        local1TextView = v.findViewById(R.id.user_statistics_1_TextView);
        local2TextView = v.findViewById(R.id.user_statistics_2_TextView);
        local3TextView = v.findViewById(R.id.user_statistics_3_TextView);
        local4TextView = v.findViewById(R.id.user_statistics_4_TextView);
        localTitleTextView = v.findViewById(R.id.local_title_TextView);

        nation1TextView.setText(this.nationStatistic.getPatientNum()+"명");
        nation2TextView.setText(this.nationStatistic.getCareNum()+"명");
        nation3TextView.setText(this.nationStatistic.getHealerNum()+"명");
        nation4TextView.setText(this.nationStatistic.getDeadNum()+"명");
        nation5TextView.setText(this.nationStatistic.getLocalStatistics().get(this.nationStatistic.getLocalStatistics().size()-1).getIncreaseDecrease()+"명");

        setnum(localNum);
        LocalButtonAction();
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public PageOfStatistics() throws ParserConfigurationException, SAXException, ParseException, IOException {
        this.nationStatistic = APIEntity.getNation();
    }

    private NationStatistics nationStatistic;

    public void nationStatisticsPrint() {
        System.out.println("전국");
        System.out.println("기준 일시: "+this.nationStatistic.getStaticsDate());

        System.out.println("누적 검사 횟수 : "+this.nationStatistic.getTestCnt());
        System.out.println("누적 검사 완료수: "+this.nationStatistic.getTestCntComplete());
        System.out.println("결과 음성: "+this.nationStatistic.getTestNeg());
        System.out.println("검사 중: "+this.nationStatistic.getTestNum());
        Double confirmRate = ((double) this.nationStatistic.getPatientNum()/(double)this.nationStatistic.getTestCntComplete())*100;
        System.out.println("확진률: "+confirmRate+"%");

        System.out.println("누적 확진자: "+this.nationStatistic.getPatientNum());
        System.out.println("오늘 확진자: "+this.nationStatistic.getLocalStatistics().get(this.nationStatistic.getLocalStatistics().size()-1).getIncreaseDecrease());
        System.out.println("지역 유입: "+this.nationStatistic.getLocalStatistics().get(this.nationStatistic.getLocalStatistics().size()-1).getLocConfirm());
        System.out.println("해외 유입: "+this.nationStatistic.getLocalStatistics().get(this.nationStatistic.getLocalStatistics().size()-1).getBroadConfirm());

        System.out.println("격리 중 : "+this.nationStatistic.getCareNum());
        System.out.println("누적 격리 해제: "+this.nationStatistic.getHealerNum());
        System.out.println("사망자: "+this.nationStatistic.getDeadNum());
        System.out.println("10만명당 확진률: "+this.nationStatistic.getLocalStatistics().get(this.nationStatistic.getLocalStatistics().size()-1).getQurRate()+"%");
    }

    public void localStatisticsPrint() {
        System.out.println("지역: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getLocalName());
        System.out.println("기준 일시: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getStaticsDate());

        System.out.println("누적 확진자: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getAccumulatePatient());
        System.out.println("오늘 확진자: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getTodayConfirm());
        System.out.println("지역 유입: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getLocConfirm());
        System.out.println("해외 유입: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getBroadConfirm());

        System.out.println("격리 중 : "+this.nationStatistic.getLocalStatistics().get(this.localNum).getPatientNum());
        System.out.println("누적 격리 해제: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getHealerNum());
        System.out.println("사망자: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getDeadNum());
        System.out.println("10만명당 확진률: "+this.nationStatistic.getLocalStatistics().get(this.localNum).getAccumulatePatient()+"%");
    }
    public void setnum(int localNum){
        local1TextView.setText(this.nationStatistic.getLocalStatistics().get(localNum).getAccumulatePatient()+"명");
        local2TextView.setText(this.nationStatistic.getLocalStatistics().get(localNum).getHealerNum()+"명");
        local3TextView.setText(this.nationStatistic.getLocalStatistics().get(localNum).getPatientNum()+"명");
        local4TextView.setText(this.nationStatistic.getLocalStatistics().get(localNum).getTodayConfirm()+"명");
    }
    Button.OnClickListener localClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.seoul_Button:
                    Toast.makeText(getContext(), "서울 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("서울");
                    locName="서울";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.busan_Button:
                    Toast.makeText(getContext(), "부산 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("부산");
                    locName="부산";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.daegu_Button:
                    Toast.makeText(getContext(), "대구 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("대구");
                    locName="대구";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.incheon_Button:
                    Toast.makeText(getContext(), "인천 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("인천");
                    locName="인천";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.gwangju_Button:
                    Toast.makeText(getContext(), "광주 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("광주");
                    locName="광주";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.daejeon_Button:
                    Toast.makeText(getContext(), "대전 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("대전");
                    locName="대전";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.ulsan_Button:
                    Toast.makeText(getContext(), "울산 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("울산");
                    locName="울산";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.sejong_Button:
                    Toast.makeText(getContext(), "세종 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("세종");
                    locName="세종";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.gyeonggi_Button:
                    Toast.makeText(getContext(), "경기 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("경기");
                    locName="경기";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.gangwondo_Button:
                    Toast.makeText(getContext(), "강원 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("강원");
                    locName="강원";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.chungBuk_Button:
                    Toast.makeText(getContext(), "충북 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("충북");
                    locName="충북";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.chungNam_Button:
                    Toast.makeText(getContext(), "충남 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("충남");
                    locName="충남";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.jeonBuk_Button:
                    Toast.makeText(getContext(), "전북 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("전북");
                    locName="전북";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.jeonNam_Button:
                    Toast.makeText(getContext(), "전남 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("전남");
                    locName="전남";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.gyeongBuk1_Button:
                case R.id.gyeongBuk2_Button:
                    Toast.makeText(getContext(), "경북 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("경북");
                    locName="경북";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.gyeongNam1_Button:
                case R.id.gyeongNam2_Button:
                    Toast.makeText(getContext(), "경남 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("경남");
                    locName="경남";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
                case R.id.jeju_Button:
                    Toast.makeText(getContext(), "제주 클릭", Toast.LENGTH_SHORT).show();
                    localTitleTextView.setText("제주");
                    locName="제주";
                    findLoc();
                    dataView(pieChart,values,localNum);
                    setnum(localNum);
                    break;
            }
        }
    };

    private void LocalButtonAction(){
        btn_gyeonggido.setOnClickListener(localClickListener);
        btn_gangwondo.setOnClickListener(localClickListener);
        btn_chungbuk.setOnClickListener(localClickListener);
        btn_chungnam.setOnClickListener(localClickListener);
        btn_jeonbuk.setOnClickListener(localClickListener);
        btn_jeonnam.setOnClickListener(localClickListener);
        btn1_gyeongbuk.setOnClickListener(localClickListener);
        btn2_gyeongbuk.setOnClickListener(localClickListener);
        btn1_gyeongnam.setOnClickListener(localClickListener);
        btn2_gyeongnam.setOnClickListener(localClickListener);
        btn_jeju.setOnClickListener(localClickListener);

        btn_seoul.setOnClickListener(localClickListener);
        btn_busan.setOnClickListener(localClickListener);
        btn_daegu.setOnClickListener(localClickListener);
        btn_incheon.setOnClickListener(localClickListener);
        btn_gwangju.setOnClickListener(localClickListener);
        btn_daejeon.setOnClickListener(localClickListener);
        btn_ulsan.setOnClickListener(localClickListener);
        btn_sejong.setOnClickListener(localClickListener);
    }

}
