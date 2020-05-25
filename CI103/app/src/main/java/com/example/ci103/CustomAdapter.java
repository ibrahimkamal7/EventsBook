package com.example.ci103;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater linf;
    private String[] eventids;
    private String type;
    String name,time,date;
    public CustomAdapter(Context c,String[] eventid) {
        this.eventids = eventid;
        linf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventids.length;
    }

    @Override
    public Object getItem(int position) {
        return eventids[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View v=linf.inflate(R.layout.custom_list_public,null);
        final TextView EventName=(TextView) v.findViewById(R.id.EventName);
        final TextView Time=(TextView) v.findViewById(R.id.Time);
        final TextView Date=(TextView) v.findViewById(R.id.Time);
        DatabaseReference event= FirebaseDatabase.getInstance().getReference("Events");
        event.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=(String)dataSnapshot.child(eventids[position]).child("event_name").getValue();
                time=(String)dataSnapshot.child(eventids[position]).child("time").getValue();
                date=(String)dataSnapshot.child(eventids[position]).child("date").getValue();
                type=(String) dataSnapshot.child(eventids[position]).child("type").getValue();
                Log.d("DATA::",eventids[position]+" "+position+" "+name+" "+time+" "+date);
                Log.d("length:",""+eventids.length);

                /*if(type=="Sports")
                    v.setBackground(Drawable.createFromPath(""));
                else if(type=="Music")
                    v.setBackgroundColor(Color.WHITE);
                else if(type=="Professional")
                    v.setBackgroundColor(Color.BLUE);
                else if(type=="Party")
                    v.setBackgroundColor(Color.YELLOW);
                else
                    v.setBackgroundColor(Color.GRAY);*/
                if(type.equals("Sports")) {
                    v.setBackgroundResource(R.drawable.sports);
                }
                else if(type.equals("Professional")){
                    v.setBackgroundResource(R.drawable.professional);
                }
                else if(type.equals("Music")){
                    v.setBackgroundResource(R.drawable.music3);
                }
                else if(type.equals("Party")){
                    v.setBackgroundResource(R.drawable.party);
                }
                else{
                    v.setBackgroundResource(R.drawable.other);
                }
                EventName.setText(name);
                Date.setText(time);
                Time.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*Log.d("Event id", events.get(1).getId());
        EventName.setText(events.get(position).getEvent_name());
        Date.setText(events.get(position).getDate());
        Time.setText(events.get(position).getTime());*/
        return v;
    }
}
