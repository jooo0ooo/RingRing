package mokpoharbor.ringring;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by pingrae on 2017. 10. 17..
 */

//WHAT is the FUNCTION of this class
//If the User Press the 'BackButton', the Application will be terminated
public class BackPressClose {
    private long backkey_pressed_time = 0;
    private Toast toast;
    private Activity activity;

    public BackPressClose(Activity context) {
        this.activity = context;
    }

    //If the User Press the 'BackButton', The User Can see the Toast Message
    //And then If the User Press the 'BackButton' AGAIN within 2SECONDS, the Application will be terminated
    public void onBackPressed() {
        if (System.currentTimeMillis() > backkey_pressed_time + 2000) {
            backkey_pressed_time = System.currentTimeMillis();
            showToast();
            return;
        }
        if (System.currentTimeMillis() <= backkey_pressed_time + 2000) {
            ActivityCompat.finishAffinity(activity);
            toast.cancel();
        }
    }

    private void showToast() {
        toast = Toast.makeText(activity, "버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}