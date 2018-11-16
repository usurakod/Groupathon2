package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class create_group extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button create;
    EditText details;
    EditText grpName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        details = (EditText)findViewById(R.id.detail_text);
        grpName=(EditText) findViewById(R.id.name_text);
        create = (Button)findViewById(R.id.create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTocreatedPg();
            }
        });
    }
    public void goTocreatedPg(){
        String getDetails = details.getText().toString();
        String getName=grpName.getText().toString();
        Intent intent = new Intent(this,Group_details.class);
        intent.putExtra("GrpDetails",getDetails);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
