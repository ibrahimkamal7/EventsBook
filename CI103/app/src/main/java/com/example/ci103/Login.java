package com.example.ci103;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);*/
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Login");
        final EditText editText3=(EditText)findViewById(R.id.editText3);
        final String emailId=editText3.getText().toString();
        TextView forgot=(TextView)findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId=editText3.getText().toString();
                if(emailId.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter email to proceed "+emailId,Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Email Sent",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        FirebaseApp.initializeApp(this);
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            //EditText editText3=(EditText)findViewById(R.id.editText3);
            EditText editText6=(EditText)findViewById(R.id.editText6);
            editText3.setText(user.getEmail());
        }
        TextView textView15=(TextView)findViewById(R.id.textView15);
        textView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);
            }
        });
        Button btn1=(Button)findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email,password;
                EditText editText3=(EditText)findViewById(R.id.editText3);
                EditText editText6=(EditText)findViewById(R.id.editText6);
                email=editText3.getText().toString();
                password=editText6.getText().toString();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


}
