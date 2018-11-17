package com.example.umasurakod.groupathon;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.content.Intent;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
    String[] titles;
    String[] descriptions;
    int[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Groupathon");
        //setContentView(R.layout.navigation_header);
        Resources res = getResources();
        titles=res.getStringArray(R.array.titles);
        descriptions = res.getStringArray(R.array.description);
        events = findViewById(R.id.events);
        SushAdapter adapter = new SushAdapter(this, titles, descriptions, images);
        events.setAdapter(adapter);
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
        });//Sush--not working(implement later)
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

    class SushAdapter extends ArrayAdapter<String> {
        Context context;
        int[] images;
        String[] titleArray;
        String[] descriptionArray;

        SushAdapter(Context c, String[] titles, String[] descriptions, int[] imgs) {
            super(c, R.layout.singlerow_listall, R.id.textView2, titles);
            this.context = c;
            this.titleArray = titles;
            this.descriptionArray = descriptions;
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







