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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    //페이스북으로부터 받을 정보를 저장할 변수
    private String user_name;
    private String user_id = "user_id";
    private URL user_picture_url;

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

                                            //페이스북에게 받은 객체에서 원하는 정보 뽑아내기
                                            user_name = response.getJSONObject().getString("name").toString();
                                            user_id = response.getJSONObject().getString("id").toString();
                                            user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");

                                            File file = new File("/data/data/mokpoharbor.ringring/cache/user_flag.txt") ;
                                            FileReader fr = null ;
                                            BufferedReader bufrd = null ;

                                            String s = null;

                                            try {
                                                // open file.
                                                fr = new FileReader(file) ;
                                                bufrd = new BufferedReader(fr) ;

                                                while ((s=bufrd.readLine()) != null) {

                                                }

                                                // close file.
                                                bufrd.close() ;
                                                fr.close() ;
                                            } catch (Exception e) {
                                                e.printStackTrace() ;
                                            }

                                            if(s == "Student"){
                                                Intent i = new Intent(LoginActivity.this, MainActivity.class);

                                                //인텐트 할때 얻은 정보도 같이 넘겨주기
                                                i.putExtra("name", user_name);
                                                i.putExtra("id", user_id);
                                                i.putExtra("image_url", user_picture_url.toString());

                                                startActivity(i);
                                                finish();
                                            }else{
                                                Intent i = new Intent(LoginActivity.this, ProfessorMainActivity.class);

                                                //인텐트 할때 얻은 정보도 같이 넘겨주기
                                                i.putExtra("name", user_name);
                                                i.putExtra("id", user_id);
                                                i.putExtra("image_url", user_picture_url.toString());

                                                startActivity(i);
                                                finish();
                                            }

                                            /*
                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);

                                            //인텐트 할때 얻은 정보도 같이 넘겨주기
                                            i.putExtra("name", user_name);
                                            i.putExtra("id", user_id);
                                            i.putExtra("image_url", user_picture_url.toString());

                                            startActivity(i);
                                            finish();
                                            */

                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                Bundle parameters = new Bundle();
                                parameters.putString("user profi", "id, name, email, gender, birthday, picture");
                                request.setParameters(parameters);
                                request.executeAsync();

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