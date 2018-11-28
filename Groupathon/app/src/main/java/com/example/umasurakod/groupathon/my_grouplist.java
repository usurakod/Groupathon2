package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
    DatabaseReference usergroupref;
    String grpname,grpdesc,grpeventdate,grpeventloc;
    ArrayList<String> Groups;
    ArrayList<String>  Desc;
    ArrayList<Integer> images;
    ArrayList<String> Dates;
    ArrayList<String> locs;


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
        Dates=new ArrayList<String>();
        locs=new ArrayList<String>();
        final Adapter adapter = new Adapter(this, Groups, locs, images);
        myList.setAdapter(adapter);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        usergroupref = FirebaseDatabase.getInstance().getReference().child("Usergroups").child(userid);

        usergroupref.addChildEventListener(new ChildEventListener() {
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
                    images.add(R.drawable.musicnew);
                    images.add(R.drawable.hackingnew);
                    images.add(R.drawable.hickingnew);
                    images.add(R.drawable.sportsnew);
                    images.add(R.drawable.photonew);
                    images.add(R.drawable.othernew);
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



class Adapter extends BaseAdapter {
    Context context;
    ArrayList Item;
    ArrayList SubItem;
    ArrayList flags;
    LayoutInflater inflter;

    public Adapter(Context applicationContext, ArrayList<String> Item, ArrayList<String> SubItem , ArrayList<Integer> flags) {
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


