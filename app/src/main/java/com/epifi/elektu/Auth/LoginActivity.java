package com.epifi.elektu.Auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.epifi.elektu.HomeActivity;
import com.epifi.elektu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mcontext;
    private ProgressBar mProgressBar;
    private EditText mEmail,mPassword;
    private TextView mPleaseWait,mRegistrate,mRegistrate0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBarLogin);
        mEmail = (EditText) findViewById(R.id.EmailEditText);
        mPassword = (EditText) findViewById(R.id.PasswordEditText);
        mRegistrate = (TextView) findViewById(R.id.Link_SignUpTextLink);
        mRegistrate0 = (TextView) findViewById(R.id.Link_SignUp);
        mcontext = LoginActivity.this;



        mRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(mcontext,RegisterActivity.class);
                startActivity(o);
            }
        });
        mRegistrate0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(mcontext,RegisterActivity.class);
                startActivity(p);
            }
        });



        mProgressBar.setVisibility(View.GONE);
        setUpFirebaseAuth();
        init();
    }
    /*
     *
     * -----------------------------------------------------Firebase-----------------------------------------------------------------------------------------------------------------------
     * */
    private boolean isStringNull(String string){
        if (string.equals("")){
            return true;
        }else {
            return false;
        }

    }
    private void init(){
        Button btnLogin = (Button) findViewById(R.id.ButtonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (isStringNull(email) && isStringNull(password)){
                    Toast.makeText(mcontext,"Debe rellenar todo",Toast.LENGTH_LONG).show();

                }else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                                        Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(LoginActivity.this, R.string.auth_succes,
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        if (mAuth.getCurrentUser() != null ){
                                            Intent i = new Intent(mcontext, HomeActivity.class);
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
        if (mAuth.getCurrentUser() != null ){
            Intent i = new Intent(mcontext, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }


    private void setUpFirebaseAuth(){
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










