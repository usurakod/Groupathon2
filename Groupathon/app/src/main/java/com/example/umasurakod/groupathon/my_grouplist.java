package com.example.umasurakod.groupathon;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class my_grouplist extends AppCompatActivity {
    ListView myList;
    String[] titles;
    String[] descriptions;
    int[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_grouplist);

        myList = (ListView)findViewById(R.id.myGroupList);
        Resources res = getResources();
        titles=res.getStringArray(R.array.titles);
        descriptions = res.getStringArray(R.array.description);
        Adapter adapter = new Adapter(this, titles, descriptions, images);
        myList.setAdapter(adapter);
    }

    class Adapter extends ArrayAdapter<String> {
        Context context;
        int[] images;
        String[] titleArray;
        String[] descriptionArray;

        public Adapter(Activity activity, String[] titles, String[] descriptions, int[] imgs) {
            super(activity, R.layout.singlerow_listall, R.id.textView2, titles);
            this.context = activity;
            this.titleArray = titles;
            this.descriptionArray = descriptions;
            this.images = imgs;
        }
    }
}
