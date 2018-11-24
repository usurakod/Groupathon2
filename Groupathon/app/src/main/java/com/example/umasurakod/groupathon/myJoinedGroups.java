package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class myJoinedGroups extends AppCompatActivity {
        ListView myJoinedList;
        DatabaseReference myJoined;
        String grpname;
        String grpdesc;
        ArrayList<String> Groups;
        ArrayList<String> Desc;
        ArrayList<Integer> images;
        ArrayList<String> Dates;
        ArrayList<String> locs;
        String grpeventdate,grpeventloc;


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.my_joined_groups);

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userid = user.getUid();
            myJoinedList = (ListView) findViewById(R.id.myJoinedGroupList);
            Groups = new ArrayList<String>();
            Desc = new ArrayList<String>();
            images = new ArrayList<Integer>();
            Dates=new ArrayList<String>();
            locs=new ArrayList<String>();

            final joinedAdapter adapter = new joinedAdapter(this, Groups, Desc, images);
            myJoinedList.setAdapter(adapter);

            myJoinedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Intent intent = new Intent(getApplicationContext(), Group_details.class);
                    intent.putExtra("GrpName",Groups.get(position));
                    intent.putExtra("GrpDetails",Desc.get(position));
                    intent.putExtra("Eventdate",Dates.get(position));
                    intent.putExtra("Eventlocation",locs.get(position));
                    startActivity(intent);

                }
            });

            myJoined = FirebaseDatabase.getInstance().getReference().child("myJoinGroups").child(userid);

            myJoined.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //if(dataSnapshot.child("Name").getValue(String.class)!=null || dataSnapshot.child("Details").getValue(String.class)!=null) {
                    grpname = dataSnapshot.child("Name").getValue(String.class);
                    grpdesc = dataSnapshot.child("Details").getValue(String.class);
                    grpeventdate=dataSnapshot.child("Event Date").getValue(String.class);
                    grpeventloc=dataSnapshot.child("Location").getValue(String.class);
                    Groups.add(grpname);
                    Desc.add(grpdesc);
                    Dates.add(grpeventdate);
                    locs.add(grpeventloc);
                    images.add(R.drawable.download1);
                    adapter.notifyDataSetChanged();
                    // }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    class joinedAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<Integer> images;
        ArrayList<String> titleArray;
        ArrayList<String> descriptionArray;

        public joinedAdapter(Activity activity, ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<Integer> imgs) {
            super(activity, R.layout.singlerow_listall, R.id.textView2, titles);
            this.context = activity;
            this.titleArray = titles;
            this.descriptionArray = descriptions;
            this.images = imgs;
        }
    }


