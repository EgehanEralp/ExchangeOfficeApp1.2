package com.example.egehaneralp.dovizuygulamasison12;

public class Kullanici {

    String ad,sifre;
    float bakiyeTL,bakiyeUSD,bakiyeEUR;

    public Kullanici(){

    }


    public Kullanici(String s,String s1){
        ad=s;
        sifre=s1;
        bakiyeTL=10000;
        bakiyeUSD=0;
        bakiyeEUR=0;

    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public float getBakiyeTL() {
        return bakiyeTL;
    }

    public void setBakiyeTL(float bakiyeTL) {
        this.bakiyeTL = bakiyeTL;
    }

    public float getBakiyeUSD() {
        return bakiyeUSD;
    }

    public void setBakiyeUSD(float bakiyeUSD) {
        this.bakiyeUSD = bakiyeUSD;
    }

    public float getBakiyeEUR() {
        return bakiyeEUR;
    }

    public void setBakiyeEUR(float bakiyeEUR) {
        this.bakiyeEUR = bakiyeEUR;
    }
}
