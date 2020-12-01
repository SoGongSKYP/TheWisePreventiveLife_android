package com.example.project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import static android.content.Context.LOCATION_SERVICE;

/**
 *
 */
public class PageOfMyDanger extends Fragment implements OnMapReadyCallback {

    Button startButton, finishButton;
    Button findRouteButton;
    DialogOfSearch dialog;
    private Place startPlace, finishPlace;  // 검색한 결과 Place 객체
    //private UserLoc userLoc;
    private ArrayList<Patient> patient;
    private ArrayList<VisitPlace> nearPlaces; // 경로 주변 확진자
    private ArrayList<Place> visitPlaceList; // 출력 결과 경로 경유지
    private ArrayList<Place> routeList; // 입력받은 출발 도착
    private int danger; // 위험도

    private ArrayList<SearchPath> searchResultPath;

    public GoogleMap mMap;

    private Marker userPoint;
    private ArrayList<Marker> visitPlacePoint;
    private ArrayList<Marker> nearMaker;

    private LatLng myLatLng;
    private MapView mapView;
    private CalRoute cl;
    private ResultCallbackListener listener;

    /*RecyclerView 관련 함수*/
    private LinearLayout routeLayout;
    private RecyclerView pathRecyclerView;
    private LinearLayoutManager layoutManager;
    private AdapterOfRow adapter;
    private SearchPath selectedPath; //사용자가 선택한 루트

    /*결과 관련 컴포넌트*/
    private LinearLayout resultLayout;
    private ImageView redImageView, yellowImageView, greenImageView;
    private TextView resultTextView;

    public PageOfMyDanger() throws InterruptedException {
        //this.userLoc=new UserLoc();
        this.visitPlacePoint = new ArrayList<Marker>();
        this.nearMaker = new ArrayList<Marker>();
        this.patient =new ArrayList<Patient>();
        this.patient=DBEntity.getPatientList();
        this.nearPlaces =new ArrayList<VisitPlace>();
        this.searchResultPath = new ArrayList<SearchPath>();
        this.visitPlaceList = new ArrayList<Place>();
        this.routeList =new ArrayList<Place>();
        this.danger=0;
        this.startPlace = new Place(null,0,0);
        this.finishPlace= new Place(null,0,0);
        this.cl=new CalRoute(getContext(),null,null);
        this.selectedPath= null;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_user_danger, container, false);
        startButton = v.findViewById(R.id.starting_point_Button);
        finishButton = v.findViewById(R.id.finish_point_Button);
        findRouteButton = v.findViewById(R.id.search_route_Button);

        routeLayout = v.findViewById(R.id.danger_route_LinearLayout);
        routeLayout.setVisibility(View.INVISIBLE);
        resultLayout = v.findViewById(R.id.danger_result_LinearLayout);
        resultLayout.setVisibility(View.INVISIBLE);

