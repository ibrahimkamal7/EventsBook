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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private int iday,iyear,imonth,fday,fmonth,fyear;
    private int imin,ihour,fmin,fhour,iampm,fampm;
    private TextView dateDialog;
    private TextView timeDialog;
    private DrawerLayout dl;
    private String id;
    private ActionBarDrawerToggle abdt;
    private String hosted;
    private boolean counter=true;
    private String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        final Spinner spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.eventTypes, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarText);
        textView.setText("Create a New Event");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button create=(Button)findViewById(R.id.button);
        dateDialog=(TextView)findViewById(R.id.textView18);
        dateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick","");
                Calendar c=Calendar.getInstance();
                iyear=c.get(Calendar.YEAR);
                imonth=c.get(Calendar.MONTH);
                iday=c.get(Calendar.DAY_OF_MONTH);
                Log.d("date",iday+" "+imonth+" "+iyear );
                DatePickerDialog datePickerDialog=new DatePickerDialog(CreateEvent.this,CreateEvent.this,iyear,imonth,iday);
                datePickerDialog.show();
            }
        });
        timeDialog=(TextView)findViewById(R.id.textView19);
        timeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                ihour=c.get(Calendar.HOUR_OF_DAY);
                imin=c.get(Calendar.MINUTE);
                iampm=c.get(Calendar.AM_PM);
                TimePickerDialog timePickerDialog=new TimePickerDialog(CreateEvent.this,CreateEvent.this,ihour,imin,true);
                timePickerDialog.show();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                DatabaseReference event= FirebaseDatabase.getInstance().getReference("Events");
                EditText name=(EditText)findViewById(R.id.editText7);
                EditText addre=(EditText)findViewById(R.id.address);
                EditText desc=(EditText)findViewById(R.id.description);
                String type=spinner.getSelectedItem().toString();
                String host_id=currentFirebaseUser.getUid();
                id=event.push().getKey();
                Log.d("Desc: ", desc.getText().toString());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String address=addre.getText().toString();
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                try{
                    if(!name.getText().toString().equals("")&&!addre.getText().toString().equals("")&&!dateDialog.getText().toString().equals("Click to select Date")&&!timeDialog.getText().toString().equals("Click to Select Time")&&!desc.getText().toString().equals("")){
                        Address loc = addresses.get(0);
                        double longitude = loc.getLongitude();
                        double latitude = loc.getLatitude();
                        Event e=new Event(id,name.getText().toString(),addre.getText().toString(),dateDialog.getText().toString(),timeDialog.getText().toString(),desc.getText().toString(),host_id,type,latitude,longitude);
                        event.child(id).setValue(e);
                        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
                        final DatabaseReference user=FirebaseDatabase.getInstance().getReference("User").child(currentFirebaseUser.getUid());
                        user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(counter) {
                                    hosted = (String)dataSnapshot.child("events_hosted").getValue();
                                    user.child("events_hosted").setValue(hosted + ";" + id);
                                    Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
                                    counter=false;
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"All fields are required to create an event",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (IndexOutOfBoundsException |NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Invalid address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(abdt.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fday=dayOfMonth;
        fmonth=month;
        fyear=year;
        dateDialog.setText(fday+" "+months[fmonth]+", "+fyear);
        Log.d("date",fday+" "+fmonth+" "+fyear);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        fhour=hourOfDay;
        fmin=minute;
        timeDialog.setText(fhour+":"+fmin+" hours");
        Log.d("time ", fhour+":"+fmin);
    }
}
