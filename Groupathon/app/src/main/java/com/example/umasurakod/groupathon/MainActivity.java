package com.example.umasurakod.groupathon;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umasurakod.groupathon.AccountActivity.LoginActivity;
import com.example.umasurakod.groupathon.AccountActivity.SettingActivity;
import com.example.umasurakod.groupathon.AccountActivity.SignupActivity;
import com.example.umasurakod.groupathon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ImageView music;
    private ImageView hacking;
    private ImageView hiking;
    private ImageView sports;
    private ImageView photography;
    private ImageView other;
    private FirebaseAuth auth;
    private NavigationView navigationView;
    private ListView events;
    private static final int SELECT_PHOTO = 100;
    private ImageView chooseImage;
    String titles;
    String descriptions;
    //int[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8};
    private DatabaseReference groupathonGrpDetails;
    private ArrayList<String> latestGroupNames ;
    private ArrayList<String> latestGroupDescriptions;
    private ArrayList<Integer> images;
    private String currentUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Groupathon");
        //setContentView(R.layout.navigation_header);
        //Resources res = getResources();
        //titles=res.getStringArray(R.array.titles);
        //descriptions = res.getStringArray(R.array.description);

        latestGroupNames = new ArrayList<>();
        latestGroupDescriptions = new ArrayList<>();
        images = new ArrayList<>();
//        events.setAdapter(adapter);
        navigationView = findViewById(R.id.nav_view);
        View navHeader=navigationView.getHeaderView(0);

        chooseImage=navHeader.findViewById(R.id.chooseImage);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                int id = item.getItemId();


                if (id == R.id.Logout) {
                    signOut();

                } else if (id == R.id.mygroups) {
                    Intent intent = new Intent(getApplicationContext(), my_grouplist.class);
                    startActivity(intent);

                } else if (id == R.id.Notifications) {


                } else if (id == R.id.Settings) {
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                }
                return true;

            }
        });
        myDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);
        myDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();


        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        music = (findViewById(R.id.music_img));
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        hacking = findViewById(R.id.hacking_img);
        hacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        hiking = findViewById(R.id.hiking_img);
        hiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sports = findViewById(R.id.sports_img);
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        photography = findViewById(R.id.photography_img);
        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        other = findViewById(R.id.other_img);
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOncreateGrp();

            }
        });


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        currentUID = user.getUid();

        //groupathonGrpDetails = FirebaseDatabase.getInstance().getReference().child("users").child(currentUID);
        groupathonGrpDetails = FirebaseDatabase.getInstance().getReference().child("Groups");
        events = findViewById(R.id.events);

        //ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,R.layout.singlerow_listall,latestGroups);
        final SushAdapter adapter = new SushAdapter(this, latestGroupNames,latestGroupDescriptions,images);
        events.setAdapter(adapter);
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Intent intent = new Intent(getApplicationContext(), join_group.class);
                intent.putExtra("GrpName", latestGroupNames.get(position));
                intent.putExtra("GrpDesc", latestGroupDescriptions.get(position));
                startActivity(intent);
            }
        });



        groupathonGrpDetails.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("Name").getValue(String.class)!=null || dataSnapshot.child("Details").getValue(String.class)!=null) {
                    titles = dataSnapshot.child("Name").getValue(String.class);
                    descriptions = dataSnapshot.child("Details").getValue(String.class);
                    latestGroupNames.add(titles);
                    latestGroupDescriptions.add(descriptions);
                    images.add(R.drawable.download1);
                    images.add(R.drawable.download2);

                    adapter.notifyDataSetChanged();
                }
            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        groupathonGrpDetails.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                titles = dataSnapshot.child("Details").getValue(String.class);
//                latestGroups.add(titles);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



    }

    public void signOut() {
        auth.signOut();
//        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    finish();
//                }
//            }
//        };
    }// *****TO-DO this authListener to be in Settings page as well********

    public void openOncreateGrp() {
        Intent intent = new Intent(this, create_group.class);
        startActivity(intent);
    }
    // to select item from action bar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {

                //setDataToView(user);

            }
        }


    };


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }

    }

    static class SushAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<Integer> images;
        ArrayList<String> titlesArrayList;
        ArrayList<String> descriptionArray;

        SushAdapter(Context c, ArrayList<String> groupTitles,ArrayList<String> groupDescriptions, ArrayList<Integer> imgs) {
            super(c, R.layout.singlerow_listall, R.id.textView2,groupTitles);
            this.context = c;
            this.titlesArrayList = groupTitles;
            this.descriptionArray = groupDescriptions;
            this.images = imgs;
        }
    }
    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        chooseImage.setImageURI(selectedImage);
                    }
                }
        }
    }

    public void ChooseImage(View v){
        openGallery();
    }



}







