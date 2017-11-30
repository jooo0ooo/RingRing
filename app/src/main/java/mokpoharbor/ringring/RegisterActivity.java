package mokpoharbor.ringring;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs"; //MyPrefs.xml로 저장
    private String user_name, user_id = "user_id", my_id;
    private URL user_picture_url;
    private Button register_with_facebook_stu, register_with_facebook_pro;
    private CallbackManager callbackManager;
    String user_flag, checking = "null";
    FirebaseDatabase database;
    DatabaseReference userRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
                                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                try {
                                                    user_name = response.getJSONObject().getString("name").toString();
                                                    user_id = response.getJSONObject().getString("id").toString();
                                                    user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");

                                                    isStudentUser();

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

                                                    isProfessorUser();

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

    public void isStudentUser() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(user_id)) {
                        checking = "check";
                        Toast.makeText(RegisterActivity.this, "이미 회원가입을 하셨습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        startActivity(intent);
                    }
                }
                if (checking.equals("null")) {
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

                    MyInfo.my_name = user_name;
                    MyInfo.my_id = user_id;
                    MyInfo.user_flag = user_flag;
                    MyInfo.user_picture_url = user_picture_url.toString();

                    Intent i = new Intent(RegisterActivity.this, StudentMainActivity.class);
                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void isProfessorUser() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(user_id)) {
                        checking = "check";
                        Toast.makeText(RegisterActivity.this, "이미 회원가입을 하셨습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        startActivity(intent);
                    }
                }
                if (checking.equals("null")) {
                    final EditText etEdit = new EditText(RegisterActivity.this);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    dialog.setTitle("교수님 인증키를 입력하세요");
                    dialog.setView(etEdit);
                    dialog.setPositiveButton("Regist", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String inputValue = etEdit.getText().toString();
                            if (inputValue.equals("mokpo")) {
                                user_flag = "Professor";
                                my_id = user_id;
                                userRef.child(user_id).child("name").setValue(user_name);
                                userRef.child(user_id).child("status").setValue("professor");

                                SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("isProfessor", true);
                                editor.putString("user_flag", user_flag);
                                editor.putString("my_id", my_id);
                                editor.putString("my_name", user_name);
                                editor.putString("picture_url", user_picture_url.toString());
                                editor.commit();

                                MyInfo.my_name = user_name;
                                MyInfo.my_id = user_id;
                                MyInfo.user_flag = user_flag;
                                MyInfo.user_picture_url = user_picture_url.toString();

                                Intent i = new Intent(RegisterActivity.this, ProfessorMainActivity.class);
                                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "인증키가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}