        pathRecyclerView = v.findViewById(R.id.danger_route_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        pathRecyclerView.setLayoutManager(layoutManager);
        pathRecyclerView.setHasFixedSize(true);
        adapter = new AdapterOfRow(getContext(), searchResultPath);
        pathRecyclerView.setAdapter(adapter);

        redImageView = v.findViewById(R.id.danger_red_ImageView);
        yellowImageView = v.findViewById(R.id.danger_yellow_ImageView);
        greenImageView = v.findViewById(R.id.danger_green_ImageView);
        resultTextView = v.findViewById(R.id.danger_result_TextView);

        // 출발지를 누르면 다이얼로그가 생성됨
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DialogOfSearch(getContext());
                dialog.setSearchDialogListener(new DialogOfSearch.SearchDialogListener() {
                    @Override
                    public void onOKCliked(Place place) {
                        // 다이얼로그에서 결과 Place 객체 받아서 저장 (DialogOfSearch)
                        if(place !=null){
                            startPlace = place;
                            startButton.setText(startPlace.get_placeAddress());
                        }
                        else{
                            Toast.makeText(getContext(), "장소가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
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
        // 도착지를 누르면 다이얼로그가 생성됨
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DialogOfSearch(getContext());
                dialog.setSearchDialogListener(new DialogOfSearch.SearchDialogListener() {
                    @Override
                    public void onOKCliked(Place place) {
                        // 다이얼로그에서 결과 Place 객체 받아서 저장 (DialogOfSearch)
                        if(place !=null){
                            finishPlace = place;
                            finishButton.setText(finishPlace.get_placeAddress());
                            if(startPlace != null && finishPlace != null){
                                //동선 검색 기능
                                try {
                                    cl = new CalRoute(getContext(),startPlace,finishPlace);
                                    listener=cl.calRoute1();
                                    searchResultPath=listener.getResultPath();
                                    System.out.println("버튼 안에서"+searchResultPath.size());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "장소가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
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
        try {
            SearchRouteButtonAction();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mapView = v.findViewById(R.id.danger_MapView);
        mapView.getMapAsync(this);
        return v;
    }

    /*경로 검색 버튼 누르면 실행되는 함수*/
    void SearchRouteButtonAction() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        findRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startPlace.get_placeAddress() != null && finishPlace.get_placeAddress() != null){
                    try {
                        listener=cl.calRoute1();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //동선 찾기 버튼을 누르면 루트 결과 레이아웃이 보이면서, 결과 보여줌
                    Log.d("동선 Recyclerview", "OK");
                    routeLayout.setVisibility(View.VISIBLE);
                    resultLayout.setVisibility(View.GONE);
                    Log.d("지금 결과 데이터의 크기는?", Integer.toString(searchResultPath.size()));
                    adapter = new AdapterOfRow(getContext(), searchResultPath);
                    pathRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setRowClickListener(new AdapterOfRow.onRowClickListener() {
                        @Override
                        public void onRowClick(View view, int pos) {
                            Toast.makeText(getContext(), "이동 경로를 선택하였습니다.", Toast.LENGTH_SHORT).show();
                            selectedPath = searchResultPath.get(pos);
                            routeLayout.setVisibility(View.INVISIBLE);
                            resultLayout.setVisibility(View.VISIBLE);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lock.lock();
                                    try {
                                        addVisitNearMarker();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    lock.unlock();
                                }
                            });
                        }
                    });

                }
                else{
                    Toast.makeText(getContext(), "출발지와 도착지를 입력했는지 확인하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        //LocPermission(getActivity(),getContext());
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
    } // 유저 현위치에 마커 추가


    public void RefreshMarker() {
        System.out.print("5");
        this.userPoint.remove();
        this.myLatLng = new LatLng(UserLoc.getUserPlace().get_placeX(), UserLoc.getUserPlace().get_placeY());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(this.myLatLng);
        markerOptions.title("사용자");
        markerOptions.snippet("현재 위치 GPS");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myicon1));
        this.userPoint = this.mMap.addMarker(markerOptions);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.myLatLng, 15));
    }

    public void addVisitNearMarker() throws ParseException {
        calVisitPlace();
        calNearPlace();
        for(int i =0; i < visitPlacePoint.size();i++){
            visitPlacePoint.get(i).remove();
        }
        visitPlacePoint.clear();
        for(int i=0; i < visitPlaceList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(visitPlaceList.get(i).get_placeX(),visitPlaceList.get(i).get_placeY());
            markerOptions.position(latLng);
            markerOptions.title(visitPlaceList.get(i).get_placeAddress());
            markerOptions.snippet(visitPlaceList.get(i).get_placeDetailAddress());
            this.visitPlacePoint.add(this.mMap.addMarker(markerOptions));
        }//우리가 다녀간 경유지
        for(int i=0; i<nearMaker.size();i++){
            nearMaker.get(i).remove();
        }
        nearMaker.clear();
        Date time = new Date();
        for(int i =0 ; i<nearPlaces.size();i++){
            String from = nearPlaces.get(i).getVisitDate();
            SimpleDateFormat transDate= new SimpleDateFormat("yyyy-MM-dd");
            Date to = transDate.parse(from);
            long dateGap = (time.getTime()- to.getTime())/(1000*60*60*24);

            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(nearPlaces.get(i).getVisitPlace().get_placeX(),nearPlaces.get(i).getVisitPlace().get_placeY());
            markerOptions.position(latLng);
            markerOptions.title(nearPlaces.get(i).getVisitPlace().get_placeAddress());
            markerOptions.snippet(nearPlaces.get(i).getVisitPlace().get_placeDetailAddress());
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
        }// 근처 확진자 동선

    }

    public void calVisitPlace() {
        this.visitPlaceList.clear();
        this.visitPlaceList.add(startPlace);
        for (int j=0;j<selectedPath.getSubPaths().size();j++){
            if(selectedPath.getSubPaths().get(j).getTrafficType()!=3){
                this.visitPlaceList.add(selectedPath.getSubPaths().get(j).getStartStation());
                this.visitPlaceList.add(selectedPath.getSubPaths().get(j).getEndStation());
            }
        }// 경로 상 모든 들리는 장소를 경유지 리스트에 넣어줌
        this.visitPlaceList.add(finishPlace);
        System.out.println(this.visitPlaceList.size());
    }
    public void calNearPlace() {
        this.nearPlaces.clear();
        for (int a = 0; a < this.patient.size(); a++) {
            for (int b = 0; b < this.patient.get(a).getVisitPlaceList().size(); b++) {
                for (int v = 0;  v<this.visitPlaceList.size();v++) {
                    if (this.patient.get(a).getVisitPlaceList().get(b).
                            Distance(this.visitPlaceList.get(v).get_placeX(),
                                    this.visitPlaceList.get(v).get_placeY(), "kilometer") <= 1) {
                        this.nearPlaces.add(this.patient.get(a).getVisitPlaceList().get(b));
                    }
                }
            }
        }
        calDanger();
    }//반경 1km이내 확진자 동선

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            RefreshMarker();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

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


    public void calDanger(){
        if(this.nearPlaces.size() >= 10){
            redImageView.setImageResource(R.drawable.circle_red);
            yellowImageView.setImageResource(R.drawable.circle_yellow_trans);
            greenImageView.setImageResource(R.drawable.circle_green_trans);
            resultTextView.setText("검색하신 외출 동선의 위험도는 '심각' 입니다.");
        }
        else if((this.nearPlaces.size() >= 5) && (this.nearPlaces.size()<50)){
            redImageView.setImageResource(R.drawable.circle_red_trans);
            yellowImageView.setImageResource(R.drawable.circle_yellow);
            greenImageView.setImageResource(R.drawable.circle_green_trans);
            resultTextView.setText("검색하신 외출 동선의 위험도는 '주의' 입니다.");
        }else{
            redImageView.setImageResource(R.drawable.circle_red_trans);
            yellowImageView.setImageResource(R.drawable.circle_yellow_trans);
            greenImageView.setImageResource(R.drawable.circle_green);
            resultTextView.setText("검색하신 외출 동선의 위험도는 '안전' 입니다.");
        }
    }


}
