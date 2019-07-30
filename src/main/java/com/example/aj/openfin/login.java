package com.example.aj.openfin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Toast.makeText(getApplicationContext(),"Already Signed In",Toast.LENGTH_SHORT).show();
            windup(firebaseUser);
        }

    }

    public void signin(View view)
    {
        if(!isInternetAvailable())
        {
            Snackbar.make(getWindow().getDecorView(),"Internet not Connected",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
            return;
        }
        final EditText emailedit=(EditText)findViewById(R.id.emailedittext);
        final String email=emailedit.getText().toString();
        final EditText passedit = (EditText)findViewById(R.id.passedittext);
        final String pass=passedit.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Snackbar.make(getWindow().getDecorView(),"Enter Valid Email ID",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
            return;
        }
        if(pass.length()<5)
        {
            Snackbar.make(getWindow().getDecorView(),"Password too short",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
            return;
        }


        final ProgressDialog progressBar = new ProgressDialog(login.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Connecting...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                                progressBar.dismiss();
                                windup(user);


                        } else {
                            progressBar.dismiss();
                            Snackbar.make(getWindow().getDecorView(),"Account doesn't exist",Snackbar.LENGTH_LONG).setAction("SignUp", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressBar.show();
                                    mAuth.createUserWithEmailAndPassword(email, pass)
                                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        progressBar.dismiss();
                                                        windup(user);

                                                    } else {
                                                        progressBar.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Authentication Problem", Toast.LENGTH_SHORT).show();
                                                    }

                                                    // ...
                                                }
                                            });

                                }
                            }).show();

                        }


                    }
                });
    }


    public void newstart(final FirebaseUser user1)
    {
        final ProgressDialog progressBar = new ProgressDialog(login.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Connecting...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);

        findViewById(R.id.linearLayout3).setVisibility(View.GONE);
        findViewById(R.id.button).setVisibility(View.GONE);
        findViewById(R.id.logintitle).setVisibility(View.GONE);
        findViewById(R.id.inly).setVisibility(View.VISIBLE);
        final RadioButton accountselect=(RadioButton)findViewById(R.id.accountcreateradio);
        final RadioButton subscribeselect=(RadioButton)findViewById(R.id.radioButton2);
        final EditText acname=(EditText)findViewById(R.id.acnameedittext);
        acname.setVisibility(View.INVISIBLE);
        final EditText uname=(EditText)findViewById(R.id.usernameedittext);
        uname.setVisibility(View.INVISIBLE);
        final EditText code=(EditText)findViewById(R.id.accountcodeedittext);
        code.setVisibility(View.INVISIBLE);
        final Button start=(Button) findViewById(R.id.startbutton);


        accountselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    subscribeselect.setChecked(false);
                    acname.setVisibility(View.VISIBLE);
                    uname.setVisibility(View.VISIBLE);
                    code.setVisibility(View.INVISIBLE);

                }

            }
        });

        subscribeselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    code.setVisibility(View.VISIBLE);
                    acname.setVisibility(View.INVISIBLE);
                    uname.setVisibility(View.INVISIBLE);
                    accountselect.setChecked(false);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInternetAvailable())
                {
                    Snackbar.make(getWindow().getDecorView(),"Internet not Connected",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                    return;
                }

                if(accountselect.isChecked())
                {
                    final String user=uname.getText().toString();
                    final String ac=acname.getText().toString();
                    if(ac.length()<1)
                    {
                        Snackbar.make(getWindow().getDecorView(),getResources().getString(R.string.accountnameedittextviewhint),Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                        return;
                    }
                    if(user.length()<1)
                    {
                        Snackbar.make(getWindow().getDecorView(),getResources().getString(R.string.usernameedittexthint),Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                        return;
                    }
                    progressBar.show();
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("id");
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists())
                            {
                                final String list = dataSnapshot.getValue().toString();
                                Random random=new Random();
                                int a=0;
                                String b="0000000000";
                                while(list.contains(b))
                                {
                                    a=random.nextInt();
                                    Toast.makeText(getApplicationContext(),String.valueOf(a),Toast.LENGTH_SHORT).show();
                                    b=String.valueOf(a);
                                    while(b.length()<10)
                                    {
                                        b="0"+b;
                                    }
                                }
                                database.setValue(list+":"+b);
                                FirebaseDatabase.getInstance().getReference(b).child("acname").setValue(ac);
                                FirebaseDatabase.getInstance().getReference(b).child("uname").setValue(user);
                                FirebaseDatabase.getInstance().getReference(b).child("balance").setValue(0);
                                FirebaseDatabase.getInstance().getReference(b).child("transaction_count").setValue(0);
                                FirebaseDatabase.getInstance().getReference("users").child(user1.getUid()).child("dataline").setValue("W"+b+":").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.dismiss();
                                        windup(user1);
                                    }
                                });

                            }
                            else
                            {
                                progressBar.dismiss();
                                Snackbar.make(getWindow().getDecorView(),"Database Error",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    final String rid = code.getText().toString();
                    if(rid.length()<1)
                    {
                        Snackbar.make(getWindow().getDecorView(),"Enter Code",Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                        return;
                    }
                    progressBar.show();
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("id");
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String cpy = rid;
                            if(dataSnapshot.exists()) {
                                String list=dataSnapshot.getValue().toString();

                                while (cpy.length() != 10) {
                                    cpy = "0" + cpy;
                                }
                                if(list.contains(cpy)) {

                                    FirebaseDatabase.getInstance().getReference("users").child(user1.getUid()).child("dataline").setValue("R"+cpy+":").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.dismiss();
                                            windup(user1);
                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Code Not Found",Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.dismiss();

                        }
                    });



                }
            }
        });
    }


    public void windup(final FirebaseUser firebaseUser)
    {
        final ProgressDialog progressBar = new ProgressDialog(login.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Connecting...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("dataline").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())
                {
                    progressBar.dismiss();
                    newstart(firebaseUser);
                }
                else
                {
                    String data=dataSnapshot.getValue().toString();
                    progressBar.setMessage("Getting data..");
                    String[] abc=data.split(":");
                    String[] rid=new String[50];
                    String[] wid=new String[50];
                    int i=0,j=0,k=0;
                    for(i=0;i<abc.length;i++)
                    {
                        if(abc[i].contains("W"))
                        {
                            wid[j]=abc[i];
                            j++;
                        }
                        else if(abc[i].contains("R"))
                        {
                            rid[k]=abc[i];
                            k++;
                        }
                    }
                    editor.putInt("ridcount",k);
                    editor.putInt("widcount",j);

                    for(i=0;i<k;i++)
                    {
                        editor.putString("rid"+(i+1),rid[i].replace("R",""));
                    }
                    for(i=0;i<j;i++)
                    {
                        editor.putString("wid"+(i+1),wid[i].replace("W",""));
                    }
                    editor.putString("uid",firebaseUser.getUid());
                    editor.putBoolean("firstrun",false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something went wrong..",Toast.LENGTH_SHORT).show();
            }
        });
    }




    boolean isInternetAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
