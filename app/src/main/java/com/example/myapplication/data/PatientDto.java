package com.example.myapplication.data;

import com.google.firebase.Timestamp;

import lombok.Data;

@Data
public class PatientDto {

    private String name;

    public String vorname;

    public Timestamp geburtsdatum;

    public String straße;

    public String hausnummer;

    public String ort;

    public String plz;


}
