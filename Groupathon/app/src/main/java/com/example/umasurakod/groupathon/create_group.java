package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class create_group extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button create;
    EditText details;
    EditText grpName;
    EditText locationName;
    private DatabaseReference createGroup;
    private String currentUID;


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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUID = user.getUid();
        createGroup = FirebaseDatabase.getInstance().getReference().child("users").child(currentUID);



        locationName = findViewById(R.id.location_text);
        details = findViewById(R.id.detail_text);
        grpName=findViewById(R.id.name_text);
        create = (Button)findViewById(R.id.create_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String grp_Name = grpName.getText().toString();
                String grp_Details = details.getText().toString();
                String location_Name = locationName.getText().toString();
                String category_Name = spinner.getSelectedItem().toString();
                HashMap<String,String> grpMap = new HashMap<>();
                grpMap.put("Name",grp_Name);
                grpMap.put("Details",grp_Details);
                grpMap.put("Location",location_Name);
                grpMap.put("Category",category_Name);

                createGroup.push().setValue(grpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if(task.isSuccessful()){
                            Toast.makeText(create_group.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(create_group.this,"Could not create Group, please conatc the customercare",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                goTocreatedPg();
            }
        });





    }
    public void goTocreatedPg(){
//        String getDetails = details.getText().toString();
//        String getName=grpName.getText().toString();
        Intent intent = new Intent(this,Group_details.class);
//        intent.putExtra("GrpDetails",getDetails);
//        intent.putExtra("GrpName",getName);
        startActivity(intent);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
