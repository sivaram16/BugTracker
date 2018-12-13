package com.example.sivaram.bugtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class AddBug extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner dropdown;
    private EditText title;
    private EditText descp;
    private String userid;
    private Boolean admin;
    List<String> items;
    private Button create;
    private String email;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bug);
        dropdown = findViewById(R.id.dropdown);
        title=findViewById(R.id.titleEdittext);
        descp=findViewById(R.id.des);
        create=findViewById(R.id.login);

        admin = getIntent().getExtras().getBoolean("Admin",false);
        userid = getIntent().getExtras().getString("UID");
        items=new ArrayList<String>();

        MyApolloClient.getApolloClient().query(AllUsersQuery.builder()
                .userId(userid).build()).enqueue(new ApolloCall.Callback<AllUsersQuery.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<AllUsersQuery.Data> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i =0;i<response.data().allUsers.size();i++) {
                            Log.e("", "" + response.data().allUsers.get(i).email);
                            Log.e("",""+response);
                            items.add(response.data().allUsers.get(i).email);
                            adapter.notifyDataSetChanged();
                        }


                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {



            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent)); //status bar or the time bar at the top
        }
    }

    private void post() {
        if (TextUtils.isEmpty(title.getText().toString())){
            title.setError("Enter the Title");
            title.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(descp.getText().toString())){
            descp.setError("Enter the Descriptin");
            descp.requestFocus();
            return;
        }


        MyApolloClient.getApolloClient().mutate(AddBugMutation.builder()
                .adminId(userid)
                .assignEmail(dropdown.getSelectedItem().toString())
                .title(title.getText().toString())
                .description(descp.getText().toString()).build())
                .enqueue(new ApolloCall.Callback<AddBugMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AddBugMutation.Data> response) {
                        Log.e("",""+response.data().addBug);
                        onBackPressed();

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Toast.makeText(AddBug.this,"Error to Add Bug ",Toast.LENGTH_SHORT).show();


                    }
                });

        Toast.makeText(AddBug.this,"Bug Added ",Toast.LENGTH_SHORT).show();
        email=dropdown.getSelectedItem().toString()+"@gmail.com";
        Intent y = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email,null));
        y.putExtra(Intent.EXTRA_SUBJECT,title.getText().toString());
        y.putExtra(Intent.EXTRA_TEXT, descp.getText().toString());
        startActivity(y.createChooser(y,"choose app to send feedback"));

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /*switch (i) {

            case 0:

                break;
            case 1:

                break;
            case 2:

                break;

        }*/
        String item = dropdown.getSelectedItem().toString();
        Log.e("ghghhjhjhjhjhjhghgjgjhg",""+item);

        Toast.makeText(AddBug.this, "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void back(View view) {

        onBackPressed();


    }
}
