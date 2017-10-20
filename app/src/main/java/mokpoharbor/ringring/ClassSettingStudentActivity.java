package mokpoharbor.ringring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pingrae on 2017. 10. 20..
 */

public class ClassSettingStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_setting);

        //액티비티 타이틀바 내용 설정
        setTitle("Class Setting");
    }
}

