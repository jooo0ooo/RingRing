package mokpoharbor.ringring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by pingrae on 2017. 10. 8..
 */

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //setting 이미지 아이콘을 터치할때 화면전환 되는 부분
        ImageView setting = (ImageView)findViewById(R.id.home_image);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
