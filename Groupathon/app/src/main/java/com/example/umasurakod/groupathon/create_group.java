package com.example.umasurakod.groupathon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.jar.Attributes;


public class create_group extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button create;
    EditText details;
    EditText grpName;
    EditText locationName;
    TextView eventdate;
    private DatabaseReference createGroup;
    private DatabaseReference Groups;
    private DatabaseReference categories;
    private DatabaseReference Usergroups;
    private DatabaseReference joinGroup;
    private DatabaseReference userName;
    private String currentUID;
    MainActivity mainactivity=new MainActivity();
    private DatabaseReference createNotification_user;
    private String Notification="Notification";
    private String NotificationId;
    private DatePickerDialog.OnDateSetListener Datelistener;

    String grp_Name,location_Name, category_Name  ;
    String groupid;
    String userid;
    String emailid;
    String event_date;
    String email;
    String currentname;
    String userKey;
    String myName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        locationName = findViewById(R.id.location_text);
        details = findViewById(R.id.detail_text);
        grpName=findViewById(R.id.name_text);
        eventdate=(TextView) findViewById(R.id.datetext);
        create = (Button)findViewById(R.id.create_button);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUID = user.getUid();
        emailid=user.getEmail();
        createGroup = FirebaseDatabase.getInstance().getReference().child("users").child(currentUID);
        userName = FirebaseDatabase.getInstance().getReference().child("users").child(currentUID);
        userKey = createGroup.push().getKey();
        Groups=FirebaseDatabase.getInstance().getReference().child("Groups");
        createNotification_user = FirebaseDatabase.getInstance().getReference().child(Notification).child(user.getDisplayName());
        NotificationId=createNotification_user.push().getKey();

        locationName = findViewById(R.id.location_text);
        details = findViewById(R.id.detail_text);
        grpName=findViewById(R.id.name_text);
        create = (Button)findViewById(R.id.create_button);


        eventdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(create_group.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Datelistener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        Datelistener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date=day+"/"+month+"/"+year;
                eventdate.setText(date);
            }
        };

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (grpName.getText().toString().isEmpty() || eventdate.getText().toString().isEmpty() || locationName.getText().toString().isEmpty() || details.getText().toString().isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(create_group.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please fill all mandatory fields to proceed with group creation");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else if(details.getText().toString().length() < 20) {
                    AlertDialog alertDialog = new AlertDialog.Builder(create_group.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please enter minimum characters for Group Detils field to proceed with group creation");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                    else
                {

                    //String grp_Name = grpName.getText().toString();
                    String grp_Details = details.getText().toString();
                    grp_Name = grpName.getText().toString();
                    location_Name = locationName.getText().toString();
                    category_Name = spinner.getSelectedItem().toString();
                    event_date = eventdate.getText().toString();
                    categories = FirebaseDatabase.getInstance().getReference().child("Categories").child(category_Name);
                    Usergroups = FirebaseDatabase.getInstance().getReference().child("Usergroups").child(currentUID);
                    joinGroup = FirebaseDatabase.getInstance().getReference().child("Groupmembers").child(grp_Name);
                    email = user.getEmail();
                    myName=user.getDisplayName();
                    Toast.makeText(create_group.this, myName, Toast.LENGTH_SHORT).show();




                    HashMap<String, String> grpMap = new HashMap<>();
                    grpMap.put("Name", grp_Name);
                    grpMap.put("Details", grp_Details);
                    grpMap.put("Location", location_Name);
                    grpMap.put("Event Date", event_date);
                    grpMap.put("Category", category_Name);
                    groupid = categories.push().getKey();
                    userid = Usergroups.push().getKey();

                    HashMap<String, String> categorymap = new HashMap<>();
                    categorymap.put("Name", grp_Name);
                    categorymap.put("Location", location_Name);
                    categorymap.put("Details", grp_Details);
                    categorymap.put("Event Date", event_date);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("Name", grp_Name);
                    userMap.put("Location", location_Name);
                    userMap.put("Details", grp_Details);
                    userMap.put("Event Date", event_date);


                    HashMap<String,String> joinMap = new HashMap<>();
                    joinMap.put("User",email);
                    joinMap.put("Details",grp_Details);
                    joinMap.put("User Name",myName);

                    groupid=joinGroup.push().getKey();
                    HashMap<String,String> notify_map = new HashMap<>();
                    notify_map.put("Notification_MSG","New Group \""+grp_Name+"\" created");


                    createNotification_user.child(NotificationId).setValue(notify_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    Usergroups.child(userid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(create_group.this, "Group added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(create_group.this, "Could not add Group, please contact the customer care", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    createGroup.push().setValue(grpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()) {
                                Toast.makeText(create_group.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(create_group.this, "Could not create Group, please contact the customer care", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Groups.push().setValue(grpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(create_group.this, "Group added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(create_group.this, "Could not add Group, please contact the customer care", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    categories.child(groupid).setValue(categorymap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(create_group.this, "Group added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(create_group.this, "Could not add Group, please contact the customer care", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    joinGroup.child(userid).setValue(joinMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(create_group.this, "Joined Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(create_group.this,"Could not join, please contact the customer care",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    goTocreatedPg();
                }
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
    public void goTocreatedPg(){
        String  GrpDesc  = details.getText().toString();
        String GrpName=grpName.getText().toString();
        String Grploc=locationName.getText().toString();
        Intent intent = new Intent(this,Group_details.class);
        intent.putExtra("GrpDetails",GrpDesc);
        intent.putExtra("GrpName",GrpName);
        intent.putExtra("Eventdate",event_date);
        intent.putExtra("Eventlocation",Grploc);
        startActivity(intent);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
