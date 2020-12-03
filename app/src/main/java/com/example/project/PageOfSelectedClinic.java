package com.example.project;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.*;

/**
 * 선별 진료소 안내 페이지 class
 * 현재 내 위치 기반 직선거리로 가장 가까운 5개의 선별진료소를 소개해주는 페이지
 */

public class PageOfSelectedClinic extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap; //구글 맵 변수
    private MapView googleMap;

    private LatLng myLatLng;// 내 위치 경도 위도 저장할 변수
    private Marker userPoint; // 내 위치 마커 저장할 변수

    private ArrayList<SelectedClinic> clinics; // api에서 받아온 모든 선별진료소가 저장된 변수
    private ArrayList<Marker> clinicsMarker; // 지도에 찍히는 마커를 저장할 변수
    private ArrayList<SelectedClinic> nearClinics; // 모든 선별진료소에서 가까운 5개의 선별진료소를 저장할 변수

    public PageOfSelectedClinic() {
        this.clinicsMarker=new ArrayList<Marker>();
        this.clinics = new ArrayList<SelectedClinic>();
        this.clinics = clinicAPIEntity.getClinicsList();
        this.nearClinics =new ArrayList<SelectedClinic>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_clinics, container, false);
        googleMap = v.findViewById(R.id.user_clinic_Map);
        googleMap.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        GPSListener gpsListener =new GPSListener(this,mMap);
        UserLoc.LocBy_gps(getContext(),gpsListener);
        this.addMarker();
        this.myLatLng = new LatLng(UserLoc.getUserPlace().get_placeX(), UserLoc.getUserPlace().get_placeY());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(this.myLatLng);
        markerOptions.title("사용자");
        markerOptions.snippet("현재 위치 GPS");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myicon1));
        this.userPoint = this.mMap.addMarker(markerOptions);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.myLatLng, 15));
    } // 유저 현위치에 마커 추가

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (googleMap != null) {
            googleMap.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleMap.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleMap.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        googleMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        googleMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        googleMap.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        googleMap.onLowMemory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        googleMap.onLowMemory();
    }

    public void RefreshMarker() {
        this.userPoint.remove();
        this.myLatLng = new LatLng(UserLoc.getUserPlace().get_placeX(), UserLoc.getUserPlace().get_placeY());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(this.myLatLng);
        markerOptions.title("사용자");
        markerOptions.snippet("현재 위치 GPS");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myicon1));
        this.userPoint = this.mMap.addMarker(markerOptions);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.myLatLng, 15));
    } // 내 위치가 변경되었을때 refresh 해주는 함수

    public void addMarker()  {
        nearClinics = findClinic();
        for(int i =0; i <clinicsMarker.size();i++ ){
            this.clinicsMarker.get(i).remove();
        }
        for (int i = 0; i < nearClinics.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(nearClinics.get(i).getPlace().get_placeX(),
                    nearClinics.get(i).getPlace().get_placeY()));
            markerOptions.title(nearClinics.get(i).getName());
            markerOptions.snippet("주소: " + nearClinics.get(i).getPlace().get_placeAddress() +
                    "전화번호: " + nearClinics.get(i).getPhoneNum());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital1));
            clinicsMarker.add(mMap.addMarker(markerOptions));
        }
    } // 선별진료소에 마커를 찍어줄 함수

    public ArrayList<SelectedClinic> findClinic(){
        java.util.Map<Double, SelectedClinic> nearClinics = new LinkedHashMap<>();
        ArrayList<SelectedClinic> fiveNearClinics = new ArrayList<SelectedClinic>();
        for (int i = 0; i < this.clinics.size(); i++) {
            double dis = this.clinics.get(i).Distance(UserLoc.getUserPlace().get_placeX(),
                    UserLoc.getUserPlace().get_placeY()); // 현재 위치와 병원과의 직선 거리
            nearClinics.put(dis,clinics.get(i));
        } // 거리대로 정렬
        Object[] mapKey = nearClinics.keySet().toArray();
        Arrays.sort(mapKey);
        for (Object nKey : mapKey)
        {
            fiveNearClinics.add(nearClinics.get(nKey));
            if(fiveNearClinics.size() >= 5){
                return fiveNearClinics;
            }
        } // 5개 고름
        return fiveNearClinics;
    } // 현재 내 위치에서 가까운 5개의 선별진료소를 탐색하는 함수
    // map 자료구조를 사용해서 거리를 정렬하여 구했습니다.

    private class GPSListener implements LocationListener {

        PageOfSelectedClinic page;
        GoogleMap mMap;

        public GPSListener(PageOfSelectedClinic page,GoogleMap mMap){
            this.page=page;
            this.mMap=mMap;
        }

        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            if(getActivity()!=null){
                getActivity().runOnUiThread((new Runnable() {
                    @Override
                    public void run() {
                        page.RefreshMarker();
                    }
                }));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        page.addMarker();
                    }
                });
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }//GPS가 업데이트 되면서 장소가 변경되었을때 실행되는 리스너 함수

}
