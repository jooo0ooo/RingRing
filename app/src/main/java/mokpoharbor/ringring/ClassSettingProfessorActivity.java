package mokpoharbor.ringring;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

/**
 * Created by pingrae on 2017. 10. 20..
 */
public class ClassSettingProfessorActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference classRef, userRef;
    String my_name;
    ArrayList<String> myclass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_setting_professor);

        database = FirebaseDatabase.getInstance();
        classRef = database.getReference("class");
        userRef = database.getReference("user");
        setTitle("Class Setting");
        final Button my_class = (Button) findViewById(R.id.my_class);
        my_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] test = myclass.toArray(new String[myclass.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassSettingProfessorActivity.this);
                builder.setTitle("My Class");
                builder.setItems(test, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ClassSettingProfessorActivity.this, test[which], Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder remove = new AlertDialog.Builder(ClassSettingProfessorActivity.this);
                                remove.setTitle("Delete Class");
                                remove.setMessage("선택한 과목을 삭제하시겠습니까?");
                                remove.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        classRef.child(selected).removeValue();
                                        userRef.child(MyInfo.my_id).child("my_class").child(selected).removeValue();
                                        ad.cancel();
                                        Toast.makeText(ClassSettingProfessorActivity.this, selected + "가 삭제되었습니다", Toast.LENGTH_SHORT).show();
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
                final EditText etEdit = new EditText(ClassSettingProfessorActivity.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClassSettingProfessorActivity.this);
                dialog.setTitle("강좌명을 입력해 주세요.");
                dialog.setView(etEdit);
                dialog.setPositiveButton("Regist", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String class_name = etEdit.getText().toString();
                        classRef.child(class_name).setValue(class_name);
                        classRef.child(class_name).child("Professor").child(MyInfo.my_id).setValue(MyInfo.my_id);
                        userRef.child(MyInfo.my_id).child("my_class").child(class_name).setValue(class_name);
                        Toast.makeText(ClassSettingProfessorActivity.this, class_name, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myclass.clear();
                for (DataSnapshot snapshot : dataSnapshot.child(MyInfo.my_id).child("my_class").getChildren()) {
                    String key = snapshot.getKey();
                    myclass.add(key);
                }
                my_name = dataSnapshot.child(MyInfo.my_id).child("name").getValue().toString();
                TextView tv = (TextView) findViewById(R.id.empty_view);
                tv.setText(my_name + "교수님 반갑습니다.");
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}