package com.example.myapplication.service;

import com.example.myapplication.data.PatientDto;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestorePatientService {

    private FirestorePatientService(){}

    public static Task<DocumentSnapshot> getPatientData(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return  db.collection("patient").document(uid).get();
    }

    public static void updateData(String uid, PatientDto patientDto) {
        CollectionReference collection = FirebaseFirestore.getInstance().collection("patient");
        collection.document(uid).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot result = task.getResult();
                if(result.exists()){
                    FirebaseFirestore.getInstance().collection("patient").document(uid).update(patientDto.toMap());
                }else{
                    FirebaseFirestore.getInstance().collection("patient").document(uid).set(patientDto.toMap());
                }
            }

        });

    }
}
