package com.example.myapplication.service;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreFormularService {
    public static void updateFormularDate(String uid) {

        CollectionReference collection = FirebaseFirestore.getInstance().collection("formular");
        collection.document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                if (result.exists()) {
                    FirebaseFirestore.getInstance().collection("formular").document(uid).update(getCurrentTime());
                } else {
                    FirebaseFirestore.getInstance().collection("formular").document(uid).set(getCurrentTime());
                }
            }

        });

    }

    private static Map<String, Object> getCurrentTime() {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("formSentOn", FieldValue.serverTimestamp());
        return objectObjectHashMap;
    }
}
