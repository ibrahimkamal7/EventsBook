package com.example.ci103;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Register");
        Button register=(Button)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                EditText editText2=(EditText)findViewById(R.id.editText2);
                final String email=editText2.getText().toString().trim();
                final EditText pwd=(EditText)findViewById(R.id.editText4);
                final String password=pwd.getText().toString();
                EditText cpwd=(EditText)findViewById(R.id.editText5);
                final TextView textView7=(TextView)findViewById(R.id.textView7);
                String cpassword=cpwd.getText().toString();
                final EditText n=(EditText)findViewById(R.id.editText);
                final String name=n.getText().toString();
                final FirebaseAuth auth= FirebaseAuth.getInstance();
                if(password.equals(cpassword) && password.length()>=6 && !name.equals("") && !email.equals("")){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final FirebaseUser user=auth.getCurrentUser();
                                DatabaseReference newUser= FirebaseDatabase.getInstance().getReference("User");
                                //String id=newUser.push().getKey();
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                String id=currentFirebaseUser.getUid();
                                User u=new User(id,name,email);
                                newUser.child(id).setValue(u);
                                Intent i= new Intent(getApplicationContext(),Login.class);
                                startActivity(i);
                            }
                            else{
                                Log.w("error:",task.getException());
                                Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                                //textView7.setText("error "+task.getException());
                            }
                        }
                    });
                }
                else
                {
                    if(!password.equals(cpassword)){
                        Toast.makeText(getApplicationContext(),"Passwords dont match",Toast.LENGTH_SHORT).show();
                    }
                    else if(password.length()<6){
                        Toast.makeText(getApplicationContext(),"Password should be more than 6 characters long",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid values entered",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }
}
