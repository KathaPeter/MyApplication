package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DialogYesNo;
import com.example.myapplication.Globals;
import com.example.myapplication.Helper;
import com.example.myapplication.R;
import com.example.myapplication.ValidationException;
import com.example.myapplication.data.KontaktDto;
import com.example.myapplication.service.FirestoreKontaktService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ContactPersonFragment extends Fragment {

    private View root;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_3, container, false);

        Button buttonSave = (Button) root.findViewById(R.id.bt_save);
        //  public Dialog_YesNo(FragmentActivity parent, String question, IEvent handlerYes, @Nullable IEvent handlerNo)
        buttonSave.setOnClickListener((v) ->
                new DialogYesNo(getActivity(), Globals.qSaveContactPerson, ContactPersonFragment.this::updateContactPerson, null)
        );

        Button buttonCancel = (Button) root.findViewById(R.id.bt_cancel);
        buttonCancel.setOnClickListener((v) ->
                new DialogYesNo(getActivity(), Globals.qCancelContactPerson, ContactPersonFragment.this::loadContactPerson, null)
        );


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadContactPerson();
    }

    private void updateContactPerson() {
        try {
            KontaktDto kontakt = extractFromForm();
            getActivity().getIntent().putExtra("contact_email", kontakt.getEmail());
            FirestoreKontaktService.updateKontakt(getActivity().getIntent().getStringExtra("user_uid"), kontakt);
            Toast.makeText(this.getContext(), "Kontaktdaten wurden gespeichert", Toast.LENGTH_LONG).show();
        } catch(ValidationException exc ) {
            Toast.makeText(this.getContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private KontaktDto extractFromForm() throws ValidationException {
        KontaktDto kontaktDto = new KontaktDto();


        kontaktDto.setEmail(Helper.validate(root, R.id.input_email));
        //kontaktDto.setStraße(Helper.validate(root, R.id.input_town));
        kontaktDto.setHausnummer(Helper.validate(root, R.id.input_housenumber));
        kontaktDto.setPlz(Helper.validate(root, R.id.input_plz));
        kontaktDto.setTelefonNummer(Helper.validate(root, R.id.input_birthdate));
        kontaktDto.setName(Helper.validate(root, R.id.input_lastname));
        kontaktDto.setVorname(Helper.validate(root, R.id.input_firstname));
        return kontaktDto;
    }


    private void loadContactPerson() {
        Task<DocumentSnapshot> getContactTask = FirestoreKontaktService.getContactData(getActivity().getIntent().getStringExtra("user_uid"));
        getContactTask.addOnSuccessListener(result -> {
            KontaktDto kontaktDto = result.toObject(KontaktDto.class);
            //Toast.makeText(this.getContext(), "Kontaktdaten wurden geladen", Toast.LENGTH_LONG).show();
            if(kontaktDto!= null) {
                reloadForm(kontaktDto);
            }});
    }
    private void reloadForm(KontaktDto kontaktDto) {
        EditText vornameInput = (EditText) root.findViewById(R.id.input_firstname);
        EditText nachnameInput = (EditText) root.findViewById(R.id.input_lastname);
        EditText telefonInput = (EditText) root.findViewById(R.id.input_birthdate);
        EditText plzInput = (EditText) root.findViewById(R.id.input_plz);
        EditText hausnummerInput = (EditText) root.findViewById(R.id.input_housenumber);
        EditText straßeInput = (EditText) root.findViewById(R.id.input_street);
        EditText emailInput = (EditText) root.findViewById(R.id.input_email);

        vornameInput.setText(kontaktDto.getVorname());
        nachnameInput.setText(kontaktDto.getName());
        telefonInput.setText(kontaktDto.getTelefonNummer());
        plzInput.setText(kontaktDto.getPlz());
        hausnummerInput.setText(kontaktDto.getHausnummer());
        straßeInput.setText(kontaktDto.getStraße());
        emailInput.setText(kontaktDto.getEmail());
    }
}
