package mokpoharbor.ringring;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> myclass = new ArrayList<>();
    ArrayList<String> homework = new ArrayList<>();
    ArrayList<String> homework_context = new ArrayList<>();
    ArrayList<String> homework_limit = new ArrayList<>();

    String[] my_homework = homework.toArray(new String[homework.size()]);
    String[] my_homework_context = homework_context.toArray(new String[homework_context.size()]);
    String[] my_homework_limit = homework_limit.toArray(new String[homework_limit.size()]);


    FirebaseDatabase database;
    DatabaseReference userRef, classRef;

    String my_id;

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    private String user_name;
    private String user_id;
    private String user_image_url;


    BackPressClose back_pressed;

    //list 테스트 해보는 겁니당.
    /*
    String [] data = { "☞ 콜로키움 : 감상문 작성 : 1시간",
            "☞ 알고리즘 : 보고서 작성 : 2시간",
            "☞ 영어2 : 영어단어 암기 : 3시간",
            "☞ 고급객체 : 유투브 시청 : 4시간",
            "☞ 공개SW실무 : 우분투 설치 : 5시간",
            "☞ 교양골프 : 연습하기 : 6시간",
            "☞ 팀프로젝트 : 치맥하기 : 7시간",
            "☞ 직무역량어쩌고 : 출튀하기 : 8시간",
            "☞ 나는야 : 목포 사나이 : 정지우",
            "☞ 집 : 화장실청소하기 : 10시간",
            "☞ 정지우 : 정말 멋진 팀장ㅎ : 최고에요!",
            "☞ 데이트 : 여소점 : 올해는 제발ㅎ",
            "☞ 과제는 : 언제나 : 극혐",
            "☞ 알코올 : 은 싫지만 주면 : 마실 수 밖에",
            "☞ 테스트 : 목록 : 넣는 중",
            "☞ 더이상 : 할게 : 없어요"};
    */

    //오버롸이드~
    @Override
    public void onBackPressed(){
        back_pressed.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        my_id = pref.getString("my_id", "nothing");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        classRef = database.getReference("class");

        //액티비티 타이틀바 내용 설정
        setTitle("HOME");

        Bundle i = getIntent().getExtras();

        user_name = i.getString("name");
        user_image_url = i.getString("image_url");
        user_id = i.getString("id");

        /*
        //정보 잘 가져오나 테스트
        Toast.makeText(MainActivity.this, "사용자 id->" + user_id, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "사용자 이름->" + user_name, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "프필 URL->" + user_image_url, Toast.LENGTH_SHORT).show();
        */

        //뒤로가기 버튼 눌를시 토스트메세지로 확인 메세지를 뛰어준다
        back_pressed = new BackPressClose(this);

        /*

        //리스트뷰를 이용하기위해 어댑터 사용
        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(),
                R.layout.myrow,
                data);

        ListView lv = (ListView)findViewById(R.id.homework_list);
        lv.setAdapter(adapter);

        */

/*
        mListView = (ListView) findViewById(R.id.homework_list);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        mAdapter.addItem("팀프로젝트 : ",
                "시나리오 작성",
                "2017-10-18");
        mAdapter.addItem("교양골프 : ",
                "7번 아이언 연습",
                "2017-10-19");
        mAdapter.addItem("정지우 : ",
                "목포의 사나이",
                "2017-10-20");
        mAdapter.addItem("올해안에 : ",
                "여자친구 생기게 해주세요",
                "2017-10-21");
        mAdapter.addItem("정팀장 : ",
                "최고의 조장님!",
                "2017-10-22");
        mAdapter.addItem("못하는게 : ",
                "뭔가요 당신",
                "2017-10-23");
        mAdapter.addItem("날씨가 추워지니 ",
                "다들 딱 붙어 뎅긴다",
                "2017-10-24");
        mAdapter.addItem("커플들 ",
                "다 사라졌으면^^",
                "2017-10-25");
        */

        //setting 이미지 아이콘을 터치할때 화면전환 되는 부분
        ImageView setting = (ImageView)findViewById(R.id.setting_image);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);

                intent.putExtra("name", user_name);
                intent.putExtra("id", user_id);
                intent.putExtra("image_url", user_image_url);

                startActivity(intent);
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.homework_list);
        mAdapter = new MainActivity.ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        for(int n = 0; n < homework.size(); n++){
            mAdapter.addItem(my_homework[n] + " : ", my_homework_context[n], my_homework_limit[n]);
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                final String class_name = ((TextView) view.findViewById(R.id.mTitle)).getText().toString();
                final String class_context = ((TextView) view.findViewById(R.id.mText)).getText().toString();
                final LinearLayout row = (LinearLayout)view.findViewById(R.id.row_layout);

                ColorDrawable color = (ColorDrawable) row.getBackground();

                if(color.getColor() == Color.rgb(255, 204, 204)){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle(class_name);
                    dialog.setMessage(class_context+" - 아직 덜함?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            row.setBackgroundColor(Color.rgb(255,255, 255));

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean(class_context, false);
                            editor.commit();
                        }
                    });
                    dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }else{

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle(class_name);
                    dialog.setMessage(class_context + " - 완료하셨습니까?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            row.setBackgroundColor(Color.rgb(255, 204, 204));

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean(class_context, true);
                            editor.commit();
                        }
                    });
                    dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Toast.makeText(MainActivity.this, id+"", Toast.LENGTH_SHORT).show();

                final String class_name = ((TextView) v.findViewById(R.id.mTitle)).getText().toString();
                final String date = ((TextView) v.findViewById(R.id.mDate)).getText().toString();

                Date curDAte = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date reqDate = new Date();
                try {
                    reqDate = dateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long reqDateTime = reqDate.getTime();
                try {
                    curDAte = dateFormat.parse(dateFormat.format(curDAte));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long curDateTime = curDAte.getTime();
                long minute = (curDateTime - reqDateTime) / 60000;
                if(minute > 0){
                    minute = -1;
                }else{
                    minute = Math.abs(minute);
                }
                //String time_to_limit = Long.toString(minute);
                long limit_day = minute / 1440; // 24 * 60
                long limit_hour = (minute - (limit_day * 1440)) / 60;
                long limit_minute = (minute - (limit_day * 1440 + limit_hour * 60));

                String left_time;

                if(limit_day > 0){
                    left_time = Long.toString(limit_day) + "일 " + Long.toString(limit_hour) + "시간 " + Long.toString(limit_minute) +"분 남았습니다.";
                }else{
                    if(limit_hour > 0){
                        left_time = Long.toString(limit_hour) + "시간 " + Long.toString(limit_minute) +"분 남았습니다.";
                    }else{
                        if(limit_minute > 0){
                            left_time =Long.toString(limit_minute) +"분 남았습니다.";
                        }else{
                            left_time = "과제 제출기간이 지났습니다.";
                        }
                    }
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(class_name +"과제 남은 시간");
                dialog.setMessage(left_time);

                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });


        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myclass.clear();
                homework.clear();
                homework_context.clear();
                homework_limit.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot find_me : snapshot.child("Student").getChildren()){
                        if(find_me.getKey().equals(my_id)){
                            for(DataSnapshot my_homework : snapshot.child("Homework").getChildren()){
                                String title = snapshot.getKey();
                                String text = my_homework.getKey();
                                String date = my_homework.getValue().toString();

                                homework.add(title);
                                homework_context.add(text);
                                homework_limit.add(date);
                            }
                        }
                    }
                    my_homework = homework.toArray(new String[homework.size()]);
                    my_homework_context = homework_context.toArray(new String[homework_context.size()]);
                    my_homework_limit = homework_limit.toArray(new String[homework_limit.size()]);

                    mListView = (ListView) findViewById(R.id.homework_list);
                    mAdapter = new MainActivity.ListViewAdapter(MainActivity.this);
                    mListView.setAdapter(mAdapter);

                    for(int n = 0; n < homework.size(); n++){
                        mAdapter.addItem(my_homework[n] + " : ", my_homework_context[n], my_homework_limit[n]);
                    }
                    mAdapter.sort();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }


    private class ViewHolder {
        public TextView mTitle;

        public TextView mText;

        public TextView mDate;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();



        public void addItem(String mTitle, String mText, String mDate){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mTitle;
            addInfo.mText = mText;
            addInfo.mDate = mDate;

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void sort(){
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange(){
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
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.myrow, null);

                holder.mTitle = (TextView) convertView.findViewById(R.id.mTitle);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);
            holder.mTitle.setText(mData.mTitle);
            holder.mText.setText(mData.mText);
            holder.mDate.setText(mData.mDate);

            Boolean homework;
            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            homework = pref.getBoolean(mData.mText, false);

            if(homework){
                convertView.setBackgroundColor(Color.rgb(255, 204, 204));
            }else{
                convertView.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            
            return convertView;
        }

    }

}


