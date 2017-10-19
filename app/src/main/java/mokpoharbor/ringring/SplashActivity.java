package mokpoharbor.ringring;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pingrae on 2017. 10. 17..
 */

public class SplashActivity extends Activity {

    private String user_name;
    private String user_id;
    private URL user_picture_url;

    private SharedPreferences settings ;
    private void graphRequest(AccessToken accessToken) {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = settings.edit();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse
                            response) {
                        try {
                            if(object != null){

                                String id = object.getString("id");
                                String name = object.getString("name");

                                editor.putString("username", id);
                                editor.putString("name", name);
                                editor.apply();

                                user_name = response.getJSONObject().getString("name").toString();
                                user_id = response.getJSONObject().getString("id").toString();
                                user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");

                                Intent i = new Intent(SplashActivity.this, MainActivity.class);

                                //인텐트 할때 얻은 정보도 같이 넘겨주기
                                i.putExtra("name", user_name);
                                i.putExtra("id", user_id);
                                i.putExtra("image_url", user_picture_url.toString());

                                startActivity(i);
                                finish();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    protected void onResume() {
        settings =PreferenceManager.getDefaultSharedPreferences(this);
        if (!TextUtils.isEmpty(settings.getString("username", ""))) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        Profile profile = Profile.getCurrentProfile();
        super.onResume();
    }


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
            graphRequest(AccessToken.getCurrentAccessToken());
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
