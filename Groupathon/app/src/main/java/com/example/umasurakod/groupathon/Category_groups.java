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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Category_groups extends AppCompatActivity {
    DatabaseReference myCategory;
    ListView list;
    String name,date,location;
    String description;
    private FirebaseUser user;
    private DatabaseReference memberDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_general);


        final ArrayList<String> latestGroupcategoryNames;
        final ArrayList<String> latestGroupcategoryDescriptions;
        final ArrayList<String> latestGrouplocations;
        final ArrayList<String> latestGroupEventdates;
        final ArrayList<Integer> images;

        latestGroupcategoryNames = new ArrayList<String>();
        latestGroupcategoryDescriptions = new ArrayList<String>();
        latestGroupEventdates = new ArrayList<String>();
        latestGrouplocations = new ArrayList<String>();
        images = new ArrayList<Integer>();
        String category = getIntent().getStringExtra("category");
        setTitle(category);

        user = FirebaseAuth.getInstance().getCurrentUser();

        myCategory = FirebaseDatabase.getInstance().getReference().child("Categories").child(category);

        final CAdapter adapter = new CAdapter(this, latestGroupcategoryNames, images);
        list = findViewById(R.id.list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                String grpName = (String) adapterView.getItemAtPosition(position);
                memberDetails = FirebaseDatabase.getInstance().getReferenceFromUrl("https://groupathon.firebaseio.com/Groupmembers/" + grpName);
                memberDetails.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot

                                boolean exists = collectEmailIds((Map<String, Object>) dataSnapshot.getValue());
                                if (!exists) {
                                    Intent intent = new Intent(getApplicationContext(), join_group.class);
                                    intent.putExtra("GrpName", latestGroupcategoryNames.get(position));
                                    intent.putExtra("GrpDesc", latestGroupcategoryDescriptions.get(position));
                                    intent.putExtra("Eventdate", latestGroupEventdates.get(position));
                                    intent.putExtra("Eventlocation", latestGrouplocations.get(position));
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Group_details.class);
                                    intent.putExtra("GrpName", latestGroupcategoryNames.get(position));
                                    intent.putExtra("GrpDetails", latestGroupcategoryDescriptions.get(position));
                                    intent.putExtra("Eventdate", latestGroupEventdates.get(position));
                                    intent.putExtra("Eventlocation", latestGrouplocations.get(position));
                                    // intent.putExtra("Grpid", groupid);
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });

            }
        });

        myCategory.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*if(dataSnapshot.child("User").getValue(String.class)!=null ) {*/

                name = dataSnapshot.child("Name").getValue(String.class);
                description = dataSnapshot.child("Details").getValue(String.class);
                date = dataSnapshot.child("Event Date").getValue(String.class);
                location = dataSnapshot.child("Location").getValue(String.class);
                latestGroupcategoryNames.add(name);
                latestGroupcategoryDescriptions.add(description);
                latestGroupEventdates.add(date);
                latestGrouplocations.add(location);
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
        private boolean collectEmailIds(Map<String,Object> users) {
            Intent intent = getIntent();
            String email = user.getEmail();
            ArrayList<String> emailIds = new ArrayList<>();


            for (Map.Entry<String, Object> entry : users.entrySet()){


                Map singleUser = (Map) entry.getValue();

                //String useri=(String)singleUser.get(0);
                String userii = (String)singleUser.get("User");
                emailIds.add(userii);
            }

            if(emailIds.contains(user.getEmail())){
                return true;
            }

            return false;
        }
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



