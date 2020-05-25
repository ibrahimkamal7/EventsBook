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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateEvent extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private String hosted;
    private String[] hostedId;
    private ListView listView;
    private ArrayList<String> newList=new ArrayList<String>();
    private ArrayList<Event> eventsList=new ArrayList<Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarText);
        textView.setText("Update Event");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        abdt=new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        listView=(ListView)findViewById(R.id.updateList);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        break;
                    case R.id.home:startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        break;
                    case R.id.my_events:startActivity(new Intent(getApplicationContext(),MyEvents.class));
                        break;
                    case R.id.create_event:startActivity(new Intent(getApplicationContext(),CreateEvent.class));
                        break;
                    case R.id.map:startActivity(new Intent(getApplicationContext(),Map.class));
                        break;
                }
                return false;
            }
        });
        DatabaseReference user= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("events_hosted");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    hosted = dataSnapshot.getValue().toString();
                }
                catch (Exception e){
                    hosted="";
                }
                hostedId=hosted.split(";");
                hostedId[hostedId.length - 1].replace("}", " ");
                for(int i=1;i<hostedId.length;i++){
                    Log.d("data: ",hostedId[i]);
                    newList.add(hostedId[i]);
                }
                DatabaseReference event=FirebaseDatabase.getInstance().getReference("Events");
                event.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot keynode:dataSnapshot.getChildren()) {
                            if (newList.contains(keynode.child("id").getValue().toString())) {
                                eventsList.add(keynode.getValue(Event.class));
                            }
                        }

                        PublicCustomAdapter ca=new PublicCustomAdapter(getApplication(),eventsList);
                        listView.setAdapter(ca);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),UpdatePage.class);
                i.putExtra("event",eventsList.get(position));
                startActivity(i);
            }
        });
    }
}
