package com.example.myapplication.data;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class KontaktDto {

    private String name;

    public String getVorname() {
        return vorname;
    }

    private String vorname;

    public String getName() {
        return name;
    }

    public String getTelefonNummer() {
        return telefonNummer;
    }

    private String telefonNummer;

    public String getPlz() {
        return plz;
    }

    private String plz;

    public String getStraße() {
        return straße;
    }

    private String straße;

    public String getHausnummer() {
        return hausnummer;
    }

    private String hausnummer;

    private String email;

    public String getEmail() {
        return email;
    }

       @Exclude
    public Map<String,Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("vorname", vorname);
        result.put("telefonNummer", telefonNummer);
        result.put("straße", straße);
        result.put("hausnummer", hausnummer);
        result.put("plz", plz);
        result.put("email", email);
        return result;
    }

    public void setEmail(String email) {
        this.email = email;

    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public void setStraße(String straße) {
        this.straße = straße;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public void setTelefonNummer(String telefonNummer) {
        this.telefonNummer = telefonNummer;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setName(String name) {
        this.name = name;
    }
}
