package mokpoharbor.ringring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    BackPressClose back_pressed;

    //list 테스트 해보는 겁니당.
    String [] data = { "☞ 콜로키움 : 감상문 작성 : 1시간",
            "☞ 알고리즘 : 보고서 작성 : 2시간",
            "☞ 영어2 : 영어단어 암기 : 3시간",
            "☞ 고급객체 : 유투브 시청 : 4시간",
            "☞ 공개SW실무 : 우분투 설치 : 5시간",
            "☞ 교양골프 : 연습하기 : 6시간",
            "☞ 팀프로젝트 : 치맥하기 : 7시간",
            "☞ 직무역량어쩌고 : 출튀하기 : 8시간",
            "☞ 피온3 : 접속보상 받기 : 9시간",
            "☞ 집 : 화장실청소하기 : 10시간"};

    //오버롸이드~
    @Override
    public void onBackPressed(){
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액티비티 타이틀바 내용 설정
        setTitle("HOME");

        //뒤로가기 버튼 눌를시 토스트메세지로 확인 메세지를 뛰어준다
        back_pressed = new BackPressClose(this);

        //리스트뷰를 이용하기위해 어댑터 사용
        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(),
                R.layout.myrow,
                data);

        ListView lv = (ListView)findViewById(R.id.test_list);
        lv.setAdapter(adapter);

        //setting 이미지 아이콘을 터치할때 화면전환 되는 부분
        ImageView setting = (ImageView)findViewById(R.id.setting_image);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
