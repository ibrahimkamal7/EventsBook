package com.example.ci103;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private  List<Messages> messages=new ArrayList<>();
    private LayoutInflater linf;
    private String email,time;

    public MessageAdapter(Context c,List<Messages> messages) {
        this.messages = messages;
        linf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FirebaseUser currentFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(!messages.get(position).getSenderId().equals(currentFirebaseUser.getUid())) {
            View v = linf.inflate(R.layout.message_left, null);
            final TextView sender = (TextView) v.findViewById(R.id.sender);
            final TextView message = (TextView) v.findViewById(R.id.senderMessage);
            TextView time=(TextView)v.findViewById(R.id.time);
            message.setText(messages.get(position).getMessage());
            time.setText(messages.get(position).getTime());
            DatabaseReference users = FirebaseDatabase.getInstance().getReference("User").child(messages.get(position).getSenderId());
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    email = dataSnapshot.child("email").getValue().toString();
                    sender.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return v;
        }
        else{
            View v=linf.inflate(R.layout.message_right,null);
            TextView message=(TextView) v.findViewById(R.id.myMessage);
            message.setText(messages.get(position).getMessage());
            TextView time=(TextView)v.findViewById(R.id.textView8);
            time.setText(messages.get(position).getTime());
            return v;
        }
    }


}
