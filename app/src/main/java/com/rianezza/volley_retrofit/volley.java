package com.rianezza.volley_retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class volley extends AppCompatActivity {

    private String TAG;
    private ListView lvUserVolley;

    private void getUserFromAPI() {
        Gson gson = new Gson();
        String url = "https://192.168.1.6/volley/User_Registration.php";
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Volley");
        progressDialog.setMessage("Silahkan Tunggu...");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                user_response rsp = gson.fromJson(response.toString(), user_response.class);
                if (rsp.getCode() == 200) {
                    user_adapter adapter = new user_adapter(getApplicationContext(), rsp.getUserList());
                    lvUserVolley.setAdapter(adapter);
                    lvUserVolley.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(getApplicationContext(), rsp.getUserList().get(i).getUser_fullname(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Volley Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        lvUserVolley = findViewById(R.id.lv_user_volley);

        getUserFromAPI();
    }

    public void actionClose(View view) {
        finish();
    }

    public void actionRefresh(View view) {
        getUserFromAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.retrofit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, adduseractivity.class);
                intent.putExtra("typeConnection", "volley");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}