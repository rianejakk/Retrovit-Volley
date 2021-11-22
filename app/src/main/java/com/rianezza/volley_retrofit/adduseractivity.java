package com.rianezza.volley_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class adduseractivity extends AppCompatActivity {

    private EditText fullname, email, password;
    private Button btnSubmit;
    private String typeConn = "";

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduseractivity);
        fullname = findViewById(R.id.edt_fullname);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        btnSubmit = findViewById(R.id.btn_submit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            typeConn = extras.getString("typeConnection", "Undefined");
        }
    }

    public void submitByRetrofit(user user) {
        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(getString(R.string.retrofit));
        progressDialog.setMessage("Sedang disubmit...");
        progressDialog.show();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://192.168.1.6/volley")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        MethodHTTP client = retrofit.create(MethodHTTP.class);
        Call<request> call = client.sendUser(user);

        call.enqueue(new Callback<request>() {
            @Override
            public void onResponse(Call<request> call, Response<request> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getCode() == 201) {
                        Toast.makeText(getApplicationContext(), "Response : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (response.body().getCode() == 406) {
                        Toast.makeText(getApplicationContext(), "Response : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        email.requestFocus();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Response : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                Log.e(TAG, "Error : " + response.message());
            }

            @Override
            public void onFailure(Call<request> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Error 2 : " + t.getMessage());
            }
        });
    }

    public void actionSubmit(View view) {
        boolean isInputValid = false;

        if (fullname.getText().toString().isEmpty()) {
            fullname.setError("Tidak Boleh Kosong");
            fullname.requestFocus();
            isInputValid = false;
        }
        else {
            isInputValid = true;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Tidak Boleh Kosong");
            email.requestFocus();
            isInputValid = false;
        }
        else {
            isInputValid = true;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Tidak Boleh Kosong");
            password.requestFocus();
            isInputValid = false;
        }
        else {
            isInputValid = true;
        }
        if (isInputValid) {
            user usr = new user();
            usr.setUser_fullname(fullname.getText().toString());
            usr.setUser_email(email.getText().toString());
            usr.setUser_password(password.getText().toString());
            if (typeConn.equalsIgnoreCase("retrofit"))
                submitByRetrofit(usr);
            else submitbyVolley(usr);
        }
    }

    private void submitbyVolley(user usr) {
        Gson gson = new Gson();
        String URL = "https://192.168.1.6/volley/User_Registration.php";

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Volley");
        progressDialog.setMessage("Sedang disubmit");
        progressDialog.show();

        String userRequest = gson.toJson(usr);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response != null) {
                    request requestFormat = gson.fromJson(response.toString(), request.class);
                    if (requestFormat.getCode() == 201) {
                        Toast.makeText(getApplicationContext(), "Response : " + requestFormat.getStatus(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (requestFormat.getCode() == 406) {
                        Toast.makeText(getApplicationContext(), "Response : " + requestFormat.getStatus(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Response : " + requestFormat.getStatus(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG,"Error POST Volley : " + error.getMessage());
            }
        }) {
            @Override
            public byte[] getBody() {
                return userRequest.getBytes();
            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
}