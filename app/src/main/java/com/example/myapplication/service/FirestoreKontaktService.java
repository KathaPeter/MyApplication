package com.example.myapplication.service;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreKontaktService {

    public static DocumentReference getKontaktDocumentReference(String uid){
       return  FirebaseFirestore.getInstance().collection("kontakt").document(uid);
    }

}
