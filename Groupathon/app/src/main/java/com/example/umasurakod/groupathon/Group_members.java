package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Group_members extends AppCompatActivity {

    ListView list;
    //Integer[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8,R.drawable.download7, R.drawable.download8,R.drawable.download7, R.drawable.download8,R.drawable.download8};
    DatabaseReference groupmembers;

    String names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_general);
        final ArrayList<String> latestGroupmemNames ;
        final ArrayList<Integer> images ;
        final String groupid=FirebaseAuth.getInstance().getUid();
        latestGroupmemNames=new ArrayList<String>();
        images=new ArrayList<Integer>();
        groupmembers = FirebaseDatabase.getInstance().getReference().child("Groupmembers");

        final Adapter adapter = new Adapter(this, latestGroupmemNames, images);
        list = findViewById(R.id.list_view);



        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
            });


        groupmembers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*if(dataSnapshot.child("User").getValue(String.class)!=null ) {*/

                    names = dataSnapshot.child("User").getValue(String.class);

                    latestGroupmemNames.add(names);
                    images.add(R.drawable.download1);

                    adapter.notifyDataSetChanged();
                /*}*/
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
        }

    class Adapter extends ArrayAdapter<String> {
        private final Activity activity;
        private final ArrayList<String> mem_name;
        private final ArrayList<Integer> image;

        public Adapter(Activity activity, ArrayList<String> mem_name, ArrayList<Integer> image) {
            super(activity, R.layout.group_members, mem_name);
            this.activity = activity;
            this.mem_name = mem_name;
            this.image = image;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.group_members, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            txtTitle.setText(mem_name.get(position));
            imageView.setImageResource(image.get(position));
            return rowView;

        }
    }
}
