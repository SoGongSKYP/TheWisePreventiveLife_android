package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 사용자 모드
 * PageOfMain(메인 페이지)
 * PageOfMyDanger(위험도 계산 페이지)
 * PageOfStatistics(통계 페이지)
 * PageOfSelfDiagnosis(자가 진단 페이지)
 * PageOfSelectedClinic(선별진료소 페이지)
 * ****************************************
 * PageOfToManager(관리자 모드 변경 페이지)
 */
public class UserPages<Private> extends AppCompatActivity {

    /*Bottom Navigation Bar 관련 컴포넌트*/
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private PageOfMain pageOfMain;
    private PageOfMyDanger pageOfMyDanger ;
    private PageOfStatistics pageOfStatistics;
    private PageOfSelfDiagnosis pageOfSelfDiagnosis ;
    private PageOfSelectedClinic pageOfSelectedClinic;

    /*Tool Bar 관련 컴포넌트*/
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView TitleTextView;
    private ImageButton InfoImageButton;

    /*search bar 관련 컴포넌트*/
    private ImageButton searchButton;
    DialogOfSearch dialog;

    private String nowPage="user_Home";

    public UserPages() throws ParserConfigurationException, SAXException, ParseException, IOException {
        pageOfMain = new PageOfMain();
        new Thread(){
            public void run() {
                try {
                    pageOfMyDanger = new PageOfMyDanger();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            public void run() {
                pageOfSelectedClinic = new PageOfSelectedClinic();
            }
        }.start();
        new Thread(){
            public void run() {
                try {
                    pageOfStatistics = new PageOfStatistics();
                } catch (ParserConfigurationException | SAXException | ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            public void run() {
                pageOfSelfDiagnosis = new PageOfSelfDiagnosis();
            }
        }.start();
        try{
            DBEntity.patient_info();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pages);
        /*Bottom Navigation 연결*/
        BottomNavigationView navigationView = findViewById(R.id.user_BottomNavigation);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.user_FrameLayout, pageOfMain).commit();

        /*Tool Bar 연결*/
        toolbar = findViewById(R.id.user_Toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TitleTextView = findViewById(R.id.user_Title_TextView);
        TitleTextView.setText("주변 확진자 현황");

        /*서치바 컴포넌트 연결*/
        searchButton = findViewById(R.id.user_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DialogOfSearch(UserPages.this);
                dialog.setSearchDialogListener(new DialogOfSearch.SearchDialogListener(){
                    @Override
                    public void onOKCliked(Place place) throws JSONException {
                        UserLoc.setUser_place(place);   // searchPlace가 검색된 장소, 이 장소 좌표로 이동
                        if(nowPage.equals("user_Home")){
                            pageOfMain.RefreshMarker();
                            pageOfMain.calNearPlace();
                            try {
                                pageOfMain.addNearPlaceMaker();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(nowPage.equals("user_Clinics")){
                            pageOfSelectedClinic.RefreshMarker();
                            pageOfSelectedClinic.addMarker();
                        }
                    }
                });

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = dialog.getWindow();
                window.setAttributes(lp);

            }
        });

        InfoImageButton = findViewById(R.id.user_info_ImageButton);
        InfoImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PageOfToManager.class);
                startActivity(intent);

            }
        });
    }

    /* 일반사용자 페이지 네비게이션 바 연결*/
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.user_Home:
                    nowPage = "user_Home";
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_FrameLayout, pageOfMain).commit();
                    TitleTextView.setText("주변 확진자 현황");
                    searchButton.setVisibility(View.VISIBLE);
                    return true;
                case R.id.user_Danger:
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_FrameLayout, pageOfMyDanger).commit();
                    TitleTextView.setText("나의 동선 위험도");
                    searchButton.setVisibility(View.GONE);
                    return true;
                case R.id.user_Statistics:
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_FrameLayout, pageOfStatistics).commit();
                    TitleTextView.setText("전국 통계");
                    searchButton.setVisibility(View.GONE);
                    return true;
                case R.id.user_SelfDiagnosis:
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_FrameLayout, pageOfSelfDiagnosis).commit();
                    TitleTextView.setText("자가 진단");
                    searchButton.setVisibility(View.GONE);
                    return true;
                case R.id.user_Clinics:
                    nowPage = "user_Clinics";
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_FrameLayout, pageOfSelectedClinic).commit();
                    TitleTextView.setText("주변 선별 진료소");
                    searchButton.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
