package com.example.umasurakod.groupathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    TextView grpDetail,Eventdate,Eventlocation;
    ListView listItems;
    String grpName;
    String eventdate,eventloc;
    //String grpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details);

        Eventdate = (TextView)findViewById(R.id.Date);
        Eventlocation = (TextView)findViewById(R.id.Location);
        grpDetail = (TextView)findViewById(R.id.detail_text);

        eventdate=getIntent().getStringExtra("Eventdate");
        Eventdate.setText(eventdate);
        eventloc=getIntent().getStringExtra("Eventlocation");
        Eventlocation.setText(eventloc);
        grpDetail.setText(getIntent().getStringExtra("GrpDetails"));
        grpName=getIntent().getStringExtra("GrpName");
        eventdate=getIntent().getStringExtra("Eventdate");
       // grpid=getIntent().getStringExtra("Grpid");
        setTitle(grpName);
        //  groupName = (Toolbar)findViewById(R.id.toolbar);
        // groupName.setTitle(getIntent().getStringExtra("grpName"));

        listItems = (ListView)findViewById(R.id.list_items);
        final String[] mem_Item = {"Group members                                              >>", "Item Checklist                                                 >>"};
         ListAdapter myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mem_Item);
         listItems.setAdapter(myAdapter);


        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    if(position==0) {
                        Intent intent = new Intent(Group_details.this, Group_members.class);
                        intent.putExtra("GrpName", grpName);
                        //intent.putExtra("Grpid",grpid);
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
