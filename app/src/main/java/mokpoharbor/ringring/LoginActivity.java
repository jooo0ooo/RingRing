package mokpoharbor.ringring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private String user_name;
    private String user_id = "user_id";
    private URL user_picture_url;
    private ImageView login_with_facebook;
    private CallbackManager callbackManager;
    String user_flag;

    FirebaseDatabase database;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");

        callbackManager = CallbackManager.Factory.create();
        login_with_facebook = (ImageView) findViewById(R.id.login_with_facebook);
        login_with_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    user_name = response.getJSONObject().getString("name").toString();
                                    user_id = response.getJSONObject().getString("id").toString();
                                    user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");

                                    if(userRef.child(user_id).getKey().equals(user_id)){
                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.child(user_id).getChildren()){
                                                    if(snapshot.getKey().equals("status")){
                                                        String status = snapshot.getValue().toString();
                                                        if(status.equals("professor")){
                                                            user_flag = "Professor";
                                                        }else if(status.equals("student")){
                                                            user_flag = "Student";
                                                        }
                                                        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = pref.edit();
                                                        editor.putString("user_flag", user_flag);
                                                        editor.putString("my_id", user_id);
                                                        editor.putString("my_name", user_name);
                                                        editor.putString("picture_url", user_picture_url.toString());
                                                        editor.commit();

                                                        MyInfo.my_name = user_name;
                                                        MyInfo.my_id = user_id;
                                                        MyInfo.user_picture_url = user_picture_url.toString();
                                                        MyInfo.user_flag = user_flag;

                                                        if (user_flag.equals("Student")) {
                                                            Intent i = new Intent(LoginActivity.this, StudentMainActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        } else if (user_flag.equals("Professor")) {
                                                            Intent i = new Intent(LoginActivity.this, ProfessorMainActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "Error or Not_User", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다.\n회원가입을 해주세요",Toast.LENGTH_LONG).show();
                                    }

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
                        Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ImageView register = (ImageView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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