package com.example.myapplication.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class PatientDto {

    private String name;

    private String vorname;

    private Timestamp geburtsdatum;

    private String straße;

    private String hausnummer;

    private String ort;

    private String plz;

}
