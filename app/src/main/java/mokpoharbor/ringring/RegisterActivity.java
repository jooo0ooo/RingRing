package mokpoharbor.ringring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity{

    private Button register_with_facebook_stu;
    private Button register_with_facebook_pro;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        setContentView(R.layout.activity_register);
        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자

        //액티비티 타이틀바 내용 설정
        setTitle("Register");

        //내가 잘 선택했나 이미지 클릭시 이미지를 바꿔주는 효과를 주었습니다.
        register_with_facebook_stu = (Button) findViewById(R.id.register_student);
        register_with_facebook_stu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        register_with_facebook_stu.setBackgroundResource(R.drawable.student_touch);
                    case MotionEvent.ACTION_UP:
                        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this,
                                Arrays.asList("public_profile", "user_friends"));
                        LoginManager.getInstance().registerCallback(callbackManager,
                                new FacebookCallback<LoginResult>() {
                                    @Override
                                    public void onSuccess(LoginResult loginResult) {
                                        Log.e("onSuccess", "onSuccess");
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.e("onCancel", "onCancel");
                                        Toast.makeText(RegisterActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(FacebookException exception) {
                                        Log.e("onError", "onError " + exception.getLocalizedMessage());
                                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                }
                return true;
            }
        });

        register_with_facebook_pro = (Button) findViewById(R.id.register_professor);
        register_with_facebook_pro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        register_with_facebook_pro.setBackgroundResource(R.drawable.professor_touch);
                    case MotionEvent.ACTION_UP:
                        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this,
                                Arrays.asList("public_profile", "user_friends"));
                        LoginManager.getInstance().registerCallback(callbackManager,
                                new FacebookCallback<LoginResult>() {
                                    @Override
                                    public void onSuccess(LoginResult loginResult) {
                                        Log.e("onSuccess", "onSuccess");
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, ProfessorMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.e("onCancel", "onCancel");
                                        Toast.makeText(RegisterActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(FacebookException exception) {
                                        Log.e("onError", "onError " + exception.getLocalizedMessage());
                                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                }
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
