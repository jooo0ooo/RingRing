package mokpoharbor.ringring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

/**
 * Created by pingrae on 2017. 10. 19..
 */
public class PersonalSettingStudentActivity extends AppCompatActivity {
    private String user_name;
    private String my_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_student);
        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String user_flag = pref.getString("user_flag", "nothing");
        my_id = pref.getString("my_id", "nothing");
        user_name = pref.getString("my_name", "nothing");
        setTitle("Personal Setting");
        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(user_name);
        TextView id = (TextView) findViewById(R.id.user_id);
        id.setText(my_id);
        TextView status = (TextView) findViewById(R.id.user_status);
        status.setText(user_flag);
        ImageView logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectFromFacebook();
                Intent intent = new Intent(PersonalSettingStudentActivity.this, LoginActivity.class);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }
}