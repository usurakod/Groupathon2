package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Context;
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
import android.widget.BaseAdapter;
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
    ArrayList<String> Groups;
    ArrayList<String> Desc;
    ArrayList<Integer> images;
    ArrayList<String> Dates;
    ArrayList<String> locs;
    String grpeventdate,grpeventloc;
    String grpName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_general);

        Groups = new ArrayList<String>();
        Desc = new ArrayList<String>();
        images = new ArrayList<Integer>();
        Dates=new ArrayList<String>();
        locs=new ArrayList<String>();

        String category = getIntent().getStringExtra("category");
        setTitle(category);

        user = FirebaseAuth.getInstance().getCurrentUser();

        myCategory = FirebaseDatabase.getInstance().getReference().child("Categories").child(category);

        final categoryAdapter myAdapter = new categoryAdapter(this, Groups, locs, images);
        list = findViewById(R.id.list_view);

        list.setAdapter(myAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                TextView textview1 = (TextView) view.findViewById(R.id.textView2);
                String grpName = textview1.getText().toString();
                memberDetails = FirebaseDatabase.getInstance().getReferenceFromUrl("https://groupathon.firebaseio.com/Groupmembers/" + grpName);
                memberDetails.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot

                                boolean exists = collectEmailIds((Map<String, Object>) dataSnapshot.getValue());
                                if (!exists) {
                                    Intent intent = new Intent(getApplicationContext(), join_group.class);
                                    intent.putExtra("GrpName", Groups.get(position));
                                    intent.putExtra("GrpDesc", Desc.get(position));
/*
                                    intent.putExtra("Eventdate", Dates.get(position));
*/
                                    intent.putExtra("Eventlocation", locs.get(position));
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Group_details.class);
                                    intent.putExtra("GrpName", Groups.get(position));
                                    intent.putExtra("GrpDesc", Desc.get(position));
/*
                                    intent.putExtra("Eventdate", Dates.get(position));
*/
                                    intent.putExtra("Eventlocation", locs.get(position));
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
                Groups.add(name);
                Desc.add(description);
                Dates.add(date);
                locs.add(location);
                images.add(R.drawable.download1);
                myAdapter.notifyDataSetChanged();
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

            if(emailIds.contains(email)){
                return true;
            }

            return false;
        }

    }


class categoryAdapter extends BaseAdapter {
    Context context;
    ArrayList Item = new ArrayList<>();
    ArrayList SubItem = new ArrayList<>();
    ArrayList flags= new ArrayList<>();
    LayoutInflater inflter;




    public categoryAdapter(Context applicationContext, ArrayList<String> Item, ArrayList<String> SubItem , ArrayList<Integer> flags) {
        this.context = applicationContext;
        this.Item = Item;
        this.SubItem = SubItem;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Item.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.singlerow_listall, null);
        TextView item = (TextView) view.findViewById(R.id.textView2);
        TextView subitem = (TextView) view.findViewById(R.id.textView3);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        item.setText(Item.get(i).toString());
        subitem.setText(SubItem.get(i).toString());
        image.setImageResource((int)flags.get(i));
        return view;
    }
 }
