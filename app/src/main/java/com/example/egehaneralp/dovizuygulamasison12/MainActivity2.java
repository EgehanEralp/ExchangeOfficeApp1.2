package com.example.egehaneralp.dovizuygulamasison12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity2 extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase database;

    TextView tlText,dolarText,euroText,textInfoD,textInfoE;
    EditText editText;
    RadioGroup bgrup;
    RadioButton dolarrb,eurorb,RB;
    Button satinAlButton,bozdurButton,buttonSiralamaGoster;

    String edittextS;
    int girilentutar;

    int control;
    String s1,s2;
    static float bakiyeTL,bakiyeDOLAR,bakiyeEURO;

    SharedPreferences sharedPre;
    SharedPreferences.Editor editor;

    Kullanici k1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText=findViewById(R.id.editText);

        bgrup=findViewById(R.id.bgrup);

        dolarrb=findViewById(R.id.dolarrb);
        eurorb=findViewById(R.id.eurorb);

        satinAlButton=findViewById(R.id.satinAlButton);
        bozdurButton=findViewById(R.id.bozdurButton);

        tlText=findViewById(R.id.tlText);
        dolarText=findViewById(R.id.dolarText);
        euroText=findViewById(R.id.euroText);

        textInfoD=findViewById(R.id.textInfoD);
        textInfoE=findViewById(R.id.textInfoE);

        buttonSiralamaGoster=findViewById(R.id.buttonSirala);

        sharedPre=getPreferences(MODE_PRIVATE);
        editor=sharedPre.edit();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("kullanicilar");

        Intent intent=getIntent();
        String isim=intent.getStringExtra("isim");
        String sifre=intent.getStringExtra("sifre");
        k1=new Kullanici(isim,sifre);


        Query sorgu = myRef.orderByChild("ad").equalTo(isim);
        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){ //for-each

                    //d -> o anki döngüdeki veri'dir!!
                    Kullanici kullanici=d.getValue(Kullanici.class); //kişi nesnesini DB den almak

                    String key = d.getKey();//d verisinin key ini alıp tutacağız, ilerde silmek ve güncellemek için lazım oluyor...
                    bakiyeTL=kullanici.getBakiyeTL();
                    bakiyeDOLAR=kullanici.getBakiyeUSD();
                    bakiyeEURO=kullanici.getBakiyeEUR();
                    tlText.setText(" "+bakiyeTL+" TL");
                    dolarText.setText(" "+bakiyeDOLAR+ " $");
                    euroText.setText(" "+bakiyeEURO+ " €");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tlText.setText(" "+bakiyeTL+" TL");
        dolarText.setText(" "+bakiyeDOLAR+ " $");
        euroText.setText(" "+bakiyeEURO+ " €");


        s1=sharedPre.getString("s11","DOLAR - ALIŞ=0, SATIŞ=0");
        s2=sharedPre.getString("s22","EURO - ALIŞ=0, SATIŞ=0");

        textInfoD.setText(s1);
        textInfoE.setText(s2);


        buttonSiralamaGoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity2.this,MainActivity3.class);

                startActivity(i);
            }
        });



        satinAlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control=0;
                new ArkaPlan().execute("https://api.exchangeratesapi.io/latest?base=TRY");
            }
        });
        bozdurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control=1;
                new ArkaPlan().execute("https://api.exchangeratesapi.io/latest?base=TRY");

            }
        });

    }

    class ArkaPlan extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader buf;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                buf = new BufferedReader(new InputStreamReader(is));

                String satir, dosya ="";

                while ((satir = buf.readLine()) != null) {
                    //Log.d("satir", satir);
                    dosya += satir;  // WHİLE BİTTİGİNDE SUNUCUDAKİ TÜM SATİRLARI ELDE ETMİŞ OLUCAĞIM

                }
                return dosya;

            } catch (Exception e) {
                e.printStackTrace();

            }

            return "sorun";


        }

        @Override
        protected void onPostExecute(String s) {

            int selectedRB= bgrup.getCheckedRadioButtonId(); //********************
            RB =findViewById(selectedRB);                   //SEÇİLMİŞ RB Yİ BELİRLEDİK

            edittextS=editText.getText().toString();         //EditText teki sayı ---editInt---
            girilentutar=Integer.parseInt(edittextS);

            try{
                JSONObject json1 =new JSONObject(s);
                String rates = json1.getString("rates");
                JSONObject json= new JSONObject(rates);

                double dolarAlis= (1/json.getDouble("USD"));
                double dolarSatis= (dolarAlis+0.021);

                double euroAlis= (1/json.getDouble("EUR"));
                double euroSatis= (euroAlis+0.031);

                if(control==0){ //SATIN AL BUTONUNA BASILDIYSA

                    if(RB==dolarrb){


                        double a=dolarAlis*girilentutar;
                        bakiyeTL-=a;
                        tlText.setText(" "+ bakiyeTL +" TL");
                        bakiyeDOLAR +=girilentutar;
                        dolarText.setText(" "+ bakiyeDOLAR + " $");



                    }
                    if(RB==eurorb){


                        double a =euroAlis*girilentutar;
                        bakiyeTL -=a;
                        tlText.setText(" "+bakiyeTL+" TL");
                        bakiyeEURO+=girilentutar;
                        euroText.setText(" "+bakiyeEURO + " €");



                    }

                }
                if(control==1){ //BOZDUR BUTONUNA TIKLANDIYSA
                    if(RB==dolarrb){


                        double a=dolarSatis*girilentutar;
                        bakiyeTL+=a;
                        tlText.setText(" "+bakiyeTL+" TL");
                        bakiyeDOLAR-=girilentutar;
                        dolarText.setText(" "+bakiyeDOLAR+" $");



                    }
                    if(RB==eurorb){


                        double a=euroSatis*girilentutar;
                        bakiyeTL+=a;
                        tlText.setText(" "+bakiyeTL+" TL");
                        bakiyeEURO-=girilentutar;
                        euroText.setText(" "+bakiyeEURO+" €");


                    }
                }
                double a,b,c,d;
                a=dolarAlis;
                b=dolarSatis;
                c=euroAlis;
                d=euroSatis;

                String str1,str2;
                str1= "DOLAR - ALIŞ="+a+", SATIŞ="+b;
                str2= "EURO - ALIŞ="+c+", SATIŞ="+d;

                textInfoD.setText(str1);
                textInfoE.setText(str2);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,Object> bilgiler =new HashMap<>();
                        bilgiler.put("bakiyeTL",bakiyeTL);
                        bilgiler.put("bakiyeEUR",bakiyeEURO);
                        bilgiler.put("bakiyeUSD",bakiyeDOLAR);
                        myRef.child(k1.getAd()).updateChildren(bilgiler);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                editor.putString("s11",str1);
                editor.putString("s22",str2);

                editor.commit();

            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
