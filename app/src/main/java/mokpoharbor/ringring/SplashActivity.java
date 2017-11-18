package mokpoharbor.ringring;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by pingrae on 2017. 10. 17..
 */

public class SplashActivity extends Activity {
    String user_name;
    String user_id;
    String user_picture_url;
    String user_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(1000);
            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            user_flag = pref.getString("user_flag", "nothing");
            if(user_flag.equals("Student")){

                user_id = pref.getString("my_id", "nothing");
                user_name = pref.getString("my_name", "nothing");
                user_picture_url = pref.getString("picture_url", "nothing");

                MyInfo.my_name = user_name;
                MyInfo.my_id = user_id;
                MyInfo.user_flag = user_flag;
                MyInfo.user_picture_url = user_picture_url;

                Intent i = new Intent(SplashActivity.this, StudentMainActivity.class);
                startActivity(i);
                finish();

            }else if(user_flag.equals("Professor")){

                user_id = pref.getString("my_id", "nothing");
                user_name = pref.getString("my_name", "nothing");
                user_picture_url = pref.getString("picture_url", "nothing");

                MyInfo.my_name = user_name;
                MyInfo.my_id = user_id;
                MyInfo.user_flag = user_flag;
                MyInfo.user_picture_url = user_picture_url;

                Intent i = new Intent(SplashActivity.this, ProfessorMainActivity.class);
                startActivity(i);
                finish();

            }else{
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}