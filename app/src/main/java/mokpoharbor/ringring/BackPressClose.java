package mokpoharbor.ringring;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by pingrae on 2017. 10. 17..
 */


//꼬레와 뒤로가기 버튼 눌렀을때 토스트 메세지 뛰어주는 부분 데스.

public class BackPressClose {
    private long backkey_pressed_time = 0;
    private Toast toast;

    private Activity activity;

    public BackPressClose(Activity context){
        this.activity = context;
    }

    public void onBackPressed(){

        //뒤로가기 버튼 눌렀는데 2초가 지났다. 그러면 어플 노우 종료!
        if(System.currentTimeMillis() > backkey_pressed_time + 2000){
            backkey_pressed_time = System.currentTimeMillis();
            showToast();
            return;
        }

        //뒤로가기 버튼 눌르고 2초 이내 또 눌렀따? 굿바이~
        if(System.currentTimeMillis() <= backkey_pressed_time + 2000){

            activity.finish();
            toast.cancel();
        }
    }

    private void showToast(){
        toast = Toast.makeText(activity, "버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}


