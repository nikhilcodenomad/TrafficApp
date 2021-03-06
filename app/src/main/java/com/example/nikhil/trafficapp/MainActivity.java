package com.example.nikhil.trafficapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    Button login;
    RelativeLayout rl;
    ImageView iv6;
    SessionManager sessionmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the components of the Login Page
        init();
        //Add action Listener to the button login
        login.setOnClickListener(this);

    }

    private void init(){

        sessionmanager = new SessionManager(this);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        rl = (RelativeLayout)findViewById(R.id.activity_main);
        iv6 = (ImageView)findViewById(R.id.imageView6);
    }

    @Override
    public void onClick(View v) {


        if(validinput()) {
            if (isConnected()) {
                String url1 = "http://k53ldts.org/api/index.php/login/";
                String uname = username.getText().toString();
                String pass = password.getText().toString();
                String data = "{\"username\":\"" + uname + "\", \"password\": \"" + pass + "\"}";
                try {
                    HttpPostingData htdp = new HttpPostingData(url1, data);
                    String res = htdp.getRes();
                    //System.out.print(res);
                    //Toast.makeText(this, res, Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray arr = jsonObject.getJSONArray("result");
                    for(int i=0; i<arr.length(); i++){
                        JSONObject o = arr.getJSONObject(i);

                        if(o.getString("id").matches("")){
                            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_LONG).show();
                        }else{
                            String user_type = o.getString("user_type");
                            String user_id = o.getString("id");
                            sessionmanager.loggingIn(user_type, user_id);
                            Intent it = new Intent(this, Home.class);
                            startActivity(it);

                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }





    }

    //Validating the data
    private boolean validinput(){
        if((username.getText().toString().matches("")) || (password.getText().toString().matches(""))){
            Toast.makeText(this, "Fields cann't be left blank", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }



}
