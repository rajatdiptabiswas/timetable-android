package com.example.kalyan.timetable;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Poll extends AppCompatActivity {
    EditText polltitle;
    EditText groupname;
    EditText username;
    Button startbutton;
    Button endbutton;
    Button submit;
    Button result;
    Integer flag2=0;
    Integer flag1=0,poll;
    String USER;
    private FirebaseDatabase database;
    private FirebaseStorage mfirebaseStorage;
    private DatabaseReference mdatabasereference;
    private DatabaseReference mdatabasereference1;
    private LinkedList<Map<String, Object>> mUserStatuses = new LinkedList<>();
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        FirebaseApp.initializeApp(this);
        polltitle=(EditText)findViewById(R.id.polltitle);
        groupname=(EditText)findViewById(R.id.groupname);
        username=(EditText)findViewById(R.id.username);
        startbutton=(Button) findViewById(R.id.startpoll);
        endbutton=(Button)findViewById(R.id.endpoll);
        submit=(Button)findViewById(R.id.submit);
        result=(Button)findViewById(R.id.result);

        flag2=0;
        fab = (FloatingActionButton) findViewById(R.id.fabpoll);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().length()>0&&groupname.getText().toString().length()>0){
                    flag2=1;
                    Log.e("submit on click","submit on click");
                    loadPreviousStatuses(groupname.getText().toString());
                    Log.e("submit on click","submit on click");
                    Toast.makeText(Poll.this,"Submitted Successfully",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Poll.this,"Enetr the credential",Toast.LENGTH_SHORT).show();
            }
        });

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag2==1){

                if(flag1==0) {
                    if (polltitle.getText().toString().length()>0 && flag2==1) {
                        //  mFirebaseTransportRef = FirebaseDatabase.getInstance().getReference(path);

                        database = FirebaseDatabase.getInstance();
                        //mfirebaseStorage=FirebaseStorage.getInstance();
                        mdatabasereference = database.getReference().child(groupname.getText().toString());
                        //  DatabaseReference ref = database.getReference("groupname");
                        User user = new User(username.getText().toString(), "0", "1");
                        flag1=1;
                        mdatabasereference.setValue(user);
                        //DatabaseReference usersRef = ref.child("user");
                        //ref.child("user2").setValue(new User("June 23, 1912", "Alan Turing","xx"));
                  /* Map<String, User> users = new HashMap<>();
                    users.put("alanisawesome", new User("user", "0","1"));
                    usersRef.setValue(users);*/
                        Toast.makeText(Poll.this, "Polling Started", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(Poll.this, "Submit your Details", LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Poll.this,"Polling is in process",Toast.LENGTH_SHORT).show();
            }
            else
                    Toast.makeText(Poll.this, "Submit your Details", LENGTH_SHORT).show();
            }
        });

        endbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag2==1){
                
              if(flag1==1){
                  if(USER.equals(username.getText().toString())){
                      database = FirebaseDatabase.getInstance();
                      //mfirebaseStorage=FirebaseStorage.getInstance();
                      mdatabasereference = database.getReference(groupname.getText().toString());
                      //  DatabaseReference ref = database.getReference("groupname");
                      User user = new User(USER, "0", "0");
                      flag1=0;
                      mdatabasereference.setValue(user);
                      Toast.makeText(Poll.this,"Polling is stopped",Toast.LENGTH_SHORT).show();
                  }
                  else
                      Toast.makeText(Poll.this,"You cannot stop",Toast.LENGTH_SHORT).show();
              }
              else
              Toast.makeText(Poll.this,"Polling is not stated yet",Toast.LENGTH_SHORT).show();

            }
            else
                    Toast.makeText(Poll.this, "Submit your Details", LENGTH_SHORT).show();
            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag2==1){
                    loadPreviousStatuses(groupname.getText().toString());
                    if(flag1==1){
                        Toast.makeText(Poll.this,"Present Result (Polling In Process) "+ poll.toString(),Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(Poll.this,"Previous Polling Result "+poll.toString(),Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Poll.this, "Submit your Details", LENGTH_SHORT).show();
            }


        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag1 == 1) {
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Poll.this);
                    a_builder.setMessage(polltitle.getText().toString())
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database = FirebaseDatabase.getInstance();
                                    //mfirebaseStorage=FirebaseStorage.getInstance();
                                    mdatabasereference = database.getReference(groupname.getText().toString());
                                    //  DatabaseReference ref = database.getReference("groupname");
                                    poll++;
                                    User user = new User(USER, poll.toString(), flag1.toString());
                                    mdatabasereference.setValue(user);
                                    Toast.makeText(Poll.this, "Your Poll has been counted", LENGTH_SHORT).show();
                                    Intent intent = new Intent(Poll.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    System.exit(0);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(Poll.this, "Your Poll has been counted", LENGTH_SHORT).show();
                                    Intent intent = new Intent(Poll.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.show();
                }
else
                {
                    Toast.makeText(Poll.this,"Polling is not started yet",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPreviousStatuses(final String group) {
        database = FirebaseDatabase.getInstance();
        //mfirebaseStorage=FirebaseStorage.getInstance();
        mdatabasereference1 = database.getReference();

        Log.e("beforeFirebase", "beforeFirebase");
        mdatabasereference = database.getReference(group);

        mdatabasereference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if ((group != "") && snapshot.hasChild(group)) {
                    mdatabasereference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.e("insidedatachange", "insidedatachange");
                            if (snapshot != null) {
                                User post = snapshot.getValue(User.class);
                                Log.e("insideforloop", "insidedforloop");
                                Log.e("insidedatachange", "insidedatachange");
                                USER=post.username;
                                poll=Integer.valueOf(post.polls);//post.polls;
                                flag1=Integer.valueOf(post.flag);
                                Log.e("insidedatachange", "belowLAT");
                                Log.e("insidedatachange", "BelowUSERNAME");


                            } else {
                                Log.e("insideelse", "insidedelse");

                                Toast.makeText(Poll.this, "Polling in "+group + " has not started", Toast.LENGTH_SHORT).show();


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // TODO: Handle gracefully
                        }
                    });
                } else {
                    flag2 = 0;
                    Toast.makeText(Poll.this, "Polling in "+group + " has not started", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    }
