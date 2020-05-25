package com.example.ci103;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class MemberAdapter extends BaseAdapter {
    private LayoutInflater linf;
    private String[] members;
    public MemberAdapter(Context c,String[] members){
        this.members=members;
        linf=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return members.length;
    }

    @Override
    public Object getItem(int position) {
        return members[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View v=linf.inflate(R.layout.member_display,null);

        DatabaseReference user= FirebaseDatabase.getInstance().getReference("User");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView name=(TextView)v.findViewById(R.id.memberName);
                TextView email=(TextView)v.findViewById(R.id.memberEmail);

                name.setText(dataSnapshot.child(members[position]).child("name").getValue().toString());
                email.setText(dataSnapshot.child(members[position]).child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
