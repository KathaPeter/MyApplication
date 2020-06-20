package com.example.myapplication.service;

import com.example.myapplication.data.KontaktDto;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreKontaktService {

    public static Task<DocumentSnapshot> getContactData(String uid) {
        return FirebaseFirestore.getInstance().collection("kontakt").document(uid).get();
    }

    public static void updateKontakt(String uid, KontaktDto dto) {
        CollectionReference collection = FirebaseFirestore.getInstance().collection("kontakt");
         collection.document(uid).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot result = task.getResult();
                if(result.exists()){
                    FirebaseFirestore.getInstance().collection("kontakt").document(uid).update(dto.toMap());
                }else{
                    FirebaseFirestore.getInstance().collection("kontakt").document(uid).set(dto.toMap());
                }
            }
        });
    }
}
