package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Group_members extends AppCompatActivity {

    ListView list;
    String[] mem_names;
    Integer[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8,R.drawable.download7, R.drawable.download8,R.drawable.download7, R.drawable.download8,R.drawable.download8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_general);

        Resources res= getResources();
        mem_names=res.getStringArray(R.array.members);
        Adapter adapter = new Adapter(this, mem_names, images);
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
        private final String[] mem_name;
        private final Integer[] image;

        public Adapter(Activity activity, String[] mem_name, Integer[] image) {
            super(activity, R.layout.group_members, mem_name);
            this.activity = activity;
            this.mem_name = mem_name;
            this.image = image;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.group_members, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            txtTitle.setText(mem_name[position]);
            imageView.setImageResource(image[position]);
            return rowView;

        }
    }
}
