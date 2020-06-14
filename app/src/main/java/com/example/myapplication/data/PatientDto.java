package com.example.myapplication.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class PatientDto {

    public String name;

    public String vorname;

    public Timestamp geburtsdatum;

    public String straße;

    public String hausnummer;

    public String ort;

    public String plz;


    @Exclude
    public Map<String,Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("vorname", vorname);
        result.put("straße", straße);
        result.put("hausnummer", hausnummer);
        result.put("plz", plz);
        result.put("ort", ort);
        //result.put("geburtsdatum", geburtsdatum);
        return result;
    }
}
