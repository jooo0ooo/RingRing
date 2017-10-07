package mokpoharbor.ringring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    //list 테스트 해보는 겁니당.
    String [] data = { "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf",
            "asdf : asdf : asdf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            }
        });
    }
}
