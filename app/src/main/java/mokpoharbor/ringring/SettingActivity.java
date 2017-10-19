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

    private String user_name;
    private String user_id;
    private String user_image_url;

    Handler handler = new Handler();

    BackPressClose back_pressed;

    //오버롸이드~
    @Override
    public void onBackPressed(){
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //액티비티 타이틀바 내용 설정
        setTitle("SETTING");


        Bundle i = getIntent().getExtras();

        user_image_url = i.getString("image_url");
        user_name = i.getString("name");
        user_id = i.getString("id");

        //Thread에서 웹의 이미지를 받아온다.
        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final ImageView iv = (ImageView)findViewById(R.id.user_image);
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
                } catch(Exception e){

                }

            }

        });

        t.start();


        //뒤로가기 버튼 눌를시 토스트메세지로 확인 메세지를 뛰어준다
        back_pressed = new BackPressClose(this);

        //setting 이미지 아이콘을 터치할때 화면전환 되는 부분
        ImageView home = (ImageView)findViewById(R.id.home_image);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);

                intent.putExtra("name", user_name);
                intent.putExtra("id", user_id);
                intent.putExtra("image_url", user_image_url);

                startActivity(intent);
                finish();
            }
        });

        ImageView personal_setting = (ImageView)findViewById(R.id.personal_setting);
        personal_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, PersonalSettingStudentActivity.class);

                intent.putExtra("name", user_name);
                intent.putExtra("id", user_id);

                startActivity(intent);
            }
        });

    }
}
