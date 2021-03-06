package com.example.monitoringplatform.add_room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoringplatform.R;
import com.example.monitoringplatform.Util;
import com.example.monitoringplatform.add_platform.new_platform_form_location;
import com.example.monitoringplatform.homepage;
import com.example.monitoringplatform.overview;

import org.json.JSONException;
import org.json.JSONObject;

public class new_room_form_name extends AppCompatActivity {
    private String room_name;
    private String room_ID="room_X";
    String plat_ID;
    String plat_name;
    String final_plat_ID;
    boolean isFirstRoom=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room_form);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras.containsKey("platform_ID") && extras.containsKey("platform_name")) {
            plat_ID = i.getStringExtra("platform_ID");
            plat_name = i.getStringExtra("platform_name");
            isFirstRoom=true;
        }

        Button next=findViewById(R.id.button_next_room);
        TextView enterRoom=findViewById(R.id.newroomName);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        enterRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                next.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSubmitIfReady(enterRoom,next);


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room_name=enterRoom.getText().toString();
                if(containsWhiteSpace(room_name)){
                    Toast.makeText(new_room_form_name.this,"Spaces are not allowed.",Toast.LENGTH_SHORT).show();
                }
                SharedPreferences userdetails = new_room_form_name.this.getSharedPreferences("userdetails", MODE_PRIVATE);
                String profilesURL=userdetails.getString("profilesURL","");
                SharedPreferences currentdetails = new_room_form_name.this.getSharedPreferences("currentdetails", MODE_PRIVATE);
                String platform_ID=currentdetails.getString("platform_ID","");
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("room_ID", room_ID);
                    jsonBody.put("room_name", room_name.replace(" ",""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isFirstRoom){
                    final_plat_ID= new String(plat_ID);
                }else{
                    final_plat_ID= new String(platform_ID);
                }
                try {
                    Util.putElement(new_room_form_name.this, profilesURL, "/insertRoom/", final_plat_ID, jsonBody, new Util.PutCallback() {
                        @Override
                        public void onResponseSuccess(JSONObject result) {
                            try {
                                if(result.getBoolean("result")){
                                    Toast.makeText(new_room_form_name.this,"New room created",Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onResponseError(String result) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    public void enableSubmitIfReady(TextView enterPlatform, Button next) {

        boolean isReady = enterPlatform.getText().toString().length() > 0;
        next.setEnabled(isReady);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public static boolean containsWhiteSpace( String testCode){
        if(testCode != null){
            for(int i = 0; i < testCode.length(); i++){
                if(Character.isWhitespace(testCode.charAt(i))){
                    return true;
                }
            }
        }
        return false;
    }

}