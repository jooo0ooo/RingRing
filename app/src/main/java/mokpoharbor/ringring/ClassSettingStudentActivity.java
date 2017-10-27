package mokpoharbor.ringring;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by pingrae on 2017. 10. 20..
 */

public class ClassSettingStudentActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef, classRef, userRef;

    String my_id;

    ArrayList<String> myclass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_setting);

        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        my_id = pref.getString("my_id", "nothing");

        database = FirebaseDatabase.getInstance();

        classRef = database.getReference("class");
        userRef = database.getReference("user");

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

        TextView alram_cycle = (TextView) findViewById(R.id.arlam_cycle);

        String alram_hour = pref.getString("alram_hour", "nothing");
        String alram_minute = pref.getString("alram_minute", "nothing");

        if (!alram_hour.equals("nothing")) {

            alram_cycle.setText(alram_hour + "시간 " + alram_minute + "분");

        }


        final Switch get_alram = (Switch) findViewById(R.id.get_alram);

        get_alram.setChecked(pref.getBoolean("alram", false));

        get_alram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (!isChecked) {
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(ClassSettingStudentActivity.this);
                    //다이얼로그의 내용을 설정합니다.
                    alertdialog.setTitle("Warning!");
                    alertdialog.setMessage("과제를 알림 받지 않으시면 학업과 멀어질 수 있습니다.\nYOLO족이 되시겠습니까?");

                    //확인 버튼
                    alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //확인 버튼이 눌렸을 때 토스트를 띄워줍니다.
                            Toast.makeText(ClassSettingStudentActivity.this, "YOLO! 술ㄱ!", Toast.LENGTH_SHORT).show();
                            get_alram.setChecked(false);

                            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean("alram", false);

                            editor.commit();
                        }
                    });

                    //취소 버튼
                    alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //취소 버튼이 눌렸을 때 토스트를 띄워줍니다.
                            Toast.makeText(ClassSettingStudentActivity.this, "현명한 선택~", Toast.LENGTH_SHORT).show();
                            get_alram.setChecked(true);
                        }
                    });

                    AlertDialog alert = alertdialog.create();
                    alert.show();

                } else {
                    editor.putBoolean("alram", true);
                    editor.commit();
                }
            }
        });

        Button my_class = (Button) findViewById(R.id.my_class);
        my_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] test = myclass.toArray(new String[myclass.size()]);


                AlertDialog.Builder builder = new AlertDialog.Builder(ClassSettingStudentActivity.this);
                builder.setTitle("My Class");
                builder.setItems(test, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ClassSettingStudentActivity.this, test[which], Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog ad = builder.create();
                ad.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        final ListView lv = ad.getListView();
                        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                final String selected = (lv.getItemAtPosition(position)).toString();
                                //myRef.child(selected).removeValue();
                                AlertDialog.Builder remove = new AlertDialog.Builder(ClassSettingStudentActivity.this);

                                remove.setTitle("Delete Class");
                                remove.setMessage("선택한 과목을 삭제하시겠습니까?");

                                remove.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        userRef.child(my_id).child("my_class").child(selected).removeValue();
                                        ad.cancel();
                                        Toast.makeText(ClassSettingStudentActivity.this, selected + "가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                remove.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                remove.show();
                                //userRef.child(my_id).child("my_class").child(selected).removeValue();
                                return true;
                            }
                        });
                    }
                });

                //builder.create();
                //builder.show();
                ad.show();
            }

        });


        /*
        my_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassSettingStudentActivity.this, "수강 강좌 보기 - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });
        */

        Button register_class = (Button) findViewById(R.id.register_class);
        register_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassSettingStudentActivity.this, StudentRegistClass.class);
                startActivity(intent);
                //Toast.makeText(ClassSettingStudentActivity.this, "강좌 등록하기 - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

