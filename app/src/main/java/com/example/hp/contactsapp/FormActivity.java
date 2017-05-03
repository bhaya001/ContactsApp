package com.example.hp.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {
    EditText txtNomPrenom=null,txtTel=null,txtEmail=null;
    TextView idForm=null;
    Spinner statut = null;
    Button btnSave=null,btnAnnuler=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        idForm= (TextView) findViewById(R.id.idForm);
        txtNomPrenom= (EditText) findViewById(R.id.txtNomPrenom);
        txtTel= (EditText) findViewById(R.id.txtTel);
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
        statut= (Spinner) findViewById(R.id.txtStatut);
        ArrayAdapter<String> spiner = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        spiner.addAll(new String[]{"Choisir Votre Statut","admin","prof","etudiant"});
        statut.setAdapter(spiner);
        // Toast.makeText(this, getIntent().getStringExtra("nomPrenom"), Toast.LENGTH_SHORT).show();
        if(getIntent().getStringExtra("id")!=null){
            btnSave.setText("Modifier");
            idForm.setText(getIntent().getStringExtra("id").toString());
            txtNomPrenom.setText(getIntent().getStringExtra("nomPrenom").toString());
            txtTel.setText(getIntent().getStringExtra("tel").toString());
            txtEmail.setText(getIntent().getStringExtra("email").toString());
            statut.setSelection(2);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!statut.getSelectedItem().toString().equals("Choisir Votre Statut")) {

                    Intent data = new Intent();
                    data.putExtra("nomPrenom", txtNomPrenom.getText().toString());
                    data.putExtra("tel", txtTel.getText().toString());
                    data.putExtra("email", txtEmail.getText().toString());
                    data.putExtra("statut", statut.getSelectedItem().toString());
                    if(btnSave.getText().toString().equals("Modifier")) {
                        data.putExtra("id", idForm.getText().toString());
                        setResult(AccueilActivity.UPDATE_RESPONSE, data);
                    }
                    else {
                        setResult(AccueilActivity.ADD_RESPONSE, data);
                    }

                    finish();
                }
                else
                    Toast.makeText(FormActivity.this, "Choisir votre Statut !", Toast.LENGTH_LONG).show();

            }
        });


    }

}
