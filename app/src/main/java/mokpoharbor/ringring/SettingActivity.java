package mokpoharbor.ringring;

import android.content.Intent;
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
public class SettingActivity extends AppCompatActivity {
    Handler handler = new Handler();
    BackPressClose back_pressed;

    @Override
    public void onBackPressed() {
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("SETTING");

        //사진을 불러오는데 약간의 시간이 걸려서 사진 부분은 Myinfo에서 안가져오고 SharedPreferences를 사용할까 고민중...
        //테스트 해보고 별 차이 없으면 걍 이대로 합니다...
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ImageView iv = (ImageView) findViewById(R.id.user_image);
                    URL url = new URL(MyInfo.user_picture_url);
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
        ImageView home = (ImageView) findViewById(R.id.home_image);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyInfo.user_flag.equals("Student")) {
                    Intent intent = new Intent(SettingActivity.this, StudentMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SettingActivity.this, ProfessorMainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        ImageView personal_setting = (ImageView) findViewById(R.id.personal_setting);
        personal_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, PersonalSettingActivity.class);
                startActivity(intent);

            }
        });
        ImageView class_setting = (ImageView) findViewById(R.id.class_setting);
        class_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyInfo.user_flag.equals("Student")) {
                    Intent intent = new Intent(SettingActivity.this, ClassSettingStudentActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SettingActivity.this, ClassSettingProfessorActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}