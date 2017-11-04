package mokpoharbor.ringring;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by pingrae on 2017. 10. 20..
 */
public class ClassSettingStudentActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference classRef, userRef;
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
                new MyAlarm(getApplicationContext()).Alarm();
                if (!isChecked) {
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(ClassSettingStudentActivity.this);
                    alertdialog.setTitle("Warning!");
                    alertdialog.setMessage("과제를 알림 받지 않으시면 학업과 멀어질 수 있습니다.\nYOLO족이 되시겠습니까?");
                    alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ClassSettingStudentActivity.this, "YOLO! 술ㄱ!", Toast.LENGTH_SHORT).show();
                            get_alram.setChecked(false);
                            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("alram", false);
                            editor.commit();
                            new MyAlarm(getApplicationContext()).Cancel();
                        }
                    });
                    alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                                AlertDialog.Builder remove = new AlertDialog.Builder(ClassSettingStudentActivity.this);
                                remove.setTitle("Delete Class");
                                remove.setMessage("선택한 과목을 삭제하시겠습니까?");
                                remove.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        userRef.child(my_id).child("my_class").child(selected).removeValue();
                                        classRef.child(selected).child("Student").child(my_id).removeValue();
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
                                return true;
                            }
                        });
                    }
                });
                ad.show();
            }

        });
        Button register_class = (Button) findViewById(R.id.register_class);
        register_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassSettingStudentActivity.this, StudentRegistClass.class);
                startActivity(intent);
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myclass.clear();
                for (DataSnapshot snapshot : dataSnapshot.child(my_id).child("my_class").getChildren()) {
                    String key = snapshot.getKey();
                    myclass.add(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        if (pref.getBoolean("alram", false)) {
            new MyAlarm(getApplicationContext()).Alarm();
        }
    }

    public class MyAlarm {
        private Context context;

        public MyAlarm(Context context) {
            this.context = context;
        }

        public void Alarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ClassSettingStudentActivity.this, BroadcastClass.class);
            PendingIntent sender = PendingIntent.getBroadcast(ClassSettingStudentActivity.this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String hour = pref.getString("alram_hour", "nothing");
            String minute = pref.getString("alram_minute", "nothing");
            long my_hour = Long.parseLong(hour) * 60 * 60 * 1000;
            long my_minute = Long.parseLong(minute) * 60 * 1000;
            long my_period = my_hour + my_minute;
            if (!hour.isEmpty() && !minute.isEmpty()) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), my_period, sender);
            }
        }

        public void Cancel() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ClassSettingStudentActivity.this, BroadcastClass.class);
            PendingIntent sender = PendingIntent.getBroadcast(ClassSettingStudentActivity.this, 0, intent, 0);
            am.cancel(sender);
        }
    }
}