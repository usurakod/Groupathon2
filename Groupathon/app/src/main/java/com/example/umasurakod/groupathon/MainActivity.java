package com.example.umasurakod.groupathon;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.umasurakod.groupathon.AccountActivity.LoginActivity;
import com.example.umasurakod.groupathon.AccountActivity.SettingActivity;
import com.example.umasurakod.groupathon.AccountActivity.SignupActivity;
import com.example.umasurakod.groupathon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.content.res.Resources;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ImageView music;
    private ImageView hacking;
    private ImageView hiking;
    private ImageView sports;
    private ImageView photography;
    private ImageView other;
    private EditText searchtext;
    private ImageButton search,cancel;
    private FirebaseAuth auth;
    private NavigationView navigationView;
    private ListView events;
    private static final int SELECT_PHOTO = 100;
    private ImageView chooseImage;
    String titles;
    String descriptions;
    String dates,loc;
    Uri selectedImage;
    //int[] images = {R.drawable.download1, R.drawable.download2, R.drawable.download3, R.drawable.download4, R.drawable.download5, R.drawable.download7, R.drawable.download8};
    private DatabaseReference groupathonGrpDetails;
    private DatabaseReference memberDetails;
    private DatabaseReference GrplocationDetails;
    private ArrayList<String> latestGroupNames ;
    private ArrayList<String> latestGroupDescriptions;
    private ArrayList<String> latestGroupDates ;
    private ArrayList<String> latestGrouplocations;

    private ArrayList<String> SearchlistGroupNames ;
    private ArrayList<String> SearchlistGroupDescriptions;
    private ArrayList<String> SearchlistGroupDates ;
    private ArrayList<String> SearchlistGrouplocations;

    private ArrayList<Integer> images;
    private ArrayList<Integer> images2;
    private String currentUID;
    private  Menu defaultmenu; //Notifications
    private FirebaseUser user;
    List<String> notify_list= new ArrayList<String>();
    private DatabaseReference createNotification_user;
    private String Notification="Notification";
    private String Notification_MSG="Notification_MSG";
    private int Notification_Count;
    private String Uname;
    private String NewUname;
    private String ClassName;
    Boolean flag=false;
    String  name,desc, date,venue;
    //@Prathibha ProfilePhoto
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap my_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        this.setTitle("Groupathon");
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUID = user.getUid();

       // Uname=user.getDisplayName();
        NewUname=getIntent().getStringExtra("Username");
        ClassName=getIntent().getStringExtra("ClassName");

        if((ClassName!=null)&&(ClassName.equals("SignupActivity")))
        {
            Uname=NewUname;
        }else
        {
            Uname=user.getDisplayName();
        }
        createNotification_user = FirebaseDatabase.getInstance().getReference().child(Notification).child(Uname);

        //setContentView(R.layout.navigation_header);
        //Resources res = getResources();
        //titles=res.getStringArray(R.array.titles);
        //descriptions = res.getStringArray(R.array.description);




        latestGroupNames = new ArrayList<>();
        latestGroupDates=new ArrayList<>();
        latestGrouplocations=new ArrayList<>();
        latestGroupDescriptions = new ArrayList<>();

        images = new ArrayList<>();
