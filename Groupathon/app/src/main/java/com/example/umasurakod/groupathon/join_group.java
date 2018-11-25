package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class join_group extends AppCompatActivity {

    TextView grpDetail,Eventdate,Eventlocation;
    String grpDesc;
    ListView listItems;
    String grpName;
    Button join;
    private DatabaseReference joinGroup;
    private DatabaseReference myJoinGroups;
    private String email;
    String groupid,userid;
    String eventdate;
    String eventloc;
    String myName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);
        Eventdate = (TextView)findViewById(R.id.Date);
        Eventlocation = (TextView)findViewById(R.id.Location);
        grpDetail = (TextView)findViewById(R.id.detail_text);
        join=(Button)findViewById(R.id.join_button);
        grpName=getIntent().getStringExtra("GrpName");
        grpDesc=getIntent().getStringExtra("GrpDesc");
        eventdate=getIntent().getStringExtra("Eventdate");
        eventloc=getIntent().getStringExtra("Eventlocation");
        Eventdate.setText(eventdate);
        Eventlocation.setText(eventloc);

        setTitle(grpName);
        grpDetail.setText(grpDesc);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        myName = user.getDisplayName();

        listItems = (ListView)findViewById(R.id.list_items);

        final String[] mem_Item = {"Group member"};
        String currentUID=user.getUid();
        joinGroup=FirebaseDatabase.getInstance().getReference().child("Groupmembers").child(grpName);
        myJoinGroups=FirebaseDatabase.getInstance().getReference().child("myJoinGroups").child(currentUID);

        join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        HashMap<String,String> grpMap = new HashMap<>();
                        grpMap.put("User",email);
                        grpMap.put("Details",grpDesc);
                        grpMap.put("User Name",myName);

                        groupid=joinGroup.push().getKey();
                        HashMap<String,String> userMap = new HashMap<>();
                        userid=myJoinGroups.push().getKey();
                        userMap.put("Name",grpName);
                        userMap.put("Details",grpDesc);

                        myJoinGroups.child(userid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(join_group.this, "Group added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(join_group.this,"Could not add Group, please contact the customer care",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        joinGroup.child(groupid).setValue(grpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if(task.isSuccessful()){
                                    Toast.makeText(join_group.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(join_group.this,"Could not add, please contact the customer care",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        Intent intent = new Intent(getApplicationContext(), Group_details.class);
                        intent.putExtra("GrpName", grpName);
                        intent.putExtra("GrpDetails", grpDesc);
                        intent.putExtra("Eventdate",eventdate);
                        intent.putExtra("Eventlocation",eventloc);
                       // intent.putExtra("Grpid", groupid);
                        startActivity(intent);
                    }
                });
        }
}
