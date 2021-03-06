package com.example.project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DBEntity {

    /*DBEntity에서 계속 저장할 환자 리스트*/
    private static ArrayList<Patient> patientList=new ArrayList<Patient>();

    /*DB 연동시 필요한 필드들*/

    private static String result="imsi";//일단 임시 success


    private static OkHttpClient client=new OkHttpClient().newBuilder().build();
    private static String OkhttpUrl="http://3.35.210.3:8080/WLP_re/androidDB.jsp";
    private static MediaType mediaType=MediaType.parse("text/plain");

    private static JSONArray patients=null;
    private static JSONArray movingList=null;


    //patientList 값 접-----------------------------------------------------------------------------------------------

    public DBEntity() { }

    public void setPatientList(ArrayList<Patient> patientList) {
        this.patientList = patientList;
    }

    public static ArrayList<Patient> getPatientList() {
        return patientList;
    }

    public static int ListSize(){
        return patientList.size();
    }



    // DB테이블 수정 메소드---------------------------------------------------------------------------------------------------

    public static void connectAppDB() throws InterruptedException {
        RequestBody body =new FormBody.Builder().add("type","patients_info").build();
        Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("연결 실패", "error Connect Server error is"+e.toString());
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        patients = new JSONArray(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                    Log.d("patient json array",patients.toString());
                }
            }
        });
      
        countDownLatch.await();
    }

    public static void connectMovingDB() throws InterruptedException {

        RequestBody body =new FormBody.Builder().add("type","pmoving_info").build();
        Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        movingList = new JSONArray(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                    Log.d("pmoving json array",movingList.toString());
                }
            }
        });
        countDownLatch.await();
    }

    /*인트로 때, 앱 실행시 한 번만 불리는 메소드*/
    public static ArrayList<Patient> patient_info() throws JSONException {
        ArrayList<VisitPlace> temp = new ArrayList<VisitPlace>();

        for(int i =0; i < patients.length();i++){
            JSONObject patient = patients.getJSONObject(i);
            temp = new  ArrayList<VisitPlace>();
            String plocnum = patient.getString("plocnum");
            String checkNum = plocnum+patient.getString("pnum");
            int small=Integer.parseInt(plocnum.substring(2));
            int big=Integer.parseInt(plocnum.substring(0,2));


            for(int j =0; j < movingList.length();j++){
                JSONObject moving = movingList.getJSONObject(j);
                String movingCheckNum = moving.getString("plocnum")+moving.getString("pnum");
                if(checkNum.equals(movingCheckNum)){
                    temp.add(new VisitPlace(new Place(moving.getString("address"),
                            moving.getDouble("pointx"),moving.getDouble("pointy")),moving.getString("visitdate")));
                }
            }
            patientList.add(new Patient(small,big,patient.getString("pnum")
                    ,patient.getString("confirmdate"),temp));
        }
        return patientList;
    }

    /*로그인 메소드드*/
    public static int login(String managerID, String managerPW) throws InterruptedException {
            RequestBody body = new FormBody.Builder().add("id", managerID).add("pw", managerPW).add("type", "login").build();
            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        result =response.body().string();
                        countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if(result.equals("\n\n\n\nsuccess")) return 1;
            else if(result.equals("\n\n\n\nfailed")) return 0;
            else if(result.equals("\n\n\n\nnoID")) return 2;
            else return -1;
    }

        /*환자번호와 지역번호로 중복되는 환자가 있는지 확인하는 메소드*/
        public static int DBcheck_patient(String plocnum, String pnum) throws InterruptedException {
            RequestBody body = new FormBody.Builder().add("pnum", pnum).add("plocnum", plocnum).add("type", "check_patient").build();
            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    result =response.body().string();
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if(result.equals("\n\n\n\nnewP")) return 1;
            else if(result.equals("\n\n\n\nexist")) return 0;
            else return -1;
        }

        /*관리자의 확진자 추가 페이지에서 확진자 정보를 추가하는 메소-동선 제외 */
        public static int insert_patient(Patient patient) throws InterruptedException {
            String pnum=patient.getPatientNum();
            String bloc=String.format("%02d",patient.getBigLocalNum());
            String sloc=String.format("%02d",patient.getSmallLocalNum());
            String plocnum=bloc+sloc;
            String confirmdate=patient.getConfirmDate();
            RequestBody body = new FormBody.Builder().add("pnum", pnum).add("plocnum", plocnum).add("confirmdate", confirmdate).add("type", "insert_patient").build();
            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    result =response.body().string();
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if(result.equals("\n\n\n\nsuccess")) return 1;
            else return -1;
        }

        /*관리자의 확진자 동선 정보 하나를 추가하는 메소드-무조건 해당 환자에 한 정보가 테이블에 저장되어 있어야 함. */
        public static int insert_pmoving(Patient patient, VisitPlace visitplace) throws InterruptedException {
            String pnum=patient.getPatientNum();
            String bloc=String.format("%02d",patient.getBigLocalNum());
            String sloc=String.format("%02d",patient.getSmallLocalNum());
            String plocnum=bloc+sloc;
            String visitdate=visitplace.getVisitDate();
            Double pointx=visitplace.getVisitPlace().get_placeX();
            Double pointy=visitplace.getVisitPlace().get_placeY();
            String address=visitplace.getVisitPlace().get_placeAddress();

            RequestBody body = new FormBody.Builder().add("pnum", pnum).add("plocnum", plocnum).add("visitdate", visitdate).add("pointx", String.valueOf(pointx)).add("pointy", String.valueOf(pointy)).add("address", address).add("type", "insert_pmoving").build();
            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    result =response.body().string();
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            if(result.equals("\n\n\n\nsuccess")) return 1;
            else return -1;
        }

        /*관리자의 확진자 동선 정보 하나를 삭제하는 메소드 */
        public static int delete_pmoving(Patient patient, VisitPlace visitplace) throws InterruptedException {
            String pnum=patient.getPatientNum();
            String bloc=String.format("%02d",patient.getBigLocalNum());
            String sloc=String.format("%02d",patient.getSmallLocalNum());
            String plocnum=bloc+sloc;
            String visitdate=visitplace.getVisitDate();
            Double pointx=visitplace.getVisitPlace().get_placeX();
            Double pointy=visitplace.getVisitPlace().get_placeY();

            //pnum, plocnum, visitdate, Double.parseDouble(pointx), Double.parseDouble(pointy), address);
            RequestBody body = new FormBody.Builder().add("pnum", pnum).add("plocnum", plocnum).add("visitdate", visitdate).add("pointx", String.valueOf(pointx)).add("pointy", String.valueOf(pointy)).add("type", "delete_pmoving").build();
            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    result =response.body().string();
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if(result.equals("\n\n\n\nsuccess")) return 1;
            else return -1;
        }

        /*관리자의 확진자 추가, 수정 페이지에서 확진자 동선 정보 하나를 삭제하는 메소드-환자 정보를 삭제하면 관련 동선 정보도 싹다 삭제*/
        public static int delete_patient(Patient patient) throws InterruptedException {
            String pnum=patient.getPatientNum();
            String bloc=String.format("%02d",patient.getBigLocalNum());
            String sloc=String.format("%02d",patient.getSmallLocalNum());
            String plocnum=bloc+sloc;

            RequestBody body = new FormBody.Builder().add("pnum", pnum).add("plocnum", plocnum).add("type", "delete_patient").build();
            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    result =response.body().string();
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if(result.equals("\n\n\n\nsuccess")) return 1;
            else if(result.equals("\n\n\n\nfail")) return 0;
            else return -1;
        }



    //Android 프로젝트에 저장되어 있는 patientlist 수정 메소드: DB 메소드 성공----------------------------------------------------------------------------------------------------

    /*관리자의 확진자 추가 페이지에서 확진자 정보를 추가하는 메소-동선 제외 */
    public void AND_insert_patient(Patient patient) {
        patientList.add(patient);
    }

    /*관리자의 확진자 추가 페이지에서 확진자 동선 정보 하나를 추가하는 메소드-무조건 해당 환자에 한 정보가 테이블에 저장되어 있어야 함. */
    public static int AND_insert_pmoving(Patient patient, VisitPlace visitplace) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patient.getSmallLocalNum() == patientList.get(i).getSmallLocalNum() && patient.getBigLocalNum() == patientList.get(i).getBigLocalNum()
                    && patient.getPatientNum().equals(patientList.get(i).getPatientNum())) {
                //조건문 바꿔야 함! patient 테이블의 프라이머리 키는 지역번호랑 환자번호! 즉 patient와 patientList.get(i)의 정보가 같을 두객체가 같을 때——————————————————————————————————————————————————patient는 smalllocalnum,bigloculnum,patientnum 같으면 같은 객체!
                patientList.get(i).getVisitPlaceList().add(visitplace);
                return 1;// 동선 삽입 성공
            }
        }
        return 0;//동선 삽입 실패: 해당 환자 정보가 없음
    }


    /*관리자의 확진자 수정 페이지에서 확진자 동선 정보 하나를 삭제하는 메소드 */
    public static int AND_delete_pmoving(Patient patient, VisitPlace visitplace) {
        //patient_info();
        for (int i = 0; i < patientList.size(); i++) {
            //1. 환자리스트를 돌며 동일한 환자 찾기.
            //DB테이블에서의 primary 키값들의 값이 같을 때 동일 환자.
            if (patient.getSmallLocalNum() == patientList.get(i).getSmallLocalNum() && patient.getBigLocalNum() == patientList.get(i).getBigLocalNum()
                    && patient.getPatientNum().equals(patientList.get(i).getPatientNum())){
                //칮으면 해당 환자의 visitplacelist를 돌며 매개변수로 온 visitplace 찾고 삭제하기
                int t = findIndex(patientList.get(i).getVisitPlaceList(),visitplace);
                if(t!=-1){
                    patientList.get(i).getVisitPlaceList().remove(t);
                }
                Log.d("patient",Integer.toString(patientList.get(i).getVisitPlaceList().size()));
                return 1;//정상 삭제
            }
        }
        return -1;//환자 정보도 없을 경우
    }

    public static int findIndex(ArrayList<VisitPlace> visitplaceList,VisitPlace visitplace2){
        for(int i =0; i <visitplaceList.size();i++){
            if(visitplaceList.get(i).getVisitDate().equals(visitplace2.getVisitDate())&&
                    visitplaceList.get(i).getVisitPlace().get_placeAddress().equals(visitplace2.getVisitPlace().get_placeAddress())){
                return i;
            }
        }
        return -1;
    }

    /*관리자의 확진자 추가, 수정 페이지에서 확진자 동선 정보 하나를 삭제하는 메소드-환자 정보를 삭제하면 관련 동선 정보도 싹다 삭제*/
    public static void AND_delete_patient(Patient patient) {
        patientList.remove(patient);
    }

    /*환자번호와 지역번호로 중복되는 환자가 있는지 확인하는 메소드*/
    public static int check_patient(int LocalNumber, String patientNum) {
        if (result.equals("success"))
        {
            for(int i =0 ;i< patientList.size();i++){
                int check =  patientList.get(i).getBigLocalNum()*100 + patientList.get(i).getSmallLocalNum();
                if(LocalNumber==check && patientNum.equals(patientList.get(i).getPatientNum())){
                    return 0;
                }
            }
        }
        return 1;
    }
}