//        events.setAdapter(adapter);
        navigationView = findViewById(R.id.nav_view);
        View navHeader=navigationView.getHeaderView(0);

        chooseImage=navHeader.findViewById(R.id.chooseImage);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                int id = item.getItemId();


                if (id == R.id.Logout) {
                    signOut();

                } else if (id == R.id.mygroups) {
                    Intent intent = new Intent(getApplicationContext(), my_grouplist.class);
                    startActivity(intent);

                }
                else if (id == R.id.joinedgroups) {
                    Intent intent = new Intent(getApplicationContext(), myJoinedGroups.class);
                    startActivity(intent);

                }else if (id == R.id.Notifications) {


                } else if (id == R.id.Settings) {
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                }
                return true;

            }
        });
        myDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);
        myDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();





        music = (findViewById(R.id.music_img));
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category_groups.class);
                intent.putExtra("category","Music");
                startActivity(intent);

            }
        });

        hacking = findViewById(R.id.hacking_img);
        hacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category_groups.class);
                intent.putExtra("category","Hacking");
                startActivity(intent);

            }
        });

        hiking = findViewById(R.id.hiking_img);
        hiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category_groups.class);
                intent.putExtra("category","Hiking");
                startActivity(intent);
            }
        });

        sports = findViewById(R.id.sports_img);
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category_groups.class);
                intent.putExtra("category","Sports");
                startActivity(intent);
            }
        });

        photography = findViewById(R.id.photography_img);
        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Category_groups.class);
                intent.putExtra("category","Photography");
                startActivity(intent);
            }
        });

        other = findViewById(R.id.other_img);
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOncreateGrp();

            }
        });


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };



        //groupathonGrpDetails = FirebaseDatabase.getInstance().getReference().child("users").child(currentUID);
        groupathonGrpDetails = FirebaseDatabase.getInstance().getReference().child("Groups");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Groupmembers");
        String id = ref.push().getKey();
        DatabaseReference getEmail= FirebaseDatabase.getInstance().getReference().child("Groupmembers").child(id).child("Users");

        events = findViewById(R.id.events);

        //ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,R.layout.singlerow_listall,latestGroups);
        final SushAdapter adapter = new SushAdapter(this, latestGroupNames,latestGrouplocations,images);
        events.setAdapter(adapter);
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                TextView textview1 = (TextView) view.findViewById(R.id.textView2);
                String grpName = textview1.getText().toString();
                memberDetails = FirebaseDatabase.getInstance().getReferenceFromUrl("https://groupathon.firebaseio.com/Groupmembers/"+grpName);
                memberDetails.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot

                                boolean exists=collectEmailIds((Map<String,Object>) dataSnapshot.getValue());
                                if(flag==false) {
                                    if (!exists) {
                                        Intent intent = new Intent(getApplicationContext(), join_group.class);
                                        intent.putExtra("GrpName", latestGroupNames.get(position));
                                        intent.putExtra("GrpDesc", latestGroupDescriptions.get(position));
                                        intent.putExtra("Eventdate", latestGroupDates.get(position));
                                        intent.putExtra("Eventlocation", latestGrouplocations.get(position));
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), Group_details.class);
                                        intent.putExtra("GrpName", latestGroupNames.get(position));
                                        intent.putExtra("GrpDetails", latestGroupDescriptions.get(position));
                                        intent.putExtra("Eventdate", latestGroupDates.get(position));
                                        intent.putExtra("Eventlocation", latestGrouplocations.get(position));
                                        // intent.putExtra("Grpid", groupid);
                                        startActivity(intent);

                                    }
                                }
                                else if(flag==true)
                                {
                                    if (!exists) {
                                        Intent intent = new Intent(getApplicationContext(), join_group.class);
                                        intent.putExtra("GrpName", SearchlistGroupNames.get(position));
                                        intent.putExtra("GrpDesc", SearchlistGroupDescriptions.get(position));
                                        intent.putExtra("Eventdate", SearchlistGroupDates.get(position));
                                        intent.putExtra("Eventlocation", SearchlistGrouplocations.get(position));
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), Group_details.class);
                                        intent.putExtra("GrpName", SearchlistGroupNames.get(position));
                                        intent.putExtra("GrpDetails", SearchlistGroupDescriptions.get(position));
                                        intent.putExtra("Eventdate", SearchlistGroupDates.get(position));
                                        intent.putExtra("Eventlocation", SearchlistGrouplocations.get(position));
                                        // intent.putExtra("Grpid", groupid);
                                        startActivity(intent);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });



//                Intent intent = new Intent(getApplicationContext(), join_group.class);
//                intent.putExtra("GrpName", latestGroupNames.get(position));
//                intent.putExtra("GrpDesc", latestGroupDescriptions.get(position));
//                intent.putExtra("Eventdate", latestGroupDates.get(position));
//                intent.putExtra("Eventlocation", latestGrouplocations.get(position));
//                startActivity(intent);
            }
        });


            //createNotification_user = FirebaseDatabase.getInstance().getReference().child(Notification).child(user.getDisplayName());
            //Notify new group added
            createNotification_user.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (dataSnapshot.child("Notification_MSG").getValue(String.class) != null) {
                        String NewNotify = dataSnapshot.child("Notification_MSG").getValue(String.class);
                        Notification_Count = Notification_Count + 1;
                        Put_Notification_MSGS(NewNotify, Notification_Count);
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

        searchtext=(EditText) findViewById(R.id.searchtext);
        search=(ImageButton)findViewById(R.id.searchbutton) ;
        cancel=(ImageButton)findViewById(R.id.cancelbutton) ;

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                searchtext.getText().clear();
                flag=false;
                events.setAdapter(adapter);
            }
        });


        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                events.setAdapter(null);
                flag=true;
                //Toast.makeText(getApplicationContext(), "Clicked Button", Toast.LENGTH_LONG).show();
                SearchlistGroupNames = new ArrayList<>();
                SearchlistGroupDates=new ArrayList<>();
                SearchlistGrouplocations=new ArrayList<>();
                SearchlistGroupDescriptions = new ArrayList<>();

                String loc=searchtext.getText().toString();
                for(int i=0;i<latestGrouplocations.size();i++)
                {
                    if((latestGrouplocations.get(i)).equalsIgnoreCase(loc))
                    {
                         //Toast.makeText(getApplicationContext(), (latestGrouplocations.get(i)), Toast.LENGTH_LONG).show();
                         name = latestGroupNames.get(i);
                         desc = latestGroupDescriptions.get(i);
                         date = latestGroupDates.get(i);
                         venue = latestGrouplocations.get(i);
                         SearchlistGroupNames.add(name);
                         SearchlistGroupDescriptions.add(desc);
                         SearchlistGroupDates.add(date);
                         SearchlistGrouplocations.add(venue);
                         images.add(R.drawable.download1);

                    }
                }
                SushAdapter adapter1=new SushAdapter(getApplicationContext(),SearchlistGroupNames,SearchlistGrouplocations,images);
                events.setAdapter(adapter1);


            }
        });


