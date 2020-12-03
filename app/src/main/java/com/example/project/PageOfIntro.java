package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
/**
 * 어플이 시작될 때 인트로 page
 * 이때 DB와의 연결, api에서 값 받아오는 연산을 거칩니다.
 */
public class PageOfIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final Lock lock = new ReentrantLock(); // lock instance

        Thread thread = new Thread(new APIEntity(lock)); // 전국 지역 통계 API불러오는 쓰레드
        thread.start();

        Thread threadArray[] = new Thread[21];
        /**
         * 선별진료소가 api에 저장된 것만 총 1024개이므로 50개씩 나누어서 총 21개 쓰레드를 멀티 쓰레딩 방식으로
         * 구현하여 속도 개선 및 과부화 해결
         */
        for(int i =0; i < 21;i++){
            threadArray[i]=new Thread(new clinicAPIEntity(i+1));
            threadArray[i].start();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                DBEntity.connectAppDB();
            }
        }).start();// 환자 정보를 DB에서 받아올 DB 쓰레드
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBEntity.connectMovingDB();
            }
        }).start(); // 환자 동선 정보를 DB에서 받아올 쓰레드

        this.LocPermission(this,getApplicationContext());
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), UserPages.class);
                        startActivity(intent);
                    }
                }
                , 6000);
    } 
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 200;
    public void LocPermission(Activity activity , Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_DENIED ) {
            //Toast.makeText(context, "사용자 위치정보 동의 거부시 사용이 제한되는 부분이 있을수 있습니다.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } // 권한확인후 위치정보 제공 동의가 안 되어 있을때 위치 정보 제공 동의받기
    }
}
