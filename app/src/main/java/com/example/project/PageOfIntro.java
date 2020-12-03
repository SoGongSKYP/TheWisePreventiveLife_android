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

public class PageOfIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final Lock lock = new ReentrantLock(); // lock instance

        Thread thread = new Thread(new APIEntity(lock));
        thread.start();
        Thread threadArray[] = new Thread[21];
        for(int i =0; i < 21;i++){
            threadArray[i]=new Thread(new clinicAPIEntity(i+1));
            threadArray[i].start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBEntity.connectAppDB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBEntity.connectMovingDB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


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
