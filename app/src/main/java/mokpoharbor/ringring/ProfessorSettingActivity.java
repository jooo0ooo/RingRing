package mokpoharbor.ringring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by pingrae on 2017. 10. 8..
 */

public class ProfessorSettingActivity extends AppCompatActivity {
    private String user_image_url;
    Handler handler = new Handler();
    BackPressClose back_pressed;

    @Override
    public void onBackPressed() {
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_professor);
        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        user_image_url = pref.getString("picture_url", "nothing");
        setTitle("SETTING");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ImageView iv = (ImageView) findViewById(R.id.user_image);
                    URL url = new URL(user_image_url);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setImageBitmap(bm);
                        }
                    });
                    iv.setImageBitmap(bm); //비트맵 객체로 보여주기
                } catch (Exception e) {
                }
            }

        });
        t.start();
        back_pressed = new BackPressClose(this);
        ImageView home = (ImageView) findViewById(R.id.home_image_professor);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessorSettingActivity.this, ProfessorMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView personal_setting = (ImageView) findViewById(R.id.personal_setting_professor);
        personal_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessorSettingActivity.this, PersonalSettingProfessorActivity.class);
                startActivity(intent);
            }
        });
        ImageView class_setting_professor = (ImageView) findViewById(R.id.class_setting_professor);
        class_setting_professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessorSettingActivity.this, ClassSettingProfessorActivity.class);
                startActivity(intent);
            }
        });
    }
}
