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

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

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

                dialog.setPositiveButton("Regist", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        classRef.child(class_name).child("Student").child(my_id).setValue(my_id);
                        userRef.child(my_id).child("my_class").child(class_name).setValue(class_name);

                        Toast.makeText(StudentRegistClass.this, class_name+"등록 완료", Toast.LENGTH_SHORT).show();
                    }
                });

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

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    myclass.add(key);
                }
                test = myclass.toArray(new String[myclass.size()]);
                settingList();
                arraylist.clear();
                arraylist.addAll(myclass);
            }


            @Override
            public void onCancelled(DatabaseError error) {
            }
        });



    }

    // 검색을 수행하는 메소드
    public void search(String charText) {
        list.clear();

        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for(int i = 0;i < arraylist.size(); i++) {
                if (arraylist.get(i).toLowerCase().contains(charText)) {
                    list.add(arraylist.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void settingList(){
        for(int i = 0; i < myclass.size(); i++){
            list.add(test[i]);
        }
    }
}
