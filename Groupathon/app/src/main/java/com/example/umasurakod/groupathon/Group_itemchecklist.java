package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Group_itemchecklist extends AppCompatActivity {

    ListView list;
    CheckBox checkbox;
    String[] itemlist;
    //Integer[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_general);

        Resources res= getResources();
        itemlist=res.getStringArray(R.array.items);
        Adapter adapter = new Adapter(this, itemlist,checkbox);
        list = findViewById(R.id.list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }

    class Adapter extends ArrayAdapter<String> {
        private final Activity activity;
        private final String[] item;
        private final CheckBox checkbox;

        public Adapter(Activity activity, String[] item,CheckBox checkbox) {
            super(activity, R.layout.group_itemchecklist, item);
            this.activity = activity;
            this.item = item;
            this.checkbox=checkbox;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.group_itemchecklist, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            CheckBox checkBox=rowView.findViewById(R.id.checkBox);
            txtTitle.setText(itemlist[position]);
            return rowView;

        }
    }
}
