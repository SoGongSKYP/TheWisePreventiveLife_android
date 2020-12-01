package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.GoogleMap;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class AlramService extends Service {
    GPSListener gpsListener;
    public AlramService() {
        gpsListener = new GPSListener();
    }
    @Override
    public void onCreate() {// 서비스에서 가장 먼저 호출됨(최초에 한번만)
        System.out.println("백그라운드 실행 중입니다.");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {// 서비스가 호출될 때마다 실행
        ArrayList<Patient> patientsList = (ArrayList<Patient>) intent.getSerializableExtra("patientList");
        UserLoc.LocBy_gps(this,gpsListener);

        Intent mainIntent= new Intent(this, PageOfMain.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(
          this,1,mainIntent,PendingIntent.FLAG_UPDATE_CURRENT
        );
        String channelID ="알람";
        String channelName="알람 작동중 입니다";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationManager mNotifyMgr=
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    channelID,channelName,importance
            );
            mNotifyMgr.createNotificationChannel(mChannel);
            mBuilder=new NotificationCompat.Builder(this,channelID);
        }
        else{
            mBuilder= new NotificationCompat.Builder(this)
                    //.setSmallIcon(1)// 아이콘 넣어야함
                    .setContentTitle("")
                    .setContentIntent(mPendingIntent)
                    .setContentText("");
        }
        mNotifyMgr.notify(001,mBuilder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private class GPSListener implements LocationListener {

        public GPSListener(){
        }
        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
    }
}
