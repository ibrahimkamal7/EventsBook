package com.example.ci103;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PublicCustomAdapter extends BaseAdapter {
    List<Event> events=new ArrayList<>();
    LayoutInflater linf;
    public PublicCustomAdapter(Context c, List<Event> events){
        this.events = events;
        //Log.d("Event id", events.get(0).getId());
        linf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=linf.inflate(R.layout.custom_list_public,null);
        TextView EventName=(TextView) v.findViewById(R.id.EventName);
        TextView Time=(TextView) v.findViewById(R.id.Time);
        TextView Date=(TextView) v.findViewById(R.id.Time);
        String type=events.get(position).getType();
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
        v.getBackground().setAlpha(150);
        //Time.setVisibility(View.INVISIBLE);
        EventName.setText(events.get(position).getEvent_name());
        String[] t=events.get(position).getDate().split(",");
        Date.setText(t[0]);
        //Time.setText(events.get(position).getTime());
        return v;
    }
}
