package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.umasurakod.groupathon.AccountActivity.SettingActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Category_groups extends AppCompatActivity {
    DatabaseReference myCategory;
    ListView list;
    String name;
    String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_general);


        final ArrayList<String> latestGroupcategoryNames;
        final ArrayList<String> latestGroupcategoryDescriptions;
        final ArrayList<Integer> images;

        latestGroupcategoryNames = new ArrayList<String>();
        latestGroupcategoryDescriptions=new ArrayList<String>();
        images = new ArrayList<Integer>();
        String category=getIntent().getStringExtra("category");
        setTitle(category);

        myCategory = FirebaseDatabase.getInstance().getReference().child("Categories").child(category);

        final CAdapter adapter =new CAdapter(this, latestGroupcategoryNames, images);
        list = findViewById(R.id.list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getApplicationContext(), Group_details.class);
                intent.putExtra("GrpName",latestGroupcategoryNames.get(position));
                intent.putExtra("GrpDetails",latestGroupcategoryDescriptions.get(position));
                startActivity(intent);

            }
        });

        myCategory.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*if(dataSnapshot.child("User").getValue(String.class)!=null ) {*/

                name = dataSnapshot.child("Name").getValue(String.class);
                description = dataSnapshot.child("Details").getValue(String.class);
                latestGroupcategoryNames.add(name);
                latestGroupcategoryDescriptions.add(description);
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

        class CAdapter extends ArrayAdapter<String> {
            private final Activity activity;
            private final ArrayList<String> groupname;
            private final ArrayList<Integer> image;

            public CAdapter(Activity activity, ArrayList<String> groupname, ArrayList<Integer> image) {
                super(activity, R.layout.category_groups, groupname);
                this.activity = activity;
                this.groupname = groupname;
                this.image = image;
            }

            public View getView(int position, View view, ViewGroup parent) {
                LayoutInflater inflater = activity.getLayoutInflater();
                View rowView = inflater.inflate(R.layout.category_groups, null, true);

                TextView group_name = (TextView) rowView.findViewById(R.id.groupName);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.group_icon);

                group_name.setText(groupname.get(position));
                imageView.setImageResource(image.get(position));
                return rowView;

            }
        }
    }

