package com.example.sivaram.bugtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Intent intent=getIntent();
    Boolean admin;
    String userid;
    private  String[] listitemtitle;
    private  String[] listitemdescp;
    private  String[] status;
    private  String[] assignedto;
    private  String[] bugidd;
    private String[] statuss;
    private ListView bugid;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        try {
            admin = getIntent().getExtras().getBoolean("Admin",false);
            userid = getIntent().getExtras().getString("UID");
        }
        catch (Exception e){
            Log.e("",e.toString());
        }

        bugid=findViewById(R.id.lv);
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.listitem,R.id.tv,list);
        MyApolloClient.getApolloClient().query(ViewBugsQuery.builder()
                .userId(userid).build())
                .enqueue(new ApolloCall.Callback<ViewBugsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<ViewBugsQuery.Data> response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listitemtitle=new String[response.data().viewBugs.size()];
                        listitemdescp= new String[response.data().viewBugs.size()];
                        assignedto= new String[response.data().viewBugs.size()];
                        bugidd= new String[response.data().viewBugs.size()];
                        statuss=new String[response.data().viewBugs.size()];
                        for (int i=0;i<response.data().viewBugs.size();i++) {
                            Log.e("",""+response.data().viewBugs.get(i).title);
                            listitemtitle[i]=response.data().viewBugs.get(i).title;
                            bugidd[i]=response.data().viewBugs.get(i).id;
                            statuss[i]=response.data().viewBugs.get(i).status;
                            listitemdescp[i]=response.data().viewBugs.get(i).description;
                            assignedto[i]=response.data().viewBugs.get(i).assignedTo.email;
                            Log.e("",""+ Arrays.toString(assignedto));
                            list.add(response.data().viewBugs.get(i).title);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });



            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("","jhfjhfjhjfd");

            }
        });

        bugid.setAdapter(adapter);
        bugid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int itemposition=i;
                String value=(String) bugid.getItemAtPosition(itemposition);
                Toast.makeText(MainActivity.this, "Selected: " + value, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,EditBug.class);
                intent.putExtra("UID",userid);
                intent.putExtra("statuss",statuss[itemposition]);
                intent.putExtra("bugid",bugidd[itemposition]);
                intent.putExtra("assign",assignedto[itemposition]);
                intent.putExtra("listitem",listitemtitle[itemposition]);
                intent.putExtra("listitemdescp",listitemdescp[itemposition]);
                startActivity(intent);



            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!admin) {
            fab.hide();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(MainActivity.this,AddBug.class);
                intent.putExtra("UID",userid);
                intent.putExtra("assigned",assignedto);
                intent.putExtra("Admin",admin);
               startActivity(intent);
            }
        });

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(MenuItem item) {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }
}
