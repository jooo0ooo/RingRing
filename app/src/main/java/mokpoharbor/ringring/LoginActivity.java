package mokpoharbor.ringring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    //아래 코드 짧고 존나 쉬워보이죠? ㅋ 이리저리 삽질하면서 수 많은 시행착오를 거치느라 5시간 걸림

    private ImageView login_with_facebook;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자


        login_with_facebook = (ImageView)findViewById(R.id.login_with_facebook);
        login_with_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(final LoginResult loginResult) {
                                Log.e("onSuccess", "onSuccess");



                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try{
                                            Log.e("user profile",object.toString());

                                            PropertyManager.getInstance().set_user_id("");
                                            PropertyManager.getInstance().set_user_email("");
                                            PropertyManager.getInstance().set_user_facebookid("");
                                            PropertyManager.getInstance().set_user_fcmtoken("");
                                            PropertyManager.getInstance().set_user_gender("");
                                            PropertyManager.getInstance().set_user_profileimageurl("");
                                            PropertyManager.getInstance().set_user_name("");

                                            Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_SHORT).show();


                                            //해당 파싱된 정보를 공유 저장소에 저장//
                                            PropertyManager.getInstance().set_user_id(object.getJSONObject("id").toString());
                                            PropertyManager.getInstance().set_user_email(object.getJSONObject("email").toString());
                                            //PropertyManager.getInstance().set_user_facebookid(object.getJSONObject("acces").toString());
                                            //PropertyManager.getInstance().set_user_fcmtoken(object.getJSONObject("id").toString());
                                            PropertyManager.getInstance().set_user_gender(object.getJSONObject("gender").toString());
                                            PropertyManager.getInstance().set_user_profileimageurl(object.getJSONObject("id").toString());
                                            //PropertyManager.getInstance().set_user_name(object.optString("name"));


                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id, name, email, gender, birthday, picture");
                                request.setParameters(parameters);
                                request.executeAsync();


                                //Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancel() {
                                Log.e("onCancel", "onCancel");
                                Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("onError", "onError " + exception.getLocalizedMessage());
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        ImageView register = (ImageView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}