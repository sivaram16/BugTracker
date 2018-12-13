package com.example.sivaram.bugtracker;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class EditBug extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView title;
    private TextView descp;
    private TextView assgin;
    private String item,descpt;
    private String assignedto;
    private String userid;
    private String bugid;
    private List<String> categories;
    private Spinner dropdown;
    private String statuss;
    private ArrayAdapter<String> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bug);
        title=findViewById(R.id.textView);
        descp=findViewById(R.id.textView1);
        assgin=findViewById(R.id.assign);
        dropdown = findViewById(R.id.dropdown1);
        categories = new ArrayList<String>();
        statuss=getIntent().getExtras().getString("statuss");

        Log.e("ghghghg",statuss);


        categories.add("resolved");
        categories.add("unresolved");
        categories.add("closed");








        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dataAdapter);
        dataAdapter = (ArrayAdapter) dropdown.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = dataAdapter.getPosition(statuss);
        dropdown.setSelection(spinnerPosition);

        dropdown.setOnItemSelectedListener(this);



        item=getIntent().getExtras().getString("listitem");
        userid=getIntent().getExtras().getString("UID");
        descpt=getIntent().getExtras().getString("listitemdescp");
        bugid=getIntent().getExtras().getString("bugid");
        assignedto=getIntent().getExtras().getString("assign");
        Log.e("kjkhkkhkh","hjhjhjhjh"+assignedto);
        Log.e("",""+title);
        Log.e("",""+descpt);
        title.setText(item);
        assgin.setText(assignedto);
        descp.setText(descpt);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent)); //status bar or the time bar at the top
        }



        /*MyApolloClient.getApolloClient().query(ViewBugsQuery.builder().userId(userid)
                .build())
                .enqueue(new ApolloCall.Callback<ViewBugsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ViewBugsQuery.Data> response) {


            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });*/


    }

    public void back(View view) {
        onBackPressed();
    }

    public void update(View view) {

        MyApolloClient.getApolloClient().mutate(ChangeStatusMutation.builder()
                .bugId(bugid)
                .userId(userid)
                .status(dropdown.getSelectedItem().toString()).build()).enqueue(new ApolloCall.Callback<ChangeStatusMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ChangeStatusMutation.Data> response) {
                Log.e("dssss",""+response.data().changeStatus);
                onBackPressed();
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Toast.makeText(EditBug.this, "Updated failed ! " , Toast.LENGTH_LONG).show();
                onBackPressed();


            }
        });

        Toast.makeText(EditBug.this, "Updated Successfully !" , Toast.LENGTH_LONG).show();




    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
