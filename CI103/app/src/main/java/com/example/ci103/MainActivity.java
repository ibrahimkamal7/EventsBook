package com.example.ci103;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    public boolean check(Event event,List<Event> events){
        int i;
        for(i=0;i<events.size();i++){
            if(events.get(i).getId().equals(event.getId()))
                return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarText);
        textView.setText("Home");
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
                    break;
                    case R.id.update:startActivity(new Intent(getApplicationContext(),UpdateEvent.class));
                    break;
                }
                return false;
            }
        });
        final ListView listview=(ListView)findViewById(R.id.listview);
        Resources res=getResources();
        final DatabaseReference event= FirebaseDatabase.getInstance().getReference("Events");
        final List<Event> events=new ArrayList<>();
        event.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index=0;
                for(DataSnapshot keynode:dataSnapshot.getChildren()){
                    Event event=keynode.getValue(Event.class);
                    if(check(event,events)) {
                        events.add(event);
                        PublicCustomAdapter pca = new PublicCustomAdapter(getApplicationContext(), events);
                        listview.setAdapter(pca);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG);
            }
        });
        //listview.setAdapter(new ArrayAdapter<String>(this,R.layout.custom_list_public));
        PublicCustomAdapter pca = new PublicCustomAdapter(this, events);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),PublicEventDescription.class);

                i.putExtra("id",events.get(position).getId());
                i.putExtra("name",events.get(position).getEvent_name());
                i.putExtra("date",events.get(position).getDate());
                i.putExtra("time",events.get(position).getTime());
                i.putExtra("desc",events.get(position).getDescription());
                i.putExtra("address",events.get(position).getAddress());
                startActivity(i);
            }
        });
    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("ItemSelected: ","logout");
        if(menuItem.getItemId()==R.id.logout)
            Log.d("ItemSelected: ","logout");
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(abdt.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/
}

