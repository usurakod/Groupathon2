package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class my_grouplist extends AppCompatActivity {
    ListView myList;
    String[] titles;
    String[] descriptions;
    DatabaseReference usergroupref;
    String grpname;
    String grpdesc;
    ArrayList<String> Groups;
    ArrayList<String>  Desc;
    ArrayList<Integer> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_grouplist);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getUid();
        myList = (ListView)findViewById(R.id.myGroupList);
        Groups=new ArrayList<String>();
        Desc=new ArrayList<String>();
        images=new ArrayList<Integer>();
        Resources res = getResources();
        titles=res.getStringArray(R.array.titles);
        descriptions = res.getStringArray(R.array.description);
        final Adapter adapter = new Adapter(this, Groups, Desc, images);
        myList.setAdapter(adapter);

        usergroupref = FirebaseDatabase.getInstance().getReference().child("Usergroups").child(userid);

        usergroupref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //if(dataSnapshot.child("Name").getValue(String.class)!=null || dataSnapshot.child("Details").getValue(String.class)!=null) {
                    grpname = dataSnapshot.child("Name").getValue(String.class);
                    grpdesc = dataSnapshot.child("Details").getValue(String.class);
                    Groups.add(grpname);
                    Desc.add(grpdesc);
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

    class Adapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<Integer> images;
        ArrayList<String> titleArray;
        ArrayList<String> descriptionArray;

        public Adapter(Activity activity, ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<Integer> imgs) {
            super(activity, R.layout.singlerow_listall, R.id.textView2, titles);
            this.context = activity;
            this.titleArray = titles;
            this.descriptionArray = descriptions;
            this.images = imgs;
        }
    }


