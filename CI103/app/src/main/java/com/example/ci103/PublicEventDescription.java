package com.example.ci103;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class PublicEventDescription extends AppCompatActivity {
    String n="null";
    String going="",attending="",smembers="";
    Boolean counter=true,counter1=true,counter2=true;
    private String[] temp,members;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private boolean check(){
        int i;
        FirebaseUser currentFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        for(i=0;i<members.length;i++) {
            if (members[i].equals(currentFirebaseUser.getUid()))
                return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_event_description);
        counter1=true;
        final Intent i=getIntent();
        Log.d("d","intent created");
        final String id=i.getStringExtra("id");
        Resources res=getResources();
        String name=i.getStringExtra("name");
        String date=i.getStringExtra("date");
        String time=i.getStringExtra("time");
        String desc=i.getStringExtra("desc");

        final String location=i.getStringExtra("address");
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarText);
        textView.setText(name);
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
                        break;
                }
                return false;
            }
        });
        //String[] name=res.getStringArray(R.array.EventsName);
        //String[] date=res.getStringArray(R.array.date);
        //String[] time=res.getStringArray(R.array.time);
        //final String[] location=res.getStringArray(R.array.address);
        //String[] description=res.getStringArray(R.array.description);
        Log.d("d","string arrays allocated");
        TextView Date = (TextView) findViewById(R.id.Date);
        TextView Time = (TextView) findViewById(R.id.Time);
        TextView Location = (TextView) findViewById(R.id.Location);
        TextView Description = (TextView) findViewById(R.id.Description);
        Log.d("d","objects created");
        Date.setText(date);
        Time.setText(time);
        Location.setText(location);
        Description.setText(desc);
        Log.d("d","values set");
        final ListView listview = (ListView)findViewById(R.id.listMembers);
        DatabaseReference user=FirebaseDatabase.getInstance().getReference("Events").child(id);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                smembers=dataSnapshot.child("going").getValue().toString();
                String[] temp=smembers.split(";");
                if(temp.length>1){
                    members= Arrays.copyOfRange(temp, 1, temp.length);
                    members[members.length - 1].replace("}", " ");
                    MemberAdapter ma=new MemberAdapter(getApplicationContext(),members);
                    listview.setAdapter(ma);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button btn3=(Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("geo:0,0?q="+location);
                Intent openMap = new Intent(Intent.ACTION_VIEW,address);
                openMap.setPackage("com.google.android.apps.maps");
                startActivity(openMap);
            }
        });
        Button join=(Button) findViewById(R.id.button4);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                final DatabaseReference newMember= FirebaseDatabase.getInstance().getReference("Events");

                final DatabaseReference currentUser= FirebaseDatabase.getInstance().getReference("User");
                newMember.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(counter) {
                            going = (String) dataSnapshot.child(id).child("going").getValue();
                            if(check(going)) {
                                going = going + ";" + currentFirebaseUser.getUid();
                                newMember.child(id).child("going").setValue(going);
                                counter = false;
                                counter2=true;
                            }
                            else {
                                counter2=false;
                                Log.d("make Toast ","called");
                                Toast.makeText(getApplicationContext(),"You have already joined the event",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                currentUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(counter1&&counter2) {
                            attending = (String) dataSnapshot.child(currentFirebaseUser.getUid()).child("attending").getValue();
                            attending = attending + ";" + id.toString();
                            currentUser.child(currentFirebaseUser.getUid()).child("attending").setValue(attending);
                            counter1 = false;
                            counter2=false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    public boolean check(String going){
        String[] ids=going.split(";");
        int i;
        for(i=1;i<ids.length;i++)
            Log.d("going ",ids[i]);
        FirebaseUser currentFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String uid=currentFirebaseUser.getUid();
        for(i=1;i<ids.length;i++){
            if(ids[i].equals(uid)){
                Log.d("return ","false");
                return false;
            }
        }
        Log.d("return ","true");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(abdt.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
