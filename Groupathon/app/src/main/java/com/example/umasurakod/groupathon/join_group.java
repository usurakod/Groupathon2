package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.util.Strings;
import android.content.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
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
    private DatabaseReference Db_Reference_Notification;
    private DatabaseReference Db_Reference_GroupMembers;
    private String Notification="Notification";
    private String NotificationId;
    private String currentUID;
    private ArrayList<Strings> GroupMembers_InGroup=new ArrayList<>();
    private ArrayList<String>  GroupMember_Names ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //for notification
        currentUID = user.getUid(); //get it from prev intent
        // Db_Reference_Notification = FirebaseDatabase.getInstance().getReference().child(Notification).child(currentUID);
        Db_Reference_Notification = FirebaseDatabase.getInstance().getReference().child(Notification);
        NotificationId= Db_Reference_Notification.push().getKey();
        Eventdate = (TextView)findViewById(R.id.Date);
        Eventlocation = (TextView)findViewById(R.id.Location);
        grpDetail = (TextView)findViewById(R.id.detail_text);
        join=(Button)findViewById(R.id.join_button);
        grpName=getIntent().getStringExtra("GrpName");
        Db_Reference_GroupMembers=FirebaseDatabase.getInstance().getReference().child("Groupmembers").child(grpName);

        grpDesc=getIntent().getStringExtra("GrpDesc");
        eventdate=getIntent().getStringExtra("Eventdate");
        eventloc=getIntent().getStringExtra("Eventlocation");
        Eventdate.setText(eventdate);
        Eventlocation.setText(eventloc);

        setTitle(grpName);
        grpDetail.setText(grpDesc);
        email = user.getEmail();
        myName = user.getDisplayName();

        listItems = (ListView)findViewById(R.id.list_items);

        final String[] mem_Item = {"Group member"};
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
                userMap.put("Eventdate",eventdate);
                userMap.put("Location",eventloc);

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
//Notify other members in the group joined in main method

                Db_Reference_GroupMembers.addChildEventListener(new ChildEventListener(){

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(dataSnapshot.child("User Name").getValue(String.class)!=null) {
                            //Store emails or username in arrayList
                            String Existing_UName = dataSnapshot.child("User Name").getValue(String.class);
                           if(Existing_UName==user.getDisplayName()){
                           //if fetched using uid can we map name and uid in DB? how to map name and UID for all members?

                                    //Dont add to firebase
                            }else {
                                //Notify this user that current user has been added to the group -->Enter new entry to Notification DB

                                HashMap<String, String> notify_map = new HashMap<>();
                                notify_map.put("Notification_MSG", "New Member \"" + user.getDisplayName() + "\" joined group " + grpName);//replace with current username

                                //Hard coded UUID/Username
                                Db_Reference_Notification.child(Existing_UName).child(NotificationId).setValue(notify_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }


                            //   GroupMembers_InGroup.add((Strings) dataSnapshot.child("User").getValue());
                            Log.d("GroupMembers_InGroup=",Existing_UName);
                        }

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
                //Fetch the group members id and add entry to them in Notify table


                // intent.putExtra("Grpid", groupid);



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

            @Override
            public boolean onCreateOptionsMenu(Menu menu){
                getMenuInflater().inflate(R.menu.home,menu);
                return true;
            }
            @Override
            public boolean onOptionsItemSelected(MenuItem item){
                if (item.getItemId() == R.id.Home){
                    startActivity(new Intent(this,MainActivity.class));
                    return true;
                }
                return super.onOptionsItemSelected(item);
            }


}
