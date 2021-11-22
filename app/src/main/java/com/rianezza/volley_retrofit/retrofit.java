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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofit extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ListView lvUser;
    private List<user> listuser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        lvUser = findViewById(R.id.list_user);
        getUserFromAPI();
    }

    private void getUserFromAPI() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Retrofit");
        progressDialog.setMessage("Silahkan Tunggu...");
        progressDialog.show();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://192.168.1.6/volley")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        MethodHTTP client = retrofit.create(MethodHTTP.class);
        Call<user_response> call = client.getUser();

        call.enqueue(new Callback<user_response>() {
            @Override
            public void onResponse(Call<user_response> call, Response<user_response> response) {
                progressDialog.dismiss();
                listuser = response.body().getUserList();
                user_adapter user_adapter = new user_adapter(getApplicationContext(), listuser);
                lvUser.setAdapter(user_adapter);
                lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), listuser.get(i).getUser_fullname(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<user_response> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public void actionClose(View view) {
        this.finish();
    }

    public void actionRefresh(View view) {
        getUserFromAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.retrofit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent(this, adduseractivity.class);
                intent.putExtra("typeConnection", "retrofit");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}