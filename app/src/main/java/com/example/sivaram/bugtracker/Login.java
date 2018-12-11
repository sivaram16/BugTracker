package com.example.sivaram.bugtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private String userid;
    private boolean admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email=findViewById(R.id.email);
        password =findViewById(R.id.password);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("","clicked");
                post();
            }
        });
        Log.e("siva","======================");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent)); //status bar or the time bar at the top
        }
    }

    private void get() {
        MyApolloClient.getApolloClient().query(TestQuery.builder().build()).enqueue(new ApolloCall.Callback<TestQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<TestQuery.Data> response) {
                Log.e("hello",""+response.data().test());
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("failed","");

            }
        });
    }

    private void post() {

        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Please Enter Email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }

        MyApolloClient.getApolloClient().mutate(LoginMutation.
                builder().email(email.getText().toString()).password(password.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<LoginMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<LoginMutation.Data> response) {

                Log.e("",""+response.data().login);
                if (response.data().login==null) {
                    return;
                }
                userid=response.data().login.id;
                admin=response.data().login.admin;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putBoolean("Admin", admin); // Storing boolean - true/false
                editor.putString("UID", userid); // Storing string
                editor.commit(); // commit changes


                Log.e("",""+response.data().login.id);
                Log.e(userid,"");

                Intent intent = new Intent(Login.this,MainActivity.class);
                intent.putExtra("UID",userid);
                intent.putExtra("Admin",admin);
                startActivity(intent);

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("failed post"," "+e.toString());

            }
        });
    }


}
