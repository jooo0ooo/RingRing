package mokpoharbor.ringring;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by pingrae on 2017. 10. 20..
 */

public class ClassSettingProfessorActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList <String> myclass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_setting_professor);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("professor_class");

        //final ArrayList my_class_list = new ArrayList();

        //액티비티 타이틀바 내용 설정
        setTitle("Class Setting");

        ImageView urgent_notice = (ImageView) findViewById(R.id.urgent_notice);
        urgent_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassSettingProfessorActivity.this, "urgent notice - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });

        final Button my_class = (Button) findViewById(R.id.my_class);
        my_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ClassSettingProfessorActivity.this, test2, Toast.LENGTH_SHORT).show();

                final String[] test = myclass.toArray(new String[myclass.size()]);


                AlertDialog.Builder builder = new AlertDialog.Builder(ClassSettingProfessorActivity.this);
                builder.setTitle("My Class");
                builder.setItems(test, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ClassSettingProfessorActivity.this, test[which], Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("닫기",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                //builder.create();
                builder.show();
            }

        });


        Button register_class = (Button) findViewById(R.id.register_class);
        register_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText etEdit = new EditText(ClassSettingProfessorActivity.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClassSettingProfessorActivity.this);
                dialog.setTitle("강좌명을 입력해 주세요.");
                dialog.setView(etEdit);
// OK 버튼 이벤트
                dialog.setPositiveButton("Regist", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String class_name = etEdit.getText().toString();
                        myRef.child(class_name).push().setValue(class_name);
                        Toast.makeText(ClassSettingProfessorActivity.this, class_name, Toast.LENGTH_SHORT).show();
                    }
                });
// Cancel 버튼 이벤트
                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                /*
                String class_name = etEdit.getText().toString();
                myRef.child("class").push().setValue(class_name);
                */

                //Toast.makeText(ClassSettingProfessorActivity.this, "강좌 등록하기 - 만들 예정", Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myclass.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    myclass.add(key);
                }
                /*
                int num = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getValue().toString();
                    num++;
                }

                test2 = new String[num];


                int i = 0;
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while(child.hasNext()){
                    test2[i] = child.next().getKey();
                    //Toast.makeText(ClassSettingProfessorActivity.this, test2[i], Toast.LENGTH_SHORT).show();
                }
*/
                //Toast.makeText(ClassSettingProfessorActivity.this, num+"test", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }



}