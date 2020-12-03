package com.example.project;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PageOfAdd<STATE_DUPLE_FALSE> extends Fragment {

    /*입력 받는 컴포넌트*/
    //Spinner 관련 컴포넌트
    private Spinner bigLocSpinner, smallLocSpinner;
    private ArrayAdapter bigAdapter, smallAdapter;

    private EditText patientNumEditText, patientDateEditText;
    private Button dupleButton;

    /*RecyclerView 관련 컴포넌트*/
    private RecyclerView visitRecyclerView;
    private LinearLayoutManager layoutManager;
    //private ArrayList<Patient> patientArrayList;
    AdapterOfPlace adapter;

    /*다이얼로그 관련 컴포넌트*/
    private ImageButton addPlaceButton;
    private DialogOfPlace dialog;

    /*UI 컴포넌트*/
    private TextView dupleCheckTextView;

    /*데이터 저장 변수*/
    String pNum, pDate;
    int pBigLocal, pSmallLocal;
    ArrayList<VisitPlace> visitPlaces;
    Boolean dupleCheck, saveCheck;
    DBEntity dbEntity = new DBEntity();
    Patient patient;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_manager_add, container, false);

        patientNumEditText = v.findViewById(R.id.patient_num_EditText);
        patientDateEditText = v.findViewById(R.id.patient_date_EditText);
        patientNumEditText.setText(null);
        patientDateEditText.setText(null);

        dupleCheck = false;
        saveCheck = false;
        dupleButton = v.findViewById(R.id.patient_duple_Button);
        dupleCheckTextView = v.findViewById(R.id.duple_check_TextView);
        DupleChecking();

        /*Spinner 연결*/
        bigLocSpinner = v.findViewById(R.id.add_big_Spinner);
        smallLocSpinner = v.findViewById(R.id.add_small_Spinner);

        SetSpinnerAdapter();
        BigSpinnerAction();
        SmallSpinnerAction();
        //--------------------------------------------------------------------------------------

        /*RecyclerView 연결*/
        visitRecyclerView = v.findViewById(R.id.patient_visit_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        visitRecyclerView.setLayoutManager(layoutManager);
        visitRecyclerView.setHasFixedSize(true);

        visitPlaces = new ArrayList<>();
        adapter = new AdapterOfPlace(visitPlaces, 1);
        visitRecyclerView.setAdapter(adapter);

        adapter.setModifyClickListenter(new AdapterOfPlace.OnModifyClickListener() {
            @Override
            public void onModifyItemClick(View v, int pos) {
                visitPlaces.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        //--------------------------------------------------------------------------------------

        /*다이얼로그 연결*/
        addPlaceButton = v.findViewById(R.id.patient_visit_add_Button);
        addPlaceButton.setVisibility(View.INVISIBLE);
        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dupleCheck == true){
                    dialog = new DialogOfPlace(getContext());
                    dialog.setVisitDialogListener(new DialogOfPlace.VisitDialogListener(){
                        @Override
                        public void onAddClicked(VisitPlace visitPlace) {
                            visitPlaces.add(visitPlace);
                            adapter.notifyDataSetChanged();
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
                else{
                    Toast.makeText(getContext(), "중복 확인을 먼저 하고 동선을 추가하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //--------------------------------------------------------------------------------------


        return v;
    }

    private void DupleChecking(){
        dupleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dupleCheck == false && saveCheck == false){  // 중복 확인 버튼 클릭 후
                    if(patientNumEditText.getText() != null && patientDateEditText.getText() !=null){
                        GetDataFromUI();
                        int localNumber = pBigLocal*100+pSmallLocal;
                        String bloc=String.format("%02d",pBigLocal);
                        String sloc=String.format("%02d",pSmallLocal);
                        String plocnum=bloc+sloc;

                        //if(DBEntity.check_patient(localNumber, patientNumEditText.getText().toString())==1){
                        int check= 0;
                        try {
                            check = DBEntity.DBcheck_patient(plocnum, patientNumEditText.getText().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(check==1){
                            dupleCheckTextView.setText("중복 확인! 아래에서 확진자 동선을 추가하세요");
                            dupleCheckTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                            dupleCheck = true;
                            addPlaceButton.setVisibility(View.VISIBLE);
                            dupleButton.setText("저    장");
                            dupleButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_full_green));
                        }
                        else{
                            Toast.makeText(getContext(), "확진자 번호가 중복됩니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getContext(), "확진자 기본 정보를 모두 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(dupleCheck == true && saveCheck == false){  // 저장 버튼 클릭 후
                    //Log.d("현재 DB엔티티 더미데이터 크기 : ", Integer.toString(dbEntity.ListSize()));
                    Patient addPatient = new Patient(pSmallLocal, pBigLocal, pNum, pDate, visitPlaces);
                    try {
                        if(dbEntity.insert_patient(addPatient)==1){
                            for(int i=0; i<visitPlaces.size(); i++){
                                VisitPlace vp = visitPlaces.get(i);
                                int result2 = DBEntity.insert_pmoving(addPatient, vp);
                            }
                            dbEntity.AND_insert_patient(addPatient);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   // Log.d("지금 추가 후 DB엔티티 더미데이터 크기 : ", Integer.toString(dbEntity.ListSize()));

                    Toast.makeText(getContext(), "확진자"+pNum+" 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    dupleButton.setText("새 로 고 침");
                    addPlaceButton.setVisibility(View.INVISIBLE);
                    dupleButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_full_blue));
                    dupleCheckTextView.setText("정보가 저장되었습니다. 새로고침하여 새 정보를 입력하세요.");
                    dupleCheckTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                    saveCheck = true;
                }
                else if(dupleCheck && saveCheck){
                    Log.d("새로고침", "OK");
                    RefreshFragment();
                }
            }
        });
    }
    void RefreshFragment(){
        patientNumEditText.setText(null);
        patientDateEditText.setText(null);
        SetSpinnerAdapter();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    private void GetDataFromUI(){
        pNum = patientNumEditText.getText().toString();
        pDate = patientDateEditText.getText().toString();
        Log.d("잘 들어왔는가?", pNum+" , "+pDate);
        //String date = "20" + patientDateEditText.getText().toString();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDD");
        //Date pdate = dateFormat.parse(date);

    }


    private void SetSpinnerAdapter(){
        bigAdapter = ArrayAdapter.createFromResource(getContext(), R.array.big_location_array, android.R.layout.simple_spinner_dropdown_item);
        bigAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bigLocSpinner.setAdapter(bigAdapter);

        smallAdapter = ArrayAdapter.createFromResource(getContext(), R.array.array_0, android.R.layout.simple_spinner_dropdown_item);
        smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smallLocSpinner.setAdapter(smallAdapter);
    }

    private void BigSpinnerAction(){
        bigLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String index = Integer.toString(i);
                int resId = getResources().getIdentifier("array_"+index, "array", getContext().getPackageName());
                smallAdapter = ArrayAdapter.createFromResource(getContext(), resId, android.R.layout.simple_spinner_dropdown_item);

                pBigLocal = i;
                Log.d("ADD 큰 도시 : ", Integer.toString(i));
                smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                smallLocSpinner.setAdapter(smallAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pBigLocal = 0;
            }
        });
    }
    private void SmallSpinnerAction(){
        smallLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pSmallLocal = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pSmallLocal = 0;
            }
        });
    }

}
