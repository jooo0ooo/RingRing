package mokpoharbor.ringring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //액티비티 타이틀바 내용 설정
        setTitle("Register");

        //내가 잘 선택했나 이미지 클릭시 이미지를 바꿔주는 효과를 주었습니다.
        final Button student = (Button)findViewById(R.id.register_student);
        student.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN :
                        student.setBackgroundResource(R.drawable.student_touch);
                    case MotionEvent.ACTION_UP :
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }
                return true;
            }
        });


        final Button professor = (Button)findViewById(R.id.register_professor);
        professor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN :
                        student.setBackgroundResource(R.drawable.professor_touch);
                    case MotionEvent.ACTION_UP :
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }
                return true;
            }
        });

    }

}
