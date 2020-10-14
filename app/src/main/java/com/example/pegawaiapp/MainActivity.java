package com.example.pegawaiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String JSON_STRING="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        getJSON();

    }

    private void getJSON(){
        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }

    public class GetJSON extends AsyncTask<Void, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this,"Mengambil Data",
                    "Mohon Tunggu..", false, false);

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler rh = new RequestHandler();
            String s=rh.sendGetRequest(konfigurasi.URL_GET_ALL);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            JSON_STRING=s;
            showEmployee();

        }
    }

    private void showEmployee() {
        JSONObject jsonObject=null;
        ArrayList<HashMap<String, String>> list
                =new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i=0;i<result.length();i++) {

                JSONObject jo=result.getJSONObject(i);
                String id=jo.getString(konfigurasi.TAG_ID);
                String name=jo.getString(konfigurasi.TAG_NAME);
                HashMap<String, String> employees=new HashMap<>();
                employees.put(konfigurasi.TAG_ID,id);
                employees.put(konfigurasi.TAG_NAME,name);
                list.add(employees);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleAdapter adapter=new SimpleAdapter(this, list,
                R.layout.list_item,
                new String[] {konfigurasi.TAG_ID, konfigurasi.TAG_NAME},
                new int[]{R.id.id,R.id.name});
        listView.setAdapter(adapter);

    }

}