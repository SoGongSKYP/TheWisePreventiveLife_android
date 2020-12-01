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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 */
public class PageOfMain extends Fragment implements OnMapReadyCallback {

    public GoogleMap mMap;

    private Marker userPoint;
    private ArrayList<Marker> nearMaker;

    private LatLng myLatLng;
    private MapView mapView;

    private ArrayList<Patient> patient;
    private ArrayList<VisitPlace> nearPlaces;

    private DBEntity dbEntity;

    public PageOfMain() {
        this.nearPlaces = new ArrayList<VisitPlace>();
        this.patient = new ArrayList<Patient>();
        this.nearMaker = new ArrayList<Marker>();
        this.dbEntity =new DBEntity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_home, container, false);
        /*맵 컴포넌트 연결*/
        mapView = v.findViewById(R.id.user_main_Map);
        mapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.myLatLng = new LatLng(UserLoc.getUserPlace().get_placeX(), UserLoc.getUserPlace().get_placeY());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(this.myLatLng);
        markerOptions.title("사용자");
        markerOptions.snippet("현재 위치 GPS");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myicon1));
        this.userPoint = this.mMap.addMarker(markerOptions);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.myLatLng, 15));
        GPSListener gpsListener = new GPSListener();
        UserLoc.LocBy_gps(getContext(),gpsListener);
    } // 유저 현위치에 마커 추가 // 유저 현위치에 마커 추가
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    calNearPlace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    addNearPlaceMaker();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.myLatLng, 15));
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RefreshMarker();
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            calNearPlace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            addNearPlaceMaker();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
    }

    public void calNearPlace() throws JSONException {
        this.nearPlaces.clear();
        this.patient=DBEntity.getPatientList();
        for (int a = 0; a < this.patient.size(); a++) {
            for (int b = 0; b < this.patient.get(a).getVisitPlaceList().size(); b++) {
                if (this.patient.get(a).getVisitPlaceList().get(b).
                        Distance(UserLoc.getUserPlace().get_placeX(),
                                UserLoc.getUserPlace().get_placeY(), "kilometer") <= 1) {
                    this.nearPlaces.add(this.patient.get(a).getVisitPlaceList().get(b));
                }
            }
        }
    }//반경 1km이내 확진자 동선

    public void addNearPlaceMaker() throws ParseException {
        Date time = new Date();
        for (int a = 0; a < this.nearMaker.size(); a++) {
            this.nearMaker.get(a).remove();
        }
        if (this.nearMaker.size() != 0) {
            this.nearMaker.clear();
        }
        for (int a = 0; a < this.nearPlaces.size(); a++)
        {
            String from = nearPlaces.get(a).getVisitDate();
            SimpleDateFormat transDate= new SimpleDateFormat("yyyy-MM-dd");
            Date to = transDate.parse(from);
            long dateGap = (time.getTime()- to.getTime())/(1000*60*60*24);
            LatLng nearLatlng = new LatLng(this.nearPlaces.get(a).getVisitPlace().get_placeX(), this.nearPlaces.get(a).getVisitPlace().get_placeY());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(nearLatlng);
            markerOptions.title("확진자");
            SimpleDateFormat transFormat = new SimpleDateFormat("MM월dd일");
            from = transFormat.format(to);
            markerOptions.snippet(from + this.nearPlaces.get(a).getVisitPlace().get_placeAddress());

            if(dateGap < 2){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.virus1));
            }// 현재로부터 2일 이내에 다녀간 장소
            else if(dateGap >= 2&&dateGap < 7){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.virus2));
            }// 현재로부터 2~7일 사이에 다녀간 장소
            else{
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.virus3));
            }// 현재로부터 7~14일 사이에 다녀간 장소
            this.nearMaker.add(this.mMap.addMarker(markerOptions));

        }
    } //주변 확진자 마커 추가
}

