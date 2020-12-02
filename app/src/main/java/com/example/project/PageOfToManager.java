package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PageOfToManager extends AppCompatActivity {

    private OkHttpClient client;
    private String OkhttpUrl;
    private Request request;
    private MediaType mediaType;
    private RequestBody body;
    static String result="s";

    /*Tool Bar 관련 컴포넌트*/
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView TitleTextView;

    /*관리자 로그인 관련 컴포넌트*/
    Button toManagerButton;
    Dialog loginDialog;
    private TextInputEditText managerIDEditText, managerPWEditText;
    private Button loginButton;
    private ImageButton dismissButton;
    private String managerID, managerPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_manager);

        /*Tool Bar 연결*/
        toolbar = findViewById(R.id.toManager_Toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TitleTextView = findViewById(R.id.toManager_Title_TextView);
        TitleTextView.setText("사용자 어플리케이션 정보");
        //----------------------------------------------------------------------

        /*관리자 로그인 다이얼로그 연결*/
        loginDialog = new Dialog(PageOfToManager.this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.dialog_manager_login);

        managerIDEditText = loginDialog.findViewById(R.id.manager_login_id_EditText);
        managerPWEditText = loginDialog.findViewById(R.id.manager_login_pw_EditText);
        loginButton = loginDialog.findViewById(R.id.manager_login_Button);
        dismissButton = loginDialog.findViewById(R.id.manager_login_dismiss_Button);
        //----------------------------------------------------------------------

        /*다이얼로그 버튼 연결*/
        toManagerButton = findViewById(R.id.switch_to_manager_Button);
        toManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialogAction();
            }
        });
        //----------------------------------------------------------------------
        client = new OkHttpClient().newBuilder().build();
        mediaType = MediaType.parse("text/plain");
        OkhttpUrl="http://192.168.1.196:8080/WLP_re/androidDB.jsp";
        //----------------------------------------------------------------------
    }

    /* 다이얼로그 기능 구현 메소드 */
    private void makeDialogAction() {
        loginDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(loginDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = loginDialog.getWindow();
        window.setAttributes(lp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                managerID = managerIDEditText.getText().toString();
                managerPW = managerPWEditText.getText().toString();
                //b. Make the request: okhttp3.Request
                RequestBody body =new FormBody.Builder().add("id",managerID).add("pw",managerPW).add("type","login").build();
                Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();

//c. Read the response: okhttp.Callback[1. failure: onFailure 2. success: onResponse]
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("연결 실패", "error Connect Server error is"+e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            //final JSONObject jsonObject = new JSONObject(response.body().string());
                            result =response.body().string();
                            Boolean b = result.equals("success");
                            Log.d("진짜 같냐? ", b.toString());
                            Log.d("젼쥬", result);
                            // Log.d("response jsp db",response.body().string()+"");
                            if (result.equals("\n\n\n\n\nsuccess")) {
                                //makeText(PageOfToManager.this, "로그인", LENGTH_SHORT).show();
                                Log.d("로그인", result);
                                Intent intent = new Intent(getApplicationContext(), ManagerPages.class);
                                intent.putExtra("managerID", managerID);
                                intent.putExtra("managerPW", managerPW);
                                startActivity(intent);
                                finish();
                            } else if (result.equals("\n\n\n\n\nfailed")) {
                                Log.d("아이디/비번이 틀디", result);
                                //makeText(PageOfToManager.this, "아이디 또는 비밀번호가 틀렸음", LENGTH_SHORT).show();
                                managerIDEditText.setText("");
                                managerPWEditText.setText("");
                            } else if (result.equals("\n\n\n\n\nnoID")) {
                                Log.d("존재하지 않는 아이디", result);
                                //Toast.makeText(PageOfToManager.this, "존재하지 않는 아이디", Toast.LENGTH_SHORT).show();
                                managerIDEditText.setText("");
                                managerPWEditText.setText("");
                            }else{
                                Log.d("서버 오류", result);
                                //Toast.makeText(PageOfToManager.this, "서버 오류", Toast.LENGTH_SHORT).show();
                                managerIDEditText.setText("");
                                managerPWEditText.setText("");
                            }
                            loginDialog.dismiss();
                        }
                    }

                });
                // int dbtest = new DBEntity().login(managerID, managerPW);//DB함수 호출하기

            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
            }
        });
    }
}
