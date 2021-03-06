package inspire2connect.inspire2connect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.emozers.cardviewexample.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class CardViewActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private MediaPlayer mMediaPlayer;
    boolean flag=false;
    private long version_from_database=-1,version_from_app;
    ImageView lab_logo,app_logo;
    Double threshold;
    final private int STORAGE_PERMISSION = 1;
    final private int MIC_PERMISSION = 2;
    DatabaseReference mDatabaseReference,ref,ref_for_thr,ref_for_ver;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private void requestPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(CardViewActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(CardViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Permission Granted
                    Toast.makeText(CardViewActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                }
                else
                {
                    // Permission Denied
                    Toast.makeText(CardViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MIC_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Permission Granted
                    Toast.makeText(CardViewActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                }
                else
                {
                    // Permission Denied
                    Toast.makeText(CardViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        /*if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }*/
    }
    @Override
    public void onStop()
    {
        super.onStop();
        /*if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.bt_green)
        {
            Intent i=new Intent(CardViewActivity.this,add_new.class);
            /*if(mMediaPlayer!=null)
            {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }*/
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        requestPermission();

        getSupportActionBar().hide();
        lab_logo=(ImageView)(findViewById(R.id.progress_bar));
        //app_logo=(ImageView)(findViewById(R.id.app_logo));
        //getSupportActionBar().setBackgroundDrawable(app_logo);
        final ArrayList results = new ArrayList<DataObject>();
        final CircularProgressDrawable cpd=new CircularProgressDrawable(this);
        cpd.setStyle(0);
        cpd.setBackgroundColor(1);
        cpd.setCenterRadius(2.0f);
        //ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 1; index++)
        {
            DataObject obj = new DataObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(results);
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
        ref_for_thr=FirebaseDatabase.getInstance().getReference();
        Query thr_query=ref_for_thr.child("threshold");
        thr_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //threshold_class obj_th=snapshot.getValue(threshold_class.class);
                    threshold=(Double) snapshot.getValue();

                    Log.d("threshold",Double.toString(threshold));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version_from_app = (long) info.versionCode;
            Log.d("threshod","Version from app"+Long.toString(version_from_app)+Boolean.toString(version_from_app==version_from_database));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ref_for_ver=FirebaseDatabase.getInstance().getReference();
        Query ver_query=ref_for_thr.child("version");
        ver_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //threshold_class obj_th=snapshot.getValue(threshold_class.class);
                    version_from_database=(Long) snapshot.getValue();

                    Log.d("threshodl",Long.toString(version_from_database));
                    if(version_from_app!=version_from_database)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardViewActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.drawable.image);
                        builder.setMessage("Please update your app.")
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
        ref=FirebaseDatabase.getInstance().getReference();
        Query lastQuery = ref.child("hindi").orderByKey();
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (! flag)
                {
                    cpd.start();
                int count = 0;
                ArrayList results2 = new ArrayList<DataObject>();
                HashMap<String, Story_Details> hn = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Story_Details user = snapshot.getValue(Story_Details.class);
                    hn.put(snapshot.getKey(), user);
                }
                for (Map.Entry<String, Story_Details> it : hn.entrySet())
                {
                    Log.d("Database", it.getValue().getTitle());
                    DataObject obj = new DataObject(Integer.toString(count + 1) + ".  " + it.getValue().getTitle(), it.getValue().getStory());
                    if(it.getValue().getSimilarity()>=threshold)
                        results2.add(obj);
                        count++;
                }
                if (count == 0) {
                    mAdapter = new MyRecyclerViewAdapter(results);
                } else {
                    mAdapter = new MyRecyclerViewAdapter(results2);
                }
                mRecyclerView.setAdapter(mAdapter);
                cpd.stop();
                lab_logo.setVisibility(View.INVISIBLE);
               // app_logo.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                /*try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.introduction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();*/
                flag=true;
            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v)
            {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Intent i=new Intent(CardViewActivity.this,Text2Speech2.class);
                i.putExtra("position",Integer.toString(position));
                /*if(mMediaPlayer!=null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer=null;
                }*/
                startActivity(i);
            }
        });
    }
}
