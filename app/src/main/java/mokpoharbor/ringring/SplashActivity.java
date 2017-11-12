package mokpoharbor.ringring;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by pingrae on 2017. 10. 17..
 */

public class SplashActivity extends Activity {
    String user_name;
    String user_id;
    String user_picture_url;
    String user_flag;
/*
    private void graphRequest(AccessToken accessToken) {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = settings.edit();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse
                            response) {
                        try {
                            if (object != null) {
                                String id = object.getString("id");
                                String name = object.getString("name");
                                editor.putString("username", id);
                                editor.putString("name", name);
                                editor.apply();
                                user_name = response.getJSONObject().getString("name").toString();
                                user_id = response.getJSONObject().getString("id").toString();
                                user_picture_url = new URL("https://graph.facebook.com/" + user_id + "/picture?width=500&height=500");
                                SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                String user_flag = pref.getString("user_flag", "nothing");

                                MyInfo.my_name = user_name;
                                MyInfo.my_id = user_id;
                                MyInfo.user_flag = user_flag;
                                MyInfo.user_picture_url = user_picture_url.toString();

                                if (user_flag.equals("Student")) {
                                    Intent i = new Intent(SplashActivity.this, StudentMainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else if (user_flag.equals("Professor")) {
                                    if(pref.getBoolean("isProfessor", false)){
                                        Intent i = new Intent(SplashActivity.this, ProfessorMainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Toast.makeText(SplashActivity.this, "Who are U", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SplashActivity.this, "Error or Not_User", Toast.LENGTH_SHORT).show();
                                }
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
*/
/*
    @Override
    protected void onResume() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (!TextUtils.isEmpty(settings.getString("username", ""))) {
            startActivity(new Intent(SplashActivity.this, StudentMainActivity.class));
            finish();
        }
        Profile profile = Profile.getCurrentProfile();
        super.onResume();
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(1000);
            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            user_flag = pref.getString("user_flag", "nothing");
            if(user_flag.equals("Student")){

                user_id = pref.getString("my_id", "nothing");
                user_name = pref.getString("my_name", "nothing");
                user_picture_url = pref.getString("picture_url", "nothing");

                MyInfo.my_name = user_name;
                MyInfo.my_id = user_id;
                MyInfo.user_flag = user_flag;
                MyInfo.user_picture_url = user_picture_url;

                Intent i = new Intent(SplashActivity.this, StudentMainActivity.class);
                startActivity(i);
                finish();

            }else if(user_flag.equals("Professor")){

                user_id = pref.getString("my_id", "nothing");
                user_name = pref.getString("my_name", "nothing");
                user_picture_url = pref.getString("picture_url", "nothing");

                MyInfo.my_name = user_name;
                MyInfo.my_id = user_id;
                MyInfo.user_flag = user_flag;
                MyInfo.user_picture_url = user_picture_url;

                Intent i = new Intent(SplashActivity.this, ProfessorMainActivity.class);
                startActivity(i);
                finish();

            }else{
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            //graphRequest(AccessToken.getCurrentAccessToken());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}