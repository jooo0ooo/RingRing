package mokpoharbor.ringring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ProfessorMainActivity extends AppCompatActivity {

    BackPressClose back_pressed;

    //오버롸이드~
    @Override
    public void onBackPressed(){
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);

        //액티비티 타이틀바 내용 설정
        setTitle("HOME");

        //뒤로가기 버튼 눌를시 토스트메세지로 확인 메세지를 뛰어준다
        back_pressed = new BackPressClose(this);


        //setting 이미지 아이콘을 터치할때 화면전환 되는 부분
        ImageView setting = (ImageView)findViewById(R.id.setting_image_professor);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProfessorMainActivity.this, ProfessorSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
