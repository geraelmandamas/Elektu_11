package com.epifi.elektu.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.epifi.elektu.HomeActivity;
import com.epifi.elektu.R;
import com.epifi.elektu.Utils.Puntos;
import com.epifi.elektu.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private Context mcontext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword, musername;
    private Button mbtnRegister;
    private String userID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        initWidgets();
        setUpFirebaseAuth();
        init();
    }


    private void initWidgets() {
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBarLogin);
        mEmail = (EditText) findViewById(R.id.EmailHint);
        mPassword = (EditText) findViewById(R.id.PasswordHint);
        musername = (EditText) findViewById(R.id.usernamehint);
        mcontext = RegisterActivity.this;


        mProgressBar.setVisibility(View.GONE);
    }

    /*
     *
     * -----------------------------------------------------Firebase-----------------------------------------------------------------------------------------------------------------------
     * */


    private void init() {
        Button mbtnRegister = (Button) findViewById(R.id.ButtonRegister);
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String username = musername.getText().toString();
                if (isStringNull(email) && isStringNull(password)) {
                    Toast.makeText(mcontext, "Debe rellenar todo", Toast.LENGTH_LONG).show();

                } else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                                        Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, R.string.auth_succes,
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        userID = mAuth.getCurrentUser().getUid();
                                        writePuntos(0);
                                        writeNewUser(username, email,password,0);
                                        if (mAuth.getCurrentUser() != null) {
                                            Intent i = new Intent(mcontext, HomeActivity.class);
                                            i.putExtra("userId", userID);
                                            startActivity(i);
                                            finish();
                                        }
                                    }

                                    // ...
                                }
                            });

                }
            }
        });
        //If the user is logged in the navigate to HomeActivity and call "finish
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(mcontext, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
    private void writePuntos(Integer puntosString){
        Puntos puntos = new Puntos(puntosString,userID);
        mDatabase.child("userspuntos").child(userID).setValue(puntos);


    }

    //Esto crea un nuevo usuario
    private void writeNewUser(String username, String email,String password,Integer puntosint) {
        User user = new User(username,email,password,puntosint,userID);

        mDatabase.child("users").child(userID).setValue(user);
    }

    private boolean checkInputs(String email, String nickname, String password) {

        if (email.equals("") || nickname.equals("") || password.equals("")) {
            Toast.makeText(mcontext, "Debe rellenar todo", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }


    private boolean isStringNull(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }

    }


    private void setUpFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

