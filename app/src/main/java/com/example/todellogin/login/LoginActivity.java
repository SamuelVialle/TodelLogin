package com.example.todellogin.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todellogin.MainActivity;
import com.example.todellogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //========== VARIABLES ET INITIALISATION ==========//
    //1 Variables globales
    private TextInputEditText etEmail, etPassword;
    private String email, password;

    //2 Méthode initUI pour faire le lien entre le design et le code
    public void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }
    //========== END VARIABLES ET INITIALISATION ==========//

    //========== MÉTHODES ==========//
    //4 Méthode pour la gestion du clic sur le bouton login
    public void btnLoginClick(View v) {
        //4.1 Récupération de l'email et du password
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        //4.2 Vérification du remplissage des champs email et password
        if (email.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
        } else if (password.equals("")) {
            etPassword.setError(getString(R.string.enter_password));
        } else { // On se connecte
            //4.3 Connexion à authenticator
            //TODO Renvoyer vers FirebaseConstants
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //4.4 Ajout du lien vers mainActivity si l'utilisateur est bien connecté
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                //4.5 Utilisation de finish() pour fermer l'activité présente
                                finish();
                            } else {
                                //4.6 Affichage de l'erreur de connexion, il est possible de
                                // personnaliser, manuelement, le message en fonction du type d'erreur
                                Toast.makeText(LoginActivity.this,
                                        getString(R.string.login_failed) + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //5 Ajout du bouton reset password
    public void btnResetPasswordClick(View v) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    //6 Gestion du clic sur SignUp
    public void btnSignupClick(View v) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    //7.0 Ajout du bouton send à la place du retour chariot du keyboard
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // Utilisation de actionId qui correspond à l'action ajouter dans le xml
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    btnLoginClick(v);
            }
            return false; // On laisse le return à false pour empêcher le comportement normal du clavier
        }
    };
    //========== END MÉTHODES ==========//

    //========== CYCLES DE VIE ==========//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_login);
        //3 Appel de la méthode initUI
        initUI();

        //7.1 Association du clic dans le keyboard
        etPassword.setOnEditorActionListener(editorActionListener);
    }

    //8 Passer l'utilisateur loggué directement à MainActivity
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
    //========== END CYCLES DE VIE ==========//
}