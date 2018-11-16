package com.example.umasurakod.groupathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

public class Group_details extends AppCompatActivity {

    TextView grpDetail;
    ListView listItems;
    Toolbar groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details);

        grpDetail = (TextView)findViewById(R.id.detail_text);
        grpDetail.setText(getIntent().getStringExtra("GrpDetails"));
        //  groupName = (Toolbar)findViewById(R.id.toolbar);
        // groupName.setTitle(getIntent().getStringExtra("grpName"));

        listItems = (ListView)findViewById(R.id.list_items);
        String[] mem_Item = {"Group member", "Item Checklist"};
        ListAdapter myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mem_Item);
        listItems.setAdapter(myAdapter);
    }
}
