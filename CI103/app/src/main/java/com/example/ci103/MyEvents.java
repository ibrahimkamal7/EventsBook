package com.example.ci103;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MyEvents extends AppCompatActivity {
    private DatabaseReference user;
    private FirebaseUser currentFirebaseUser;
    private ListView listView;
    private boolean counter=true;
    private String[] eventIds;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private String eventName,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarText);
        textView.setText("My Events");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        abdt=new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.logout:FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        break;
                    case R.id.home:startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        break;
                    case R.id.my_events:startActivity(new Intent(getApplicationContext(),MyEvents.class));
                        break;
                    case R.id.create_event:startActivity(new Intent(getApplicationContext(),CreateEvent.class));
                    break;
                    case R.id.map:startActivity(new Intent(getApplicationContext(),Map.class));

                }
                return false;
            }
        });
        currentFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        user= FirebaseDatabase.getInstance().getReference("User").child(currentFirebaseUser.getUid());
        listView=(ListView)findViewById(R.id.listview);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(counter) {
                    String attending = dataSnapshot.child("attending").toString();
                    String[] temp = attending.split(";");
                    if(temp.length>1) {
                        int i;
                        eventIds = Arrays.copyOfRange(temp, 1, temp.length);
                        eventIds[eventIds.length - 1].replace("}", " ");
                        String t = eventIds[eventIds.length - 1], te = "";

                        for (i = 0; i < t.length(); i++) {
                            if (t.charAt(i) == ' ')
                                eventIds[eventIds.length - 1] = te;
                            else
                                te = te + t.charAt(i);
                        }
                        if (eventIds.length >= 1) {
                            CustomAdapter ca = new CustomAdapter(getApplicationContext(), eventIds);
                            ListView listView = (ListView) findViewById(R.id.listview);
                            listView.setAdapter(ca);
                            counter = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent i=new Intent(getApplicationContext(),Chat.class);
                s=eventIds[position];

                DatabaseReference event=FirebaseDatabase.getInstance().getReference("Events").child(s);
                event.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        eventName=dataSnapshot.child("event_name").getValue().toString();
                        Log.d("event_name",eventName);
                        i.putExtra("event_id",s);
                        i.putExtra("event_name",eventName);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(abdt.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
