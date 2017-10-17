package mokpoharbor.ringring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by pingrae on 2017. 10. 17..
 */

public class SplashActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
