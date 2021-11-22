package com.rianezza.volley_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void actionVolley(View view) {
        Intent a = new Intent(this, retrofit.class);
        startActivity(a);
    }

    public void actionRetrofit(View view) {
        Intent b = new Intent(this, volley.class);
        startActivity(b);
    }
}