//        Firebase search
//        search.setOnClickListener(new OnClickListener() {
//            @Override
//           public void onClick(View view) {
//               //Toast.makeText(getApplicationContext(), "Clicked Button", Toast.LENGTH_LONG).show();
//                String loc=searchtext.getText().toString();
//                SearchlistGroupNames = new ArrayList<>();
//                SearchlistGroupDates=new ArrayList<>();
//                SearchlistGrouplocations=new ArrayList<>();
//                SearchlistGroupDescriptions = new ArrayList<>();
//                images2=new ArrayList<>();
//
//                DatabaseReference eventlocations=FirebaseDatabase.getInstance().getReference().child("Eventlocations").child(loc);
//                eventlocations.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                       if(dataSnapshot.child("Name").getValue(String.class)!=null || dataSnapshot.child("Details").getValue(String.class)!=null) {
//                           name = dataSnapshot.child("Name").getValue(String.class);
//                           desc = dataSnapshot.child("Details").getValue(String.class);
//                           date = dataSnapshot.child("Event Date").getValue(String.class);
//                           venue = dataSnapshot.child("Location").getValue(String.class);
//                           SearchlistGroupNames.add(name);
//                           SearchlistGroupDescriptions.add(desc);
//                           SearchlistGroupDescriptions.add(desc);
//                           SearchlistGroupDates.add(date);
//                           SearchlistGrouplocations.add(venue);
//                           images2.add(R.drawable.download1);
//                           images2.add(R.drawable.download2);
//                          }
//
//                           SushAdapter adapter1=new SushAdapter(getApplicationContext(),SearchlistGroupNames,SearchlistGroupDescriptions,images2);
//                           events.setAdapter(adapter1);
//                           adapter1.notifyDataSetChanged();
//                           }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                   public void onCancelled(DatabaseError databaseError) {
//
//                   }
//               });
//
//
//           }
//       });
//

        groupathonGrpDetails.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("Name").getValue(String.class)!=null || dataSnapshot.child("Details").getValue(String.class)!=null) {
                    titles = dataSnapshot.child("Name").getValue(String.class);
                    descriptions = dataSnapshot.child("Details").getValue(String.class);
                    dates=dataSnapshot.child("Event Date").getValue(String.class);
                    loc=dataSnapshot.child("Location").getValue(String.class);
                    latestGroupNames.add(titles);
                    latestGroupDescriptions.add(descriptions);
                    latestGroupDates.add(dates);
                    latestGrouplocations.add(loc);
                    images.add(R.drawable.musicnew);
                    images.add(R.drawable.hackingnew);
                    images.add(R.drawable.hickingnew);
                    images.add(R.drawable.sportsnew);
                    images.add(R.drawable.photonew);
                    images.add(R.drawable.othernew);

                    adapter.notifyDataSetChanged();
                }
            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        groupathonGrpDetails.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                titles = dataSnapshot.child("Details").getValue(String.class);
