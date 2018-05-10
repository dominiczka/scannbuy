package com.coders.goodest.scannbuy.models;

import java.io.Serializable;

public class Product implements Serializable{

    private String id_kod_kreskowy;
    private String nazwa;
    private String producent;
    private float cena;
    private float waga_gramy;
    private String kategoria;
    private String opis;
    private int ilosc_na_stanie;
    private String id_sklepu;
    private int ImageID;
    private int ilosc_w_koszyku;

    public Product(String id_kod_kreskowy, String nazwa, String producent, float cena, float waga_gramy, String kategoria, String opis, int ilosc_na_stanie, String id_sklepu) {
        this.id_kod_kreskowy = id_kod_kreskowy;
        this.nazwa = nazwa;
        this.producent = producent;
        this.cena = cena;
        this.waga_gramy = waga_gramy;
        this.kategoria = kategoria;
        this.opis = opis;
        this.ilosc_na_stanie = ilosc_na_stanie;
        this.id_sklepu = id_sklepu;
        this.ilosc_w_koszyku=0;
    }

    public String getId_kod_kreskowy() {
        return id_kod_kreskowy;
    }

    public void setId_kod_kreskowy(String id_kod_kreskowy) {
        this.id_kod_kreskowy = id_kod_kreskowy;
    }

    public int getIlosc_w_koszyku(){return ilosc_w_koszyku;}

    public void setIlosc_w_koszyku(int ilosc_w_koszyku) {
        this.ilosc_w_koszyku = ilosc_w_koszyku;
    }

    public void dodano_do_koszyka(){ilosc_w_koszyku++;}

    public int getImage(){return ImageID;}

    public void setImage(int id){ImageID=id;}

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getProducent() {
        return producent;
    }

    public void setProducent(String producent) {
        this.producent = producent;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public float getWaga_gramy() {
        return waga_gramy;
    }

    public void setWaga_gramy(float waga_gramy) {
        this.waga_gramy = waga_gramy;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getIlosc_na_stanie() {
        return ilosc_na_stanie;
    }

    public void setIlosc_na_stanie(int ilosc_na_stanie) {
        this.ilosc_na_stanie = ilosc_na_stanie;
    }

    public String getId_sklepu() {
        return id_sklepu;
    }

    public void setId_sklepu(String id_sklepu) {
        this.id_sklepu = id_sklepu;
    }
}
