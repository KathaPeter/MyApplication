package com.example.myapplication.data;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class KontaktDto {

    private String name;

    private String vorname;

    private String telefonNummer;

    private String plz;

    private String straße;

    private String hausnummer;

    private String email;

    @Exclude
    public Map<String,Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("vorname", vorname);
        result.put("telefonNummer", telefonNummer);
        result.put("straße", straße);
        result.put("hausnummer", hausnummer);
        result.put("plz", plz);
        return result;
    }

}
