package mokpoharbor.ringring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by pingrae on 2017. 10. 20..
 */

public class ClassSettingProfessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_setting_professor);

        //액티비티 타이틀바 내용 설정
        setTitle("Class Setting");

        ImageView urgent_notice = (ImageView) findViewById(R.id.urgent_notice);
        urgent_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassSettingProfessorActivity.this, "urgent notice - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });


        Button my_class = (Button) findViewById(R.id.my_class);
        my_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassSettingProfessorActivity.this, "내 강좌 보기 - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });

        Button register_class = (Button) findViewById(R.id.register_class);
        register_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassSettingProfessorActivity.this, "강좌 등록하기 - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });
    }
}