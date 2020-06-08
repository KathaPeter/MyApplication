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
import com.example.myapplication.Dialog_YesNo;
import com.example.myapplication.Globals;
import com.example.myapplication.R;
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
        loadContactPerson();
        Button buttonSave = (Button) root.findViewById(R.id.bt_save);
        //  public Dialog_YesNo(FragmentActivity parent, String question, IEvent handlerYes, @Nullable IEvent handlerNo)
        buttonSave.setOnClickListener((v) ->
                new Dialog_YesNo(getActivity(), Globals.qSaveContactPerson, ContactPersonFragment.this::updateContactPerson, null)
        );

        Button buttonCancel = (Button) root.findViewById(R.id.bt_cancel);
        buttonCancel.setOnClickListener((v) ->
                new Dialog_YesNo(getActivity(), Globals.qCancelContactPerson, ContactPersonFragment.this::loadContactPerson, null)
        );


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadContactPerson();
    }

    private void updateContactPerson() {
        KontaktDto kontakt = extractFromForm();
        FirestoreKontaktService.updateKontakt(getActivity().getIntent().getStringExtra("user_uid"), kontakt);
        Toast.makeText(this.getContext(), "Kontaktdaten wurden gespeichert", Toast.LENGTH_LONG).show();
    }

    private KontaktDto extractFromForm() {
        KontaktDto kontaktDto = new KontaktDto();
        EditText vornameInput = (EditText) this.getActivity().findViewById(R.id.input_firstname);
        EditText nachnameInput = (EditText) this.getActivity().findViewById(R.id.input_lastname);
        EditText telefonInput = (EditText) this.getActivity().findViewById(R.id.input_telephone);
        EditText plzInput = (EditText) this.getActivity().findViewById(R.id.input_plz);
        EditText hausnummerInput = (EditText) this.getActivity().findViewById(R.id.input_housenumber);
        EditText straßeInput = (EditText) this.getActivity().findViewById(R.id.input_street);
        EditText emailInput = (EditText) this.getActivity().findViewById(R.id.input_email);

        kontaktDto.setEmail(emailInput.getText().toString());
        kontaktDto.setStraße(straßeInput.getText().toString());
        kontaktDto.setHausnummer(hausnummerInput.getText().toString());
        kontaktDto.setPlz(plzInput.getText().toString());
        kontaktDto.setTelefonNummer(telefonInput.getText().toString());
        kontaktDto.setName(nachnameInput.getText().toString());
        kontaktDto.setVorname(vornameInput.getText().toString());
        return kontaktDto;
    }


    private void loadContactPerson() {
        Task<DocumentSnapshot> getContactTask = FirestoreKontaktService.getKontaktDocumentReference(getActivity().getIntent().getStringExtra("user_uid"));
        getContactTask.addOnSuccessListener(result -> {
            KontaktDto kontaktDto = result.toObject(KontaktDto.class);
            Toast.makeText(this.getContext(), "Kontaktdaten wurden geladen", Toast.LENGTH_LONG).show();
            if(kontaktDto!= null) {
                reloadForm(kontaktDto);
            }});
    }
    private void reloadForm(KontaktDto kontaktDto) {
        EditText vornameInput = (EditText) this.getActivity().findViewById(R.id.input_firstname);
        EditText nachnameInput = (EditText) this.getActivity().findViewById(R.id.input_lastname);
        EditText telefonInput = (EditText) this.getActivity().findViewById(R.id.input_telephone);
        EditText plzInput = (EditText) this.getActivity().findViewById(R.id.input_plz);
        EditText hausnummerInput = (EditText) this.getActivity().findViewById(R.id.input_housenumber);
        EditText straßeInput = (EditText) this.getActivity().findViewById(R.id.input_street);
        EditText emailInput = (EditText) this.getActivity().findViewById(R.id.input_email);

        vornameInput.setText(kontaktDto.getVorname());
        nachnameInput.setText(kontaktDto.getName());
        telefonInput.setText(kontaktDto.getTelefonNummer());
        plzInput.setText(kontaktDto.getPlz());
        hausnummerInput.setText(kontaktDto.getHausnummer());
        straßeInput.setText(kontaktDto.getStraße());
        emailInput.setText(kontaktDto.getEmail());
    }
}
