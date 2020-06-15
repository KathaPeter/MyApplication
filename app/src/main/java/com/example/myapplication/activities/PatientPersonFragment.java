package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.DialogYesNo;
import com.example.myapplication.Globals;
import com.example.myapplication.Helper;
import com.example.myapplication.R;
import com.example.myapplication.ValidationException;
import com.example.myapplication.data.PatientDto;
import com.example.myapplication.service.FirestorePatientService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class PatientPersonFragment extends androidx.fragment.app.Fragment {

    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_4, container, false);

        Button buttonSave = (Button) root.findViewById(R.id.bt_save);
        //  public Dialog_YesNo(FragmentActivity parent, String question, IEvent handlerYes, @Nullable IEvent handlerNo)
        buttonSave.setOnClickListener((v) ->
                new DialogYesNo(getActivity(), Globals.qSavePatientData, PatientPersonFragment.this::updatePatientData, null)
        );

        Button buttonCancel = (Button) root.findViewById(R.id.bt_cancel);
        buttonCancel.setOnClickListener((v) ->
                new DialogYesNo(getActivity(), Globals.qCancelPatientData, PatientPersonFragment.this::loadPatientData, null)
        );


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadPatientData();
    }

    private void updatePatientData() {
        try {
            PatientDto patientDto = extractFromForm();
            FirestorePatientService.updateData(getActivity().getIntent().getStringExtra("user_uid"), patientDto);
            Toast.makeText(this.getContext(), "Patientendaten wurden gespeichert", Toast.LENGTH_LONG).show();
        } catch(ValidationException exc ) {
            Toast.makeText(this.getContext(), "PatientData: Failure "+exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private PatientDto extractFromForm() throws ValidationException {
        PatientDto patientDto = new PatientDto();

        //patientDto.geburtsdatum = Helper.validate(root, R.id.input_birthdate); //FIXME format tiumestamp
        patientDto.straße = Helper.validate(root, R.id.input_street);
        patientDto.hausnummer = Helper.validate(root, R.id.input_housenumber);
        patientDto.name = Helper.validate(root, R.id.input_lastname);
        patientDto.vorname = Helper.validate(root, R.id.input_firstname);
        patientDto.plz = Helper.validate(root, R.id.input_plz);
        patientDto.ort = Helper.validate(root, R.id.input_town);


        return patientDto;
    }


    private void loadPatientData() {
        Task<DocumentSnapshot> getDataTask = FirestorePatientService.getPatientData(getActivity().getIntent().getStringExtra("user_uid"));
        getDataTask.addOnSuccessListener(result -> {
            PatientDto patientDto = result.toObject(PatientDto.class);
            //Toast.makeText(this.getContext(), "Patientendaten wurden geladen", Toast.LENGTH_LONG).show();
            if(patientDto!= null) {
                reloadForm(patientDto);
            }});
    }
    private void reloadForm(PatientDto patientDto) {
        EditText vornameInput = (EditText) root.findViewById(R.id.input_firstname);
        EditText nachnameInput = (EditText) root.findViewById(R.id.input_lastname);
        // EditText birthdayInput = (EditText) root.findViewById(R.id.input_birthdate);
        EditText plzInput = (EditText) root.findViewById(R.id.input_plz);
        EditText hausnummerInput = (EditText) root.findViewById(R.id.input_housenumber);
        EditText straßeInput = (EditText) root.findViewById(R.id.input_street);
        EditText townInput = (EditText) root.findViewById(R.id.input_town);
        EditText emailInput = (EditText) root.findViewById(R.id.input_email);

        vornameInput.setText(patientDto.vorname);
        nachnameInput.setText(patientDto.name);
        //telefonInput.setText(patientDto.geburtsdatum.toString()); //TODO format TimeStamp
        plzInput.setText(patientDto.plz);
        hausnummerInput.setText(patientDto.hausnummer);
        straßeInput.setText(patientDto.straße);
        townInput.setText(patientDto.ort);
        emailInput.setText(getActivity().getIntent().getStringExtra("user_email"));
    }


}
