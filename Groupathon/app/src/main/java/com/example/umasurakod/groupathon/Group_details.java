package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.content.Intent;

public class Group_details extends AppCompatActivity {

    TextView grpDetail;
    ListView listItems;
    Toolbar groupName;
    String grpName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details);

        grpDetail = (TextView)findViewById(R.id.detail_text);
        grpDetail.setText(getIntent().getStringExtra("GrpDesc"));
        grpName=getIntent().getStringExtra("GrpName");
        setTitle(grpName);
        //  groupName = (Toolbar)findViewById(R.id.toolbar);
        // groupName.setTitle(getIntent().getStringExtra("grpName"));

        listItems = (ListView)findViewById(R.id.list_items);
        final String[] mem_Item = {"Group member", "Item Checklist"};
         ListAdapter myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mem_Item);
         listItems.setAdapter(myAdapter);


        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    if(position==0) {
                        Intent intent = new Intent(Group_details.this, Group_members.class);
                        intent.putExtra("GrpName", grpName);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(Group_details.this, Group_itemchecklist.class);
                        intent.putExtra("GrpName", grpName);
                        startActivity(intent);
                    }
                    }
        });
    }
}
