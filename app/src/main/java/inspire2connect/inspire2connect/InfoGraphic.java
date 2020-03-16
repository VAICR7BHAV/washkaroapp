package inspire2connect.inspire2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoGraphic extends AppCompatActivity {

    TextView mythbusters,Guidlines,dailyUpdates;
    ToggleButton langToggleButton;
    Button gotoNews;
    DatabaseReference dref;
    ScrollView parentScrollView;
    ScrollView childScrollView1;
    ScrollView childScrollView2;
    ScrollView childScrollView3;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_graphic);
        parentScrollView=(ScrollView)findViewById(R.id.parent_scroll);
        childScrollView1=(ScrollView)findViewById(R.id.child1);
        childScrollView2=(ScrollView)findViewById(R.id.child2);
        childScrollView3=(ScrollView)findViewById(R.id.child3);
        mythbusters=(TextView)findViewById(R.id.mythbusters);
        Guidlines = (TextView)findViewById(R.id.guidlines);
        dailyUpdates=(TextView)findViewById(R.id.dailyupdates);
        langToggleButton = (ToggleButton)findViewById(R.id.langtogg);

        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "PARENT TOUCH");

                findViewById(R.id.child1).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                findViewById(R.id.child2).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                findViewById(R.id.child3).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        childScrollView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "CHILD TOUCH1");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        childScrollView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "CHILD TOUCH2");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        childScrollView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("scroll_test", "CHILD TOUCH3");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setGuidelinesHindi();
        setMythBustersHindi();
        setDailyUpdatesEng();

       // setNews();


        //toggle button listner
        langToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //set to hindi
                    setGuidelinesEng();
                    setMythBustersEng();
                    Toast.makeText(InfoGraphic.this,"English selected",Toast.LENGTH_SHORT).show();


                }
                else{

                    setGuidelinesHindi();
                    setMythBustersHindi();
                    Toast.makeText(InfoGraphic.this,"Hindi selected",Toast.LENGTH_SHORT).show();
                    //set to english
                }

            }
        });


        onGotoNewsistner();

    }


    private void setMythBustersEng() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines ="";
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    String g_hindi = snapshot.child("Myth_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if(sno.equals("1"))
                        guidelnines+=sno+". "+g_hindi;
                    else
                        guidelnines+="\n\n" +sno+". "+g_hindi;
                }
                mythbusters.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setMythBustersHindi() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("Myth");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines ="";
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    String g_hindi = snapshot.child("Myth_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if(sno.equals("1"))
                        guidelnines+=sno+". "+g_hindi;
                    else
                        guidelnines+="\n\n" +sno+". "+g_hindi;
                }
                mythbusters.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setGuidelinesHindi() {
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("guidelines");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines ="";
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    String g_hindi = snapshot.child("Guideline_hin").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if(sno.equals("1"))
                        guidelnines+=sno+". "+g_hindi;
                    else
                    guidelnines+="\n\n" +sno+". "+g_hindi;
                }
                Guidlines.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setGuidelinesEng() {

        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("guidelines");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines ="";
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    String g_eng = snapshot.child("Guideline_en").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if(sno.equals("1"))
                        guidelnines+=sno+". "+g_eng;
                    else
                        guidelnines+="\n\n" +sno+". "+g_eng;
                }
                Guidlines.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void setDailyUpdatesEng(){
        dref = FirebaseDatabase.getInstance().getReference().child("Coronavirus").child("DailyUpdate");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String guidelnines ="";
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    String g = snapshot.child("Summary").getValue(String.class);
                    String sno = snapshot.child("Sno").getValue().toString();
                    if(sno.equals("1"))
                        guidelnines += sno+". "+g;
                    else
                        guidelnines += "\n\n" +sno+". "+g;
                }
                dailyUpdates.setText(guidelnines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void onGotoNewsistner(){
        gotoNews=(Button)findViewById(R.id.gotonews);
        gotoNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoGraphic.this,CardViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
