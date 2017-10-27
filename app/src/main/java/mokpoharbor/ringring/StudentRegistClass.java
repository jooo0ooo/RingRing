package mokpoharbor.ringring;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pingrae on 2017. 10. 27..
 */

public class StudentRegistClass extends AppCompatActivity{


    FirebaseDatabase database;
    DatabaseReference myRef, classRef, userRef;
    String my_id;
    ArrayList <String> myclass = new ArrayList<>();
    String[] test = myclass.toArray(new String[myclass.size()]);


//=======================================================

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_regist_class);


        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        my_id = pref.getString("my_id", "nothing");
        database = FirebaseDatabase.getInstance();
        classRef = database.getReference("class");
        userRef = database.getReference("user");



        //=====================================================================

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);
        //&&&&&&&&&&&&
        //arraylist.addAll(myclass);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String class_name = ((TextView) view.findViewById(R.id.label)).getText().toString();
                //final String class_name = arraylist.get(adapter.).toString();
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudentRegistClass.this);
                dialog.setTitle("강좌 등록");
                dialog.setMessage(class_name+"를 등록하시겠습니까?");
// OK 버튼 이벤트
                dialog.setPositiveButton("Regist", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        classRef.child(class_name).setValue(class_name);

                        //SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        //my_id = pref.getString("my_id", "nothing");
                        userRef.child(my_id).child("my_class").child(class_name).setValue(class_name);

                        //myRef.child(class_name).push().setValue(class_name);
                        Toast.makeText(StudentRegistClass.this, class_name+"등록 완료", Toast.LENGTH_SHORT).show();
                    }
                });
// Cancel 버튼 이벤트
                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                //Toast.makeText(StudentRegistClass.this, arraylist.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //=================================================

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myclass.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    myclass.add(key);
                }
                test = myclass.toArray(new String[myclass.size()]);
                settingList();
                arraylist.clear();
                arraylist.addAll(myclass);
                //adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){
        for(int i = 0; i < myclass.size(); i++){
            list.add(test[i]);
        }
    }

    //===================================================================

}
