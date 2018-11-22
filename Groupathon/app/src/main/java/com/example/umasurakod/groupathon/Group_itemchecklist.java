package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class Group_itemchecklist extends AppCompatActivity {

    ListView list;
    EditText editText;
    Button addButton;
    String grpName;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;
    DatabaseReference itemchecklist;
    String name;
    String itemid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_checklistview);
        editText = (EditText) findViewById(R.id.editText);
        addButton = (Button) findViewById(R.id.addItem);
        listItems = new ArrayList<String>();
        grpName=getIntent().getStringExtra("GrpName");

        adapter = new Adapter(this, listItems);
        list = findViewById(R.id.list_view);
        list.setAdapter(adapter);
        itemchecklist = FirebaseDatabase.getInstance().getReference().child("GroupItemchecklist").child(grpName);


        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(editText.getText().toString().length()>0) {
                    String itemname=editText.getText().toString();
                    //listItems.add(itemname);
                    editText.getText().clear();

                    itemid=itemchecklist.push().getKey();
                    HashMap<String,String> itemMap = new HashMap<>();
                    itemMap.put("Item",itemname);

                    itemchecklist.child(itemid).setValue(itemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Group_itemchecklist.this, "Item added Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Group_itemchecklist.this,"Could not add Item, please contact the customer care",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }
        });

        itemchecklist.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("Item").getValue(String.class)!=null ) {

                name = dataSnapshot.child("Item").getValue(String.class);

                listItems.add(name);
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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


            }
        });
    }

    class Adapter extends ArrayAdapter<String> {
        private final Activity activity;
        private final ArrayList<String> item;

        public Adapter(Activity activity, ArrayList<String> item) {
            super(activity, R.layout.group_itemchecklist, item);
            this.activity = activity;
            this.item = item;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.group_itemchecklist, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            txtTitle.setText(listItems.get(position));
            return rowView;

        }
    }
}
