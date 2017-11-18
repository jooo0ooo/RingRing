package mokpoharbor.ringring;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import mokpoharbor.ringring.GuideActivity.ProfessorMainGuide;

public class ProfessorMainActivity extends AppCompatActivity {
    ListView mListView = null;
    ListViewAdapter mAdapter = null;
    String my_subject_title;
    //EditText context;
    Button limit_date;
    Button limit_time;
    int year, month, day, hour, minute;
    ArrayList<String> myclass = new ArrayList<>();
    ArrayList<String> homework = new ArrayList<>();
    ArrayList<String> homework_context = new ArrayList<>();
    ArrayList<String> homework_limit = new ArrayList<>();
    String[] my_homework = homework.toArray(new String[homework.size()]);
    String[] my_homework_context = homework_context.toArray(new String[homework_context.size()]);
    String[] my_homework_limit = homework_limit.toArray(new String[homework_limit.size()]);
    FirebaseDatabase database;
    DatabaseReference userRef, classRef;
    BackPressClose back_pressed;

    @Override
    public void onBackPressed() {
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        classRef = database.getReference("class");
        setTitle("HOME");
        back_pressed = new BackPressClose(this);
        ImageView setting = (ImageView) findViewById(R.id.setting_image_professor);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessorMainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView hand_over_subject = (ImageView) findViewById(R.id.hand_over_subject);
        hand_over_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(ProfessorMainActivity.this);
                alertdialog.setTitle("Warning!");
                alertdialog.setMessage("과제를 생성하시면 학생들의 사랑을 독차지 하게 되십니다\n사랑을 독차지 하시겠습니까?");
                alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        recall_dialog();

                    }
                });
                alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alert = alertdialog.create();
                alert.show();
            }
        });
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ProfessorMainActivity.ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        for (int n = 0; n < homework.size(); n++) {
            mAdapter.addItem(my_homework[n] + " : ", my_homework_context[n], my_homework_limit[n]);
        }
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                final String class_name = ((TextView) view.findViewById(R.id.mTitle)).getText().toString();
                final String class_context = ((TextView) view.findViewById(R.id.mText)).getText().toString();
                final String limit_date = ((TextView) view.findViewById(R.id.mDate)).getText().toString();
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfessorMainActivity.this);
                dialog.setTitle(class_name + " - 과제삭제");
                dialog.setMessage(class_context + " - 삭제하시겠습니까?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        int my_index = pref.getInt(class_name+class_context+limit_date, -1);
                        if(my_index == -1){
                            Toast.makeText(ProfessorMainActivity.this, "Fail to load my_index", Toast.LENGTH_SHORT).show();
                        }else{
                            new MyAlarm(getApplicationContext(), "","","","","",my_index).Cancel();
                            Toast.makeText(ProfessorMainActivity.this, "load_index:"+my_index, Toast.LENGTH_SHORT).show();

                        }
                        Toast.makeText(ProfessorMainActivity.this, class_name + ", " + class_context, Toast.LENGTH_SHORT).show();
                        classRef.child(class_name).child("Homework").child(class_context).removeValue();
                        userRef.child(MyInfo.my_id).child("my_class").child(class_name).child("Homework").child(class_context).removeValue();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myclass.clear();
                homework.clear();
                homework_context.clear();
                homework_limit.clear();
                for (DataSnapshot snapshot : dataSnapshot.child(MyInfo.my_id).child("my_class").getChildren()) {
                    String key = snapshot.getKey();
                    myclass.add(key);
                    if (snapshot.child("Homework").hasChildren()) {
                        for (DataSnapshot snapshot_child : snapshot.child("Homework").getChildren()) {
                            String title = snapshot_child.getRef().getParent().getParent().getKey();
                            String text = snapshot_child.getKey();
                            String date = snapshot_child.getValue().toString();
                            homework.add(title);
                            homework_context.add(text);
                            homework_limit.add(date);
                        }
                        my_homework = homework.toArray(new String[homework.size()]);
                        my_homework_context = homework_context.toArray(new String[homework_context.size()]);
                        my_homework_limit = homework_limit.toArray(new String[homework_limit.size()]);
                        mListView = (ListView) findViewById(R.id.listView);
                        mAdapter = new ProfessorMainActivity.ListViewAdapter(ProfessorMainActivity.this);
                        mListView.setAdapter(mAdapter);
                        for (int n = 0; n < homework.size(); n++) {
                            mAdapter.addItem(my_homework[n], my_homework_context[n], my_homework_limit[n]);
                        }

                        for (DataSnapshot snapshot_child : snapshot.child("Homework").getChildren()) {
                            SimpleDateFormat year_formatter = new SimpleDateFormat ("yyyy", Locale.KOREA);
                            SimpleDateFormat month_formatter = new SimpleDateFormat ("MM", Locale.KOREA);
                            SimpleDateFormat date_formatter = new SimpleDateFormat ("dd", Locale.KOREA);
                            SimpleDateFormat hour_formatter = new SimpleDateFormat ("HH", Locale.KOREA);
                            SimpleDateFormat minute_formatter = new SimpleDateFormat ("mm", Locale.KOREA);
                            Date currentTime = new Date();

                            int year_now = Integer.parseInt(year_formatter.format(currentTime));
                            int month_now = Integer.parseInt(month_formatter.format(currentTime));
                            int date_now = Integer.parseInt(date_formatter.format(currentTime));
                            int hour_now = Integer.parseInt(hour_formatter.format(currentTime));
                            int minute_now = Integer.parseInt(minute_formatter.format(currentTime));

                            String title = snapshot_child.getRef().getParent().getParent().getKey();
                            String text = snapshot_child.getKey();
                            String date = snapshot_child.getValue().toString();

                            int homework_year = -1;
                            int homework_month = -1;
                            int homework_date = -1;
                            int homework_hour = -1;
                            int homework_minute = -1;

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            SimpleDateFormat year_only_format = new SimpleDateFormat("yyyy");
                            SimpleDateFormat month_only_format = new SimpleDateFormat("MM");
                            SimpleDateFormat date_only_format = new SimpleDateFormat("dd");
                            SimpleDateFormat hour_only_format = new SimpleDateFormat("HH");
                            SimpleDateFormat minute_only_format = new SimpleDateFormat("mm");

                            Date date_detail = null;
                            try {
                                date_detail = format.parse(date);
                                homework_year = Integer.parseInt(year_only_format.format(date_detail));
                                homework_month = Integer.parseInt(month_only_format.format(date_detail));
                                homework_date = Integer.parseInt(date_only_format.format(date_detail));
                                homework_hour = Integer.parseInt(hour_only_format.format(date_detail));
                                homework_minute = Integer.parseInt(minute_only_format.format(date_detail));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int reverse_To_minute_now = (date_now * 60 * 24) + (hour_now * 60) + minute_now;
                            int reverse_To_minute_homework = (homework_date * 60 * 24) + (homework_hour * 60) + homework_minute;

                            if(year_now > homework_year){
                                classRef.child(title).child("Homework").child(text).removeValue();
                                userRef.child(MyInfo.my_id).child("my_class").child(title).child("Homework").child(text).removeValue();
                            }else if(year_now == homework_year){
                                if(month_now > homework_month){
                                    classRef.child(title).child("Homework").child(text).removeValue();
                                    userRef.child(MyInfo.my_id).child("my_class").child(title).child("Homework").child(text).removeValue();
                                }else if(month_now == homework_month){
                                    if( (reverse_To_minute_now - reverse_To_minute_homework) >= 1440 ){
                                        classRef.child(title).child("Homework").child(text).removeValue();
                                        userRef.child(MyInfo.my_id).child("my_class").child(title).child("Homework").child(text).removeValue();
                                    }
                                }
                            }

                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessorMainActivity.this, ProfessorMainGuide.class);
                startActivity(intent);
            }
        });

    }

    public class MyAlarm {
        private Context context;

        private String year_only;
        private String month_only;
        private String date_only;
        private String hour_only;
        private String minute_only;
        private int index;

        public MyAlarm(Context context, String year_only, String month_only, String date_only, String hour_only, String minute_only, int index) {
            this.context = context;
            this.year_only = year_only;
            this.month_only = month_only;
            this.date_only = date_only;
            this.hour_only = hour_only;
            this.minute_only = minute_only;
            this.index = index;
        }

        public void Alarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ProfessorMainActivity.this, BroadcastClassForProfessor.class);
            PendingIntent sender = PendingIntent.getBroadcast(ProfessorMainActivity.this, index, intent, 0);
            Calendar calendar = Calendar.getInstance();
            int homework_year = Integer.parseInt(year_only);
            int homework_month = Integer.parseInt(month_only);
            int homework_date = Integer.parseInt(date_only);
            int homework_hour = Integer.parseInt(hour_only);
            int homework_minute = Integer.parseInt(minute_only);

            calendar.set(homework_year, homework_month-1, homework_date, homework_hour, homework_minute, 0);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }

        public void Cancel() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ProfessorMainActivity.this, BroadcastClassForProfessor.class);
            PendingIntent sender = PendingIntent.getBroadcast(ProfessorMainActivity.this, index, intent, 0);
            am.cancel(sender);
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);
            limit_date.setText(date);
        }

    };
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = String.format("%d / %d", hourOfDay, minute);
            limit_time.setText(time);
        }

    };

    public void recall_dialog() {
        final String[] my_subject = myclass.toArray(new String[myclass.size()]);
        final EditText context = new EditText(ProfessorMainActivity.this);
        limit_date = new Button(ProfessorMainActivity.this);
        limit_time = new Button(ProfessorMainActivity.this);
        AlertDialog.Builder make_subject = new AlertDialog.Builder(ProfessorMainActivity.this);
        make_subject.setTitle("과목을 선택하세용");
        make_subject.setItems(my_subject, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog.Builder homework_context = new AlertDialog.Builder(ProfessorMainActivity.this);
                my_subject_title = my_subject[which];
                homework_context.setTitle(my_subject[which] + " 과제");
                LinearLayout ll = new LinearLayout(ProfessorMainActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                context.setHint("과제 내용을 입력하세요");
                ll.addView(context);
                ll.addView(limit_date);
                ll.addView(limit_time);
                homework_context.setView(ll);
                limit_date.setHint("클릭하여 마감 날짜를 설정하세요");
                limit_time.setHint("클릭하여 마감 시간를 설정하세요");
                limit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(ProfessorMainActivity.this, dateSetListener, year, month, day).show();
                    }
                });
                limit_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new TimePickerDialog(ProfessorMainActivity.this, timeSetListener, hour, minute, false).show();
                    }
                });
                homework_context.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String homework = context.getText().toString();
                        String date = limit_date.getText().toString();
                        String time = limit_time.getText().toString();
                        String limit = date + " / " + time;
                        String limit_new_format = null;
                        SimpleDateFormat original_format = new SimpleDateFormat("yyyy / MM / dd / HH / mm");
                        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        /*Detail Date For Alarm*/
                        String year_only = null;
                        String month_only = null;
                        String date_only = null;
                        String hour_only = null;
                        String minute_only = null;
                        SimpleDateFormat year_only_format = new SimpleDateFormat("yyyy");
                        SimpleDateFormat month_only_format = new SimpleDateFormat("MM");
                        SimpleDateFormat date_only_format = new SimpleDateFormat("dd");
                        SimpleDateFormat hour_only_format = new SimpleDateFormat("HH");
                        SimpleDateFormat minute_only_format = new SimpleDateFormat("mm");
                        try {
                            Date date_detail = original_format.parse(limit);
                            year_only = year_only_format.format(date_detail);
                            month_only = month_only_format.format(date_detail);
                            date_only = date_only_format.format(date_detail);
                            hour_only = hour_only_format.format(date_detail);
                            minute_only = minute_only_format.format(date_detail);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            Date date_test = original_format.parse(limit);
                            limit_new_format = new_format.format(date_test);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (homework.isEmpty() || date.isEmpty() || time.isEmpty()) {
                            Toast.makeText(ProfessorMainActivity.this, "위 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                            recall_dialog();
                        } else {
                            classRef.child(my_subject_title).child("Homework").child(homework).setValue(limit_new_format);
                            userRef.child(MyInfo.my_id).child("my_class").child(my_subject_title).child("Homework").child(homework).setValue(limit_new_format);

                            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            //editor.putString(my_subject_title+homework+limit_new_format, my_subject_title+homework+limit_new_format);
                            int my_index = pref.getInt("index", 1);
                            Toast.makeText(ProfessorMainActivity.this, "my_index:"+my_index, Toast.LENGTH_SHORT).show();
                            editor.putInt(my_subject_title+homework+limit_new_format, my_index);
                            new MyAlarm(getApplicationContext(), year_only, month_only, date_only, hour_only, minute_only, my_index).Alarm();
                            ++my_index;
                            editor.putInt("index",my_index);
                            editor.commit();
                            dialog.cancel();
                            Toast.makeText(ProfessorMainActivity.this, "과제 등록 완료", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
                homework_context.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                homework_context.show();
            }
        });
        make_subject.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        make_subject.show();
    }

    private class ViewHolder {
        public TextView mTitle;
        public TextView mText;
        public TextView mDate;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public void addItem(String mTitle, String mText, String mDate) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mTitle;
            addInfo.mText = mText;
            addInfo.mDate = mDate;
            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProfessorMainActivity.ViewHolder holder;
            if (convertView == null) {
                holder = new ProfessorMainActivity.ViewHolder();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.myrow, null);
                holder.mTitle = (TextView) convertView.findViewById(R.id.mTitle);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);
                convertView.setTag(holder);
            } else {
                holder = (ProfessorMainActivity.ViewHolder) convertView.getTag();
            }
            ListData mData = mListData.get(position);
            holder.mTitle.setText(mData.mTitle);
            holder.mText.setText(mData.mText);
            holder.mDate.setText(mData.mDate);
            return convertView;
        }
    }
}