//                latestGroups.add(titles);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



        if(user.getDisplayName()!=null) {

            Menu menu = navigationView.getMenu();
            String text = "Hi, " + user.getDisplayName() + "!";
            MenuItem username = menu.findItem(R.id.Username);
            username.setTitle(text);
        }
        else{
            Menu menu = navigationView.getMenu();
            String uname=getIntent().getStringExtra("Username");
            String text = "Hi, " +uname + "!";
            MenuItem username = menu.findItem(R.id.Username);
            username.setTitle(text);

        }



    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notify, menu);

        this.defaultmenu=menu;
        Notification_Count=0;
        //@Prathibha FirebaseCall for ProfilePic
        getImage();
        setCount(this,Notification_Count); //change this wen item added and member added to existing grp
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(item.getItemId()==R.id.Notifications){
            //Call Dialog Box with the stacked notification messages
            showDialogBoxMessage("Recent Notifications","Notification1");
            //set count to  after clicked on notifications and clear the alertbox
            Notification_Count=0;
            setCount(this,Notification_Count);

            //remove data enrey from firebase
            createNotification_user.setValue(null);
            //createNotification_user.

        }
        if (mToggle.onOptionsItemSelected(item)) {
            Log.d("MSG",item.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void signOut() {
        auth.signOut();
//        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    finish();
//                }
//            }
//        };
    }// *****TO-DO this authListener to be in Settings page as well********

    public void openOncreateGrp() {
        Intent intent = new Intent(this, create_group.class);
        startActivity(intent);
    }
    // to select item from action bar

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {

                //setDataToView(user);

            }
        }


    };


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }

    }

    public class SushAdapter extends BaseAdapter {
        Context context;
        ArrayList Item;
        ArrayList SubItem;
        ArrayList flags;
        LayoutInflater inflter;

        public SushAdapter(Context applicationContext, ArrayList<String> Item, ArrayList<String> SubItem , ArrayList<Integer> flags) {
            this.context = applicationContext;
            this.Item = Item;
            this.SubItem = SubItem;
            this.flags = flags;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return Item.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.singlerow_listall, null);
            TextView item = (TextView) view.findViewById(R.id.textView2);
            TextView subitem = (TextView) view.findViewById(R.id.textView3);
            ImageView image = (ImageView) view.findViewById(R.id.imageView);
            item.setText(Item.get(i).toString());
            subitem.setText(SubItem.get(i).toString());
            image.setImageResource((int)flags.get(i));
            return view;
        }
    }
    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

  /*  public void ChooseImage(View v){
        openGallery();
    }*/


    //@Prathibha
    public void setCount(Context context,int count) {
        String Count=Integer.toString(count);
        MenuItem menuItem = defaultmenu.findItem(R.id.Notifications);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        }
        else {
            badge = new CountDrawable(context);
        }

        badge.setCount(Count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    public void showDialogBoxMessage(String title,String msg)
    {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.notify_list);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //alertDialog.
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.notify_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("New Notifications");

        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        if(notify_list.isEmpty()) {
            notify_list.add("  No new Notification!!..");
           }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notify_list);

        lv.setAdapter(adapter);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                notify_list.clear();
            }
        }).show();
    }

    private boolean collectEmailIds(Map<String,Object> users) {
        Intent intent = getIntent();
        String email = user.getEmail();
        ArrayList<String> emailIds = new ArrayList<>();


        for (Map.Entry<String, Object> entry : users.entrySet()){


            Map singleUser = (Map) entry.getValue();

            //String useri=(String)singleUser.get(0);
            String userii = (String)singleUser.get("User");
            emailIds.add( userii);
        }

        if(emailIds.contains(user.getEmail())){
            return true;
        }

        return false;
        }

    //To stack notifications take array list as parameter
    public void Put_Notification_MSGS(String Notification_MSG,int  Notification_Count){
//        Toast.makeText(MainActivity.this, Notification_MSG,Toast.LENGTH_SHORT).show();
        //Notification_Count=+1;
        setCount(this,Notification_Count);
        notify_list.add(Notification_MSG);
    }

    public void ChooseImage(View v){
        openGallery();
    }

    public void getImage(){

        String my_image_path="images/"+currentUID.toString();
        Log.d("Image path",my_image_path);
        StorageReference ref;
       // if(storage.getReference().child(my_image_path).equals(null)) {

        //}
        try {
            ref = storage.getReference().child(my_image_path);
        }
        catch (Exception e){
            my_image_path="images/profile1.jpg";
            Log.d("Image path",my_image_path);
            Log.d("Image path",my_image_path+storage.getReference());
            ref = storage.getReference().child(my_image_path);

        }
        try {
            final File localFile = File.createTempFile("Images", "bmp");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    chooseImage.setImageBitmap(my_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //retain default profile image
                    // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadImage_toFirebase() {
        if(selectedImage != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ currentUID.toString());
            ref.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();

                    if (selectedImage != null) {
                        chooseImage.setImageURI(selectedImage);
                        //store in firebase

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            chooseImage.setImageBitmap(bitmap);
                            uploadImage_toFirebase();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }

}







