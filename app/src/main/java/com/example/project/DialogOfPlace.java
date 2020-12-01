package com.example.project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class DialogOfPlace extends Dialog {

    private Context mContext;
    ImageButton dialogDismissButton, searchButton;
    Button placeAddButton;
    EditText visitDateEditText, searchEditText;
    TextView visitPlaceTextView, visitDetailTextView;

    /*서치바 관련 컴포넌트*/
    RecyclerView searchRecyclerView;
    AdapterOfSearch adapter;
    ArrayList<Place> resultPlaces;
    LinearLayoutManager layoutManager;
    DialogOfPlace.VisitDialogListener visitDialogListener;
    Place selectedRow;
    private FindPlace fp;


    interface VisitDialogListener{
        void onAddClicked(VisitPlace visitPlace);
    }

    public void setVisitDialogListener(DialogOfPlace.VisitDialogListener visitDialogListener){
        this.visitDialogListener = visitDialogListener;
    }

    public DialogOfPlace(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_patient_visit);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogDismissButton = findViewById(R.id.dialog_dismiss_Button);
        placeAddButton = findViewById(R.id.dialog_add_Button);
        visitDateEditText = findViewById(R.id.dialog_visit_date_EditText);
        visitPlaceTextView = findViewById(R.id.dialog_visit_select_title);
        visitDetailTextView = findViewById(R.id.dialog_visit_select_detail);

        searchButton = findViewById(R.id.dialog_search_button);
        searchEditText = findViewById(R.id.dialog_search_bar_EditText);

        searchRecyclerView = findViewById(R.id.dialog_visit_RecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setHasFixedSize(true);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchEditText.getText().toString().equals("") || searchEditText.getText().toString() == null){
                    Toast.makeText(getContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 여기에서 검색 구현
                    // 검색 결과는 resultPlaces 배열에 넣으면 됩니다. (지금은 dummyData()로 더미 데이터 넣음)
                    String searchPlace = searchEditText.getText().toString();
                    fp = new FindPlace(searchPlace);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            fp.searchPlace();
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    resultPlaces = fp.getSearchLocList();

                    adapter = new AdapterOfSearch(resultPlaces);
                    searchRecyclerView.setAdapter(adapter);
                    adapter.setOnSearchClickListenter(new AdapterOfSearch.OnSearchClickListener() {
                        @Override
                        public void onSearchItemCick(View v, int pos) {
                            // 연관 장소 리스트 중에서 클릭해서 선택
                            // 선택한 Place 객체가 selectedRow
                            selectedRow = resultPlaces.get(pos);
                            visitPlaceTextView.setText(selectedRow.get_placeAddress());
                            visitDetailTextView.setText(selectedRow.get_placeDetailAddress());
                        }
                    });
                }
            }
        });


        dialogDismissButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        placeAddButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(visitDateEditText.getText().toString().equals("") || visitDateEditText.getText().toString() == null){
                    Toast.makeText(getContext(), "방문 날짜를 입력하세요", Toast.LENGTH_SHORT).show();
                }else if(selectedRow == null){
                    Toast.makeText(getContext(), "장소를 검색해서 선택하세요", Toast.LENGTH_SHORT).show();
                }else {
                    String date = visitDateEditText.getText().toString();
                    VisitPlace visitPlace = new VisitPlace(selectedRow, date);
                    visitDialogListener.onAddClicked(visitPlace);
                    dismiss();
                }
            }
        });
    }

}
