package com.example.ci103;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Chat extends AppCompatActivity {
    private DatabaseReference events;
    private FirebaseUser currentFirebaseUser;
    private String event_id;
    private List<String> messages=new ArrayList<>();
    private List<String> senders=new ArrayList<>();
    private List<Messages> messageObjects=new ArrayList<>();
    private ListView messageBox;
    private boolean counter=false;
    private String senderEmail;
    private String CHANNEL_ID="Event Messages";
    private Messages m;
    private String name,date,time,desc,address;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    public boolean check(Messages message){
        int i;
        for(i=0;i<messageObjects.size();i++){
            if(messageObjects.get(i).getId().equals(message.getId()))
                return false;
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent i=getIntent();
        event_id=i.getStringExtra("event_id");
        String eventName=i.getStringExtra("event_name");
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarText);
        textView.setText(eventName);
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
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i=new Intent(getApplicationContext(),PublicEventDescription.class);
                DatabaseReference event=FirebaseDatabase.getInstance().getReference("Events").child(event_id);
                event.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name=dataSnapshot.child("event_name").getValue().toString();
                        date=dataSnapshot.child("date").getValue().toString();
                        time=dataSnapshot.child("time").getValue().toString();
                        desc=dataSnapshot.child("description").getValue().toString();
                        address=dataSnapshot.child("address").getValue().toString();
                        i.putExtra("id",event_id);
                        i.putExtra("name",name);
                        i.putExtra("date",date);
                        i.putExtra("time",time);
                        i.putExtra("desc",desc);
                        i.putExtra("address",address);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        events= FirebaseDatabase.getInstance().getReference("Events");
        currentFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        ImageButton send=(ImageButton)findViewById(R.id.imageButton);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText message=(EditText)findViewById(R.id.editText9);
                if(!message.getText().toString().equals("")) {
                    String messageId = events.push().getKey();
                    String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    Messages m = new Messages(message.getText().toString(), currentFirebaseUser.getUid(), messageId, timeStamp);
                    events.child(event_id).child("messages").child(messageId).setValue(m);
                    message.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cant send Empty messages",Toast.LENGTH_SHORT).show();
                }
            }
        });
        DatabaseReference mes=FirebaseDatabase.getInstance().getReference("Events").child(event_id).child("messages");
        mes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot keynode:dataSnapshot.getChildren()) {
                    m = keynode.getValue(Messages.class);
                    String message = m.getMessage();
                    if (check(m)) {
                        messages.add(message);
                        messageObjects.add(m);
                        MessageAdapter ma = new MessageAdapter(getApplicationContext(), messageObjects);
                        messageBox = findViewById(R.id.messageBox);
                        messageBox.setAdapter(ma);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
