package com.example.myapplication.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestorePatientService {

    private FirestorePatientService(){}

    public static Task<DocumentSnapshot> getPatientData(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return    db.collection("patient").document(uid).get();
    }

}
