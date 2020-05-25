package com.example.ci103;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdatePage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private String nameEvent,id;
    private DrawerLayout dl;
    private Event e;
    private ActionBarDrawerToggle abdt;
    private EditText name,addre,desc;
    private TextView dateDialog,timeDialog;
    private int iday,iyear,imonth,fday,fmonth,fyear;
    private int imin,ihour,fmin,fhour,iampm,fampm;
    private Button delete,update;
    private boolean counter,counter1;
    private Spinner spinner;
    private String[] going;
    DatabaseReference user;
    private String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);
        Intent i=getIntent();
        e=(Event) i.getSerializableExtra("event");
        id=e.getId();
        nameEvent=e.getEvent_name();
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Update Event");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        abdt=new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navView= findViewById(R.id.nav_view);
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
        name= findViewById(R.id.editText8);
        addre=(EditText)findViewById(R.id.editText10);
        desc=(EditText)findViewById(R.id.editText11);
        name.setText(e.getEvent_name());
        addre.setText(e.getAddress());
        desc.setText(e.getDescription());
        dateDialog=(TextView)findViewById(R.id.textView24);
        timeDialog=(TextView)findViewById(R.id.textView25);
        dateDialog.setText(e.getDate());
        timeDialog.setText(e.getTime());
        dateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick","");
                Calendar c=Calendar.getInstance();
                iyear=c.get(Calendar.YEAR);
                imonth=c.get(Calendar.MONTH);
                iday=c.get(Calendar.DAY_OF_MONTH);
                Log.d("date",iday+" "+imonth+" "+iyear );
                DatePickerDialog datePickerDialog=new DatePickerDialog(UpdatePage.this,UpdatePage.this,iyear,imonth,iday);
                datePickerDialog.show();
            }
        });
        timeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                ihour=c.get(Calendar.HOUR_OF_DAY);
                imin=c.get(Calendar.MINUTE);
                iampm=c.get(Calendar.AM_PM);
                TimePickerDialog timePickerDialog=new TimePickerDialog(UpdatePage.this,UpdatePage.this,ihour,imin,true);
                timePickerDialog.show();
            }
        });
        spinner=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.eventTypes, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        if(e.getType().equals("Professional"))
            spinner.setSelection(0);
        else if(e.getType().equals("Sports"))
            spinner.setSelection(1);
        else if(e.getType().equals("Party"))
            spinner.setSelection(2);
        else if(e.getType().equals("Music"))
            spinner.setSelection(3);
        else
            spinner.setSelection(4);
        update=(Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                DatabaseReference event= FirebaseDatabase.getInstance().getReference("Events").child(id);
                String type=spinner.getSelectedItem().toString();
                String host_id=currentFirebaseUser.getUid();
                Log.d("Desc: ", desc.getText().toString());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String address=addre.getText().toString();
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    if(!name.getText().toString().equals("")&&!addre.getText().toString().equals("")&&!dateDialog.getText().toString().equals("Click to select Date")&&!timeDialog.getText().toString().equals("Click to Select Time")&&!desc.getText().toString().equals("")){
                        Address loc = addresses.get(0);
                        double longitude = loc.getLongitude();
                        double latitude = loc.getLatitude();
                        Event e=new Event(id,name.getText().toString(),addre.getText().toString(),dateDialog.getText().toString(),timeDialog.getText().toString(),desc.getText().toString(),host_id,type,latitude,longitude);
                        event.child("event_name").setValue(e.getEvent_name());
                        event.child("description").setValue(e.getDescription());
                        event.child("address").setValue(e.getAddress());
                        event.child("latitude").setValue(e.getLatitude());
                        event.child("longitude").setValue(e.getLongitude());
                        //event.child("going").setValue(going);
                        event.child("date").setValue(e.getDate());
                        event.child("time").setValue(e.getTime());
                        event.child("type").setValue(e.getType());
                        Toast.makeText(getApplicationContext(),"Event Updated",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),UpdateEvent.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"All fields are required to create an event",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Invalid address or error loading co-ordinates", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference event=FirebaseDatabase.getInstance().getReference("Events").child(e.getId());
                event.removeValue();
                going=e.getGoing().split(";");
                counter1=true;
                for(int i=1;i<going.length;i++){
                    counter1=true;
                    Log.d("User: ",going[i]);
                    user=FirebaseDatabase.getInstance().getReference("User").child(going[i]);
                    user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(counter1) {
                                String attending = dataSnapshot.child("attending").getValue().toString();
                                Log.d("attending before ",attending);
                                attending.replace(";"+id , "");
                                Log.d("attending after ",attending);
                                user.child("attending").setValue(attending);
                                counter1=false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                Intent intent=new Intent(getApplicationContext(),UpdateEvent.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fday=dayOfMonth;
        fmonth=month;
        fyear=year;
        dateDialog.setText(fday+" "+months[fmonth]+", "+fyear);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        fhour=hourOfDay;
        fmin=minute;
        timeDialog.setText(fhour+":"+fmin+" hours");
    }
}
