package com.example.egehaneralp.dovizuygulamasison12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;
    TextView siralamaSonucu;

    String sonuc=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kullanicilar");

        siralamaSonucu =findViewById(R.id.sirala);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*long childsayi = dataSnapshot.getChildrenCount();
                float arr[]=new float[(int)childsayi];*/
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Kullanici kullanici=d.getValue(Kullanici.class);
                    /*arr[index]=kullanici.getBakiyeTL();
                    index++;*/
                    sonuc+=kullanici.getAd()+" -> "+kullanici.getBakiyeTL()+"\n"+" ";
                }
                siralamaSonucu.setText(sonuc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        siralamaSonucu.setText(sonuc);

    }
}

