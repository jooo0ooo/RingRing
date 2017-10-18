package mokpoharbor.ringring;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by pingrae on 2017. 10. 17..
 */

public class SplashActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void setAppPreferences(Activity context, String key, String value) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("FaceBookExample", 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        Log.d("LOG", "key = " + key + "  //  value = " + value);
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    private String getAppPreferences(Activity context, String key) {
        String returnValue = null;
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("FaceBookExample", 0);
        returnValue = pref.getString(key, "");
        return returnValue;
    }


}
