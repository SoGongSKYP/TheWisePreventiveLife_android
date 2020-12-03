package com.example.project;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
/**
 * Used in UserPages
 * 장소 검색 다이얼로그
 *   - 검색한 장소로 이동 ; PageOfMain, PageOfSelectedClinic
 *   - 출발, 도착지 검색 ; PageOfMyDanger
 */
public class DialogOfSearch extends Dialog {

    private Context context;
    EditText searchEditText;
    ImageButton searchButton, dismissButton;
    Button okButton;
    TextView placeTitleTextView, placeDetailTextView;

    RecyclerView searchRecyclerView;
    AdapterOfSearch adapter;
    ArrayList<Place> resultPlaces;
    LinearLayoutManager layoutManager;
    SearchDialogListener searchDialogListener;
    Place selectedRow;
    private FindPlace fp;

    interface SearchDialogListener{
        // 다이얼로그에서 Fragment로 Place객체 보내주기 위한 리스너
        void onOKCliked(Place place) throws JSONException;
    }

    public void setSearchDialogListener(SearchDialogListener searchDialogListener){
        this.searchDialogListener = searchDialogListener;
        this.resultPlaces = new ArrayList<Place>();
    }

    public DialogOfSearch(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_place);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        searchEditText = findViewById(R.id.dialog_place_search_bar_EditText);
        searchButton = findViewById(R.id.dialog_place_search_button);
        placeTitleTextView = findViewById(R.id.dialog_search_select_title);
        placeDetailTextView = findViewById(R.id.dialog_search_select_detail);
        dismissButton = findViewById(R.id.dialog_search_dismiss_Button);
        okButton = findViewById(R.id.search_dialog_OK_Button);

        searchRecyclerView = findViewById(R.id.dialog_search_RecyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setHasFixedSize(true);

        // 검색 버튼
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
                            placeTitleTextView.setText(selectedRow.get_placeAddress());
                            placeDetailTextView.setText(selectedRow.get_placeDetailAddress());
                        }
                    });
                }
            }
        });


        // 확인 버튼
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 선택한 Place 객체 반환
                try {
                    searchDialogListener.onOKCliked(selectedRow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

}
