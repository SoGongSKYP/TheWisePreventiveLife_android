package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.ArrayList;

public class ManagerPages extends AppCompatActivity {

    interface DeleteButtonClickListener{
        void onDeleteClick();
    }
    private DeleteButtonClickListener deleteButtonClickListener = null;
    public void setDeleteButtonClickListener(DeleteButtonClickListener listener){
        this.deleteButtonClickListener = listener;
    }
    /*
    public ManagerPages(){
        try {
            DBEntity.patient_info();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    */
    /*Bottom Navigation Bar 관련 컴포넌트*/
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private PageOfList pageOfList = new PageOfList();
    private PageOfAdd pageOfAdd = new PageOfAdd();
    private PageOfManager pageOfManager = new PageOfManager();

    /*Tool Bar 관련 컴포넌트*/
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView TitleTextView;
    private ImageButton deleteButton;

    String ID, PW;
    ArrayList<Patient> data;
    Patient deletePatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_pages);

        /*Bottom Navigation 연결*/
        BottomNavigationView navigationView = findViewById(R.id.manager_BottomNavigation);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.manager_FrameLayout, pageOfList).commit();

        /*Tool Bar 연결*/
        toolbar = findViewById(R.id.manager_Toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        TitleTextView = findViewById(R.id.manager_Title_TextView);
        TitleTextView.setText("확진자 정보 추가");

        Intent intent = getIntent();
        if(ID == null && PW == null){
            ID = intent.getExtras().getString("managerID");
            PW = intent.getExtras().getString("managerPW");
        }



    }

    /* 일반사용자 페이지 네비게이션 바 연결*/
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.manager_Add:
                    getSupportFragmentManager().beginTransaction().replace(R.id.manager_FrameLayout, pageOfList).commit();
                    TitleTextView.setText("확진자 리스트");
                    return true;
                case R.id.manager_Modify:
                    getSupportFragmentManager().beginTransaction().replace(R.id.manager_FrameLayout, pageOfAdd).commit();
                    TitleTextView.setText("확진자 정보 추가");
                    return true;
                case R.id.manager_Info:
                    getSupportFragmentManager().beginTransaction().replace(R.id.manager_FrameLayout, pageOfManager).commit();
                    TitleTextView.setText("관리자 정보");
                    return true;
            }
            return false;
        }
    };
}