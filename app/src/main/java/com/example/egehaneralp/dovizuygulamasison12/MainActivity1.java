package com.example.egehaneralp.dovizuygulamasison12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity1 extends AppCompatActivity {

    EditText adGiris1,sifreGiris1,adGiris2,sifreGiris2;
    Button buttonKayitOl, buttonGirisYap;

    DatabaseReference myRef;
    FirebaseDatabase database;
    String s1,s2,s3,s4,s5;
    Kullanici k1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kullanicilar");


        adGiris1=findViewById(R.id.adGiris1);
        sifreGiris1=findViewById(R.id.sifreGiris1);

        sifreGiris2=findViewById(R.id.sifreGiris2);
        adGiris2=findViewById(R.id.adGiris2);

        buttonGirisYap=findViewById(R.id.buttonGirisYap);
        buttonKayitOl=findViewById(R.id.buttonKayıtOl);



        buttonKayitOl.setOnClickListener(new View.OnClickListener() {//kayıtol
            @Override
            public void onClick(View v) {

                s1=adGiris1.getText().toString();
                s2=sifreGiris1.getText().toString();

                Kullanici k =new Kullanici(s1,s2);
                myRef.child(k.getAd()).setValue(k);
            }
        });

        buttonGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s3=adGiris2.getText().toString();
                s4=sifreGiris2.getText().toString();

                final Kullanici k=new Kullanici(s3,s4);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(k.getAd()).exists()){
                            Intent i =new Intent(MainActivity1.this,MainActivity2.class);
                            i.putExtra("isim",k.getAd());
                            i.putExtra("sifre",k.getSifre());
                            Toast.makeText(MainActivity1.this, "GİRİŞ BAŞARILI", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(MainActivity1.this, "KAYITLI ÜYE YOK.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
