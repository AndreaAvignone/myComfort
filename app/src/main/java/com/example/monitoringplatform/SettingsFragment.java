package com.example.monitoringplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        deletelist();
        update(rootKey);

    }
    public void createPref(String rootKey){

        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
    public void deletelist() {

        SharedPreferences userdetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        Gson gsonID = new Gson();
        String jsonID = userdetails.getString("edited", "");
        Type typeID = new TypeToken<List<String>>() {
        }.getType();
        List<String> mList = gsonID.fromJson(jsonID, typeID);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        for (String temp : mList) {
            editor.remove(temp);
            editor.apply();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean isInt=false;
        boolean isFloat=false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String parameter_value=prefs.getString(key,"");
        SharedPreferences userdetails = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        String profilesURL = userdetails.getString("profilesURL", "");
        if(key.equals("inactive_time")){
            isInt=true;
        }
        try {
            Util.postParameter(getContext(),profilesURL,"/","/setParameter/",key, parameter_value,isInt,isFloat,new Util.PostCallback() {
                @Override
                public void onRespSuccess(JSONObject result) {
                    Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onRespError(String result) {
                    Toast.makeText(getActivity(),"Can't save settings",Toast.LENGTH_LONG).show();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void update(String rootKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Map<String, ?> allEntries = preferences.getAll();
        SharedPreferences currentdetails = getActivity().getSharedPreferences("currentdetails", Context.MODE_PRIVATE);
        String platform_ID = currentdetails.getString("platform_ID", "");
        SharedPreferences userdetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
        String profilesURL=userdetails.getString("profilesURL","");
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Util.getPlatformInfo(profilesURL, platform_ID, entry.getKey(), getActivity(), new Util.ResponseCallback() {

                @Override
                public void onRespSuccess(String result) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(entry.getKey(),result);
                    editor.apply();
                    createPref(rootKey);

                }

                @Override
                public void onRespError(String result) {
                    Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

}