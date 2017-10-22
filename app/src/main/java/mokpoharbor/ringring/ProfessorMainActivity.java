package mokpoharbor.ringring;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfessorMainActivity extends AppCompatActivity {

    private String user_name;
    private String user_id;
    private String user_image_url;

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

        Bundle i = getIntent().getExtras();

        user_name = i.getString("name");
        user_image_url = i.getString("image_url");
        user_id = i.getString("id");

        /*
        //정보 잘 가져오나 테스트
        Toast.makeText(ProfessorMainActivity.this, "사용자 id->" + user_id, Toast.LENGTH_SHORT).show();
        Toast.makeText(ProfessorMainActivity.this, "사용자 이름->" + user_name, Toast.LENGTH_SHORT).show();
        Toast.makeText(ProfessorMainActivity.this, "프필 URL->" + user_image_url, Toast.LENGTH_SHORT).show();
        */

        //뒤로가기 버튼 눌를시 토스트메세지로 확인 메세지를 뛰어준다
        back_pressed = new BackPressClose(this);


        //setting 이미지 아이콘을 터치할때 화면전환 되는 부분
        ImageView setting = (ImageView)findViewById(R.id.setting_image_professor);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProfessorMainActivity.this, ProfessorSettingActivity.class);

                intent.putExtra("name", user_name);
                intent.putExtra("id", user_id);
                intent.putExtra("image_url", user_image_url);

                startActivity(intent);
                finish();
            }
        });

        ImageView hand_over_subject = (ImageView)findViewById(R.id.hand_over_subject);
        hand_over_subject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(ProfessorMainActivity.this);
                //다이얼로그의 내용을 설정합니다.
                alertdialog.setTitle("Warning!");
                alertdialog.setMessage("과제를 생성하시면 학생들에게 미움받을 수 있습니다.\n미움받을 용기 있으십니까?");

                //확인 버튼
                alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //확인 버튼이 눌렸을 때 토스트를 띄워줍니다.
                        Toast.makeText(ProfessorMainActivity.this, "구현 준비 중 입니다.", Toast.LENGTH_SHORT).show();

                    }
                });

                //취소 버튼
                alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //취소 버튼이 눌렸을 때 토스트를 띄워줍니다.
                        Toast.makeText(ProfessorMainActivity.this, "구현 준비 중 입니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alert = alertdialog.create();
                alert.show();


            }
        });


    }
}
