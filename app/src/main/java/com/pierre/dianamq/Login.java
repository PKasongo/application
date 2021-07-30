package com.pierre.dianamq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText identifiant, password;
    Button btn_login;
    ProgressBar bar_chargement;
  //  TextView pass_oublie;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        identifiant = findViewById(R.id.identifiant);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        bar_chargement = findViewById(R.id.bar_chargement);
      //  pass_oublie = findViewById(R.id.pass_oublie);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = identifiant.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(id)){
                    identifiant.setError("l'identifiant est requis");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    password.setError("Mot de passe requis");
                    return;
                }

                if (pass.length() < 6){
                    password.setError("le mot de passe doit être >= à 6 caractères");
                }

                bar_chargement.setVisibility(View.VISIBLE);

                // authentification

                firebaseAuth.signInWithEmailAndPassword(id,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "authentifier", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), home.class));
                        } else {
                            Toast.makeText(Login.this, "Echec d'authentification", Toast.LENGTH_SHORT).show();
                            bar_chargement.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

     /*   pass_oublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText resetEmail = new EditText(view.getContext());
                AlertDialog.Builder passResetDialog = new AlertDialog.Builder(view.getContext());
                passResetDialog.setTitle("Réinitialisation du mot de passe");
                passResetDialog.setMessage("Entrer votre email pour recevoir le lien");
                passResetDialog.setView(resetEmail);

                passResetDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String mail = resetEmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Email de restauration envoyé à votre mail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Echec! l'email n'a pas été envoyé" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passResetDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

            }
        }); */
    }
}