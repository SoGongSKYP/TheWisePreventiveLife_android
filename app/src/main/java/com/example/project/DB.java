package com.example.project;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.sql.Connection;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class DB  extends AsyncTask<String, Void, String> {
    /*원래 설했던 필드들*/
     private ArrayList<Patient> patients;
    // public void connect_DB() { }
     public ArrayList<Patient> bring_patient_infoNroute() { return null; }
     public boolean update_DB(ArrayList<Patient> patientslist) { return true; }
     public boolean check_user_idpw(String id, String pw) throws IOException { return true;}

    //서버로 보낼 메세지, 받을 메세
    String sendMsg, receiveMsg;
    // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
    String serverip="http://10.90.1.205:8080/WLP_re/androidDB.jsp";

    //클래스 생성자
    DB(String sendMsg){
        this.sendMsg=sendMsg;
    }
    //
    @Override
    protected String doInBackground(String... strings) {

        try {
            String str;
            URL url = new URL(serverip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            if(sendMsg.equals("test_write")){
            sendMsg = "id=" + strings[0] + "&pw=" + strings[1]+"&type="+"test_write";}
            else if(sendMsg.equals("login")){
                sendMsg = "id=" + strings[0] + "&pw=" + strings[1]+"&type="+"login";
            }else if(sendMsg.equals("patients_info")){
                sendMsg = "p_id=" + strings[0] + "&p_pw=" + strings[1]+"&type="+"patients_info";
            }else if(sendMsg.equals("insert_patient")){
                sendMsg = "p_id=" + strings[0] + "&p_pw=" + strings[1]+"&type="+"insert_patient";
            }else if(sendMsg.equals("update_patient")){
                sendMsg = "p_id=" + strings[0] + "&p_pw=" + strings[1]+"&type="+"update_patient";
            }else if(sendMsg.equals("delete_patient")){
                sendMsg = "p_id=" + strings[0] + "&p_pw=" + strings[1]+"&type="+"delete_patient";
            }else if(sendMsg.equals("check_patient")){
                sendMsg = "p_id=" + strings[0] + "&p_pw=" + strings[1]+"&type="+"check_patient";
            }


            //안드로이드에서 jsp 서버로 통신값 보내
            osw.write(sendMsg);
            osw.flush();

            //jsp와 통신 성공 시 수행
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // jsp에서 보낸 값을 받는 부분
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                // 통신 실패
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //jsp로부터 받은 리턴 값
        return receiveMsg;
    }
    public String[][] jsonParserList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
//받아온 데이터를 확인합니다. 이 부분을 확인하고 싶으시면, 안드로이드 하단에 쭉쭉 뭐 뜨는거 보이시면 그거 Log들인데 거기게 흔적을 남기는 겁니다. info에서 찾아보시면 찾으실 수 있습니다.
        try{
//아까말한 {"List":[{"data1":"sfasf".""data2:"sdfsdf"}]} 이형태를 분해하는 과정입니다.
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for(int i = 0; i<jArr.length();i++){
                json = jArr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
//어떤식으로 분해하였는지 또 Log를 찍어 알아봅니다. 굳이 안 넣으셔도 됩니다.
            for(int i=0;i<parseredData.length;i++)
            {
                Log.i("JSON을 분석한 데이터"+i+":",parseredData[i][0]);
                Log.i("JSON을 분석한 데이터"+i+":",parseredData[i][1]);
                Log.i("JSON을 분석한 데이터"+i+":",parseredData[i][2]);
            }


            return parseredData;
//잘 파싱된 데이터를 넘깁니다.
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }


}
