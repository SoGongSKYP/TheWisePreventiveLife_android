package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class PageOfList extends Fragment {

    /*Spinner 관련 컴포넌트*/
    private Spinner bigLocSpinner, smallLocSpinner;
    private ArrayAdapter bigAdapter, smallAdapter;

    /*RecyclerView 관련 컴포넌트*/
    private RecyclerView patientRecyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<Patient> patientArrayList, totalArrayList;
    private AdapterOfList adapter;

    /*데이터 저장 변수*/
    int pBigLocal, pSmallLocal;
    Patient selectedRow, deletePatient;
    int testPos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(totalArrayList == null){
            try {
                totalArrayList = DBEntity.patient_info();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("리스트 페이지에 Total Array : ", "NULL");
        }
        Log.d("onCreate", "1");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_manager_list, container, false);
        Log.d("onCreateView", "2");
        pBigLocal=0;
        pSmallLocal=0;
        /*Spinner 연결*/
        bigLocSpinner = v.findViewById(R.id.list_big_Spinner);
        smallLocSpinner = v.findViewById(R.id.list_small_Spinner);

        bigAdapter = ArrayAdapter.createFromResource(getContext(), R.array.big_location_array, android.R.layout.simple_spinner_dropdown_item);
        bigAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bigLocSpinner.setAdapter(bigAdapter);

        smallAdapter = ArrayAdapter.createFromResource(getContext(), R.array.array_0, android.R.layout.simple_spinner_dropdown_item);
        smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smallLocSpinner.setAdapter(smallAdapter);

        BigSpinnerAction();
        SmallSpinnerAction();
        //--------------------------------------------------------------------------------------
        /*RecyclerView 연결*/

        patientArrayList = new ArrayList<Patient>();

        patientRecyclerView = v.findViewById(R.id.list_RecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        patientRecyclerView.setLayoutManager(layoutManager);
        patientRecyclerView.setHasFixedSize(true);

        adapter = new AdapterOfList(patientArrayList);
        patientRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnListClickListenter(new AdapterOfList.OnListClickListener() {
            @Override
            public void onListItemClick(View v, int pos) {
                selectedRow = patientArrayList.get(pos);
                testPos = pos;
                Intent intent = new Intent(getActivity(), ManagerModify.class);
                intent.putExtra("row", (Serializable) selectedRow);
                startActivity(intent);
            }
        });

        //--------------------------------------------------------------------------------------
        /* ManagerModify에서 전체삭제 버튼을 눌렀을 때, 해당 ROW 삭제*/
        // DBEntity_patient_delete
        adapter.setOnDeleteClickListener(new AdapterOfList.onDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, int pos) {
                TotalDeleteButtonAction(pos);
                //DBEntity에서도 삭제
            }
        });


        return v;
    }


    private void BigSpinnerAction(){
        bigLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String index = Integer.toString(i);
                int resId = getResources().getIdentifier("array_"+index, "array", getContext().getPackageName());
                smallAdapter = ArrayAdapter.createFromResource(getContext(), resId, android.R.layout.simple_spinner_dropdown_item);
                pBigLocal = i;
                patientArrayList.clear();
                showList(pBigLocal, 0);
                Log.d("큰 도시 선택 : ", Integer.toString(i));
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
                Log.d("작은 도시 선택 : ", Integer.toString(i));
                if(pBigLocal>7){
                    patientArrayList.clear();
                    showList(pBigLocal, pSmallLocal);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                patientArrayList.clear();
                showList(pBigLocal, 0);
            }
        });
    }

    private void showList(int bigLoc, int smallLoc){
        for(int i = 0;i<totalArrayList.size();i++){
            Patient row =  totalArrayList.get(i);
            if(row.getBigLocalNum() == pBigLocal && row.getSmallLocalNum() == pSmallLocal){
                Log.d("pBigLocal", Integer.toString(pBigLocal));
                patientArrayList.add(row);
            }
            else{
                Log.d("List Recyclerview : ", "현재 데이터가 없습니다.");
            }
        }
    }

    private void TotalDeleteButtonAction(final int pos){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("현재 확진자의 정보를 모두 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete data
                                Patient removePatient = patientArrayList.get(pos);
                                patientArrayList.remove(removePatient);
                                totalArrayList.remove(removePatient);
                                adapter.notifyDataSetChanged();

                                DBEntity.AND_delete_patient(removePatient);
                                Log.d("삭제되었나?", Integer.toString(DBEntity.ListSize()));
                                Toast.makeText(getContext(), "현재 확진자 정보를 삭제합니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
