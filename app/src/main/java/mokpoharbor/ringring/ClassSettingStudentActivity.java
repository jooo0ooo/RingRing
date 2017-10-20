package mokpoharbor.ringring;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        Button set_alram_cycle = (Button) findViewById(R.id.set_alram_cycle);
        set_alram_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAlramCycleFragment set_alram_fragment = new SetAlramCycleFragment();
                set_alram_fragment.show(getFragmentManager(), "TimePicker");
            }
        });

        TextView alram_cycle = (TextView)findViewById(R.id.arlam_cycle);

        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String alram_hour = pref.getString("alram_hour", "nothing");
        String alram_minute = pref.getString("alram_minute", "nothing");

        if(!alram_hour.equals("nothing")){

            alram_cycle.setText(alram_hour + "시간 " + alram_minute + "분");

        }
    }
}

