package mokpoharbor.ringring;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference userRef;
    private static final String PREFS_NAME = "MyPrefs"; //MyPrefs.xml로 저장
    String user_flag;
    private String user_name;
    private String user_id = "user_id";
    private URL user_picture_url;
    private String my_id;
    private Button register_with_facebook_stu;
    private Button register_with_facebook_pro;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        setContentView(R.layout.activity_register);
        callbackManager = CallbackManager.Factory.create();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        setTitle("Register");
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

                                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                try {
                                                    user_name = response.getJSONObject().getString("name").toString();
                                                    user_id = response.getJSONObject().getString("id").toString();
                                                    user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");
                                                    user_flag = "Student";
                                                    my_id = user_id;
                                                    userRef.child(user_id).child("name").setValue(user_name);
                                                    userRef.child(user_id).child("status").setValue("student");
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("user_flag", user_flag);
                                                    editor.putString("my_id", my_id);
                                                    editor.putString("my_name", user_name);
                                                    editor.putString("picture_url", user_picture_url.toString());
                                                    editor.commit();
                                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                    startActivity(i);
                                                    finish();
                                                } catch (Exception e) {
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
                                        Toast.makeText(RegisterActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(FacebookException exception) {
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
                                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                try {
                                                    user_name = response.getJSONObject().getString("name").toString();
                                                    user_id = response.getJSONObject().getString("id").toString();
                                                    user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");
                                                    user_flag = "Professor";
                                                    my_id = user_id;
                                                    userRef.child(user_id).child("name").setValue(user_name);
                                                    userRef.child(user_id).child("status").setValue("professor");
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("user_flag", user_flag);
                                                    editor.putString("my_id", my_id);
                                                    editor.putString("my_name", user_name);
                                                    editor.putString("picture_url", user_picture_url.toString());
                                                    editor.commit();
                                                    Intent i = new Intent(RegisterActivity.this, ProfessorMainActivity.class);
                                                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                    startActivity(i);
                                                    finish();
                                                } catch (Exception e) {
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
                                        Toast.makeText(RegisterActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(FacebookException exception) {
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