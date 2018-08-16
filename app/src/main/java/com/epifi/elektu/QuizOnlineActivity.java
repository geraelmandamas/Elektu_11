package com.epifi.elektu;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class QuizOnlineActivity extends AppCompatActivity  {
    //Widgets
    private TextView textPregunta,textPuntos,TvTuspuntosSon,textTiempo,TvTuspuntosSonDialogDos,TvTuspuntosSonDialogCuatro,TVDialog;
    private Button btn1,btn2,btn3,btn4,BtnGetRecompensa,BtnEmpezar,BtnSalir,BtnCanjearDialogDos
            ,BtnSalirDialogDos,BtnEmpezarDialogtres
            ,BtnSalirDialogTres,BtnEmpezarDialogCuatro,BtnContinuarDialogCuatro,Btn1Dialog,Btn2Dialog;
    private Context mContext;
    private ImageView btnBack;
    private CountDownTimer countDownTimer;
    private AlertDialog dialog2,dialog3,dialog4;
    private AlertDialog.Builder alertDialogbuilder2,alertDialogbuilder3,alertDialogbuilder4;
    private AdView mAdView2;



    //Quiz
    private PreguntasQuizOnline mpreguntas = new PreguntasQuizOnline();
    private String manswer;
    private int mPuntos =  0;
    private int vidas = 3;
    private int mPreguntaLenght = mpreguntas.mPreguntas.length;
    private   DatabaseReference myRef;
    private String uid;


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myref;

    //Add
    //Add Video

    private Boolean closeTrue = true;
    Random r;
    private static final String TAG = QuizOnlineActivity.class.getSimpleName();
    public static final String SITE_KEY = "6LfneV4UAAAAADmCb7UprRUi-x5jMV-q4v9U3_4l";
    public static final String SITE_SECRET_KEY = "6LfneV4UAAAAACCukfOKuB_oo3yqeVCmD4oKX6VE";
    String userResponseToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_online);
        initWidgets();
        mContext = QuizOnlineActivity.this;
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        BtnGetRecompensa.setEnabled(true);





        //Add Banner
        mAdView2 = findViewById(R.id.adView2);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);
        mAdView2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        //Add Video
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");




        //Add
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");


        sstart();
        r = new Random();

        textPuntos.setText("" + mPuntos);

        updatePregunta(r.nextInt(mPreguntaLenght));





    }


    private void initWidgets(){
        textPregunta = (TextView) findViewById(R.id.textPregunta);
        textPuntos = (TextView) findViewById(R.id.TVPuntosPlayer1);
        btn1 = (Button)  findViewById(R.id.Btn1);
        btn2 = (Button) findViewById(R.id.Btn2);
        btn3 = (Button)  findViewById(R.id.Btn3);
        btn4 = (Button) findViewById(R.id.Btn4);
        BtnGetRecompensa = (Button) findViewById(R.id.BtnGetRecompensa);
        textTiempo = (TextView) findViewById(R.id.TV1Tiempo);


    }
    private void updatePregunta(int num){
        textPregunta.setText(mpreguntas.getPregunta(num));
        btn1.setText(mpreguntas.getChoice1(num));
        btn2.setText(mpreguntas.getChoice2(num));
        btn3.setText(mpreguntas.getChoice3(num));
        btn4.setText(mpreguntas.getChoice4(num));

        manswer = mpreguntas.getCorrectAnswer(num);




    }
    private void gameOver(){
        Intent v = new Intent(mContext,CelebrationActivity.class);
        startActivity(v);

    }
    private void goToGetPuntosActivity(){
        finish();
        Intent w = new Intent(new Intent(mContext,CelebrationActivity.class));
        w.putExtra("puntosUsuario", mPuntos);
        startActivity(w);




    }





    ///Timer

    private void sstart(){
        textTiempo.setText("21s");
        countDownTimer =  new CountDownTimer(22 * 1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                textTiempo.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                vidas--;






            }
        };

        countDownTimer.start();
    }
    private void Cancel(){
        if (countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;

        }

    }








    private void dialog3(){

        alertDialogbuilder3 = new AlertDialog.Builder(mContext);

        View mView = getLayoutInflater().inflate(R.layout.dialog_elegir_tres,null);

        BtnEmpezarDialogtres = (Button) mView.findViewById(R.id.BtnEmpezarDialog3);

        BtnSalirDialogTres = (Button) mView.findViewById(R.id.BtnSalirDialog3);

        Cancel();

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        BtnGetRecompensa.setEnabled(false);
        BtnEmpezarDialogtres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),QuizOnlineActivity.class));
            }
        });

        BtnSalirDialogTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        alertDialogbuilder3.setView(mView);


    }
    private void dialog4(){
        alertDialogbuilder4 = new AlertDialog.Builder(mContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_elegir_cuatro,null);

        BtnEmpezarDialogCuatro = (Button) mView.findViewById(R.id.BtnEmpezarDialogCuatro);
        TvTuspuntosSonDialogCuatro = (TextView) mView.findViewById(R.id.TVTusPuntosSonDialogCuatro);
        BtnContinuarDialogCuatro = (Button) mView.findViewById(R.id.BtnContinuarDialogCuatro);

        Cancel();
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        BtnGetRecompensa.setEnabled(false);
        TvTuspuntosSonDialogCuatro.setText("Tus puntos son: " + mPuntos);
        BtnEmpezarDialogCuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),QuizOnlineActivity.class));
            }
        });


        alertDialogbuilder4.setView(mView);



    }


    /*private void setUpAuthetication(){
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(mContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_elegir,null);
        TVDialog = (TextView) mView.findViewById(R.id.TVRegistrate);
        Btn1Dialog = (Button) mView.findViewById(R.id.BtnLogin);
        Btn2Dialog = (Button) mView.findViewById(R.id.BtnRegister);

        //Actions for Btn1Dialog
        Btn1Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);

            }
        });
        //Actions for Btn2Dialog
        Btn2Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RegisterActivity.class);
                startActivity(intent);

            }
        });

        alertDialogbuilder.setView(mView);
        AlertDialog dialog = alertDialogbuilder.create();
        dialog.show();



    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void checkCurrentUser(FirebaseUser user) {
        if (user == null) {
            setUpAuthetication();


        }
        loadRewardedVideoAd();

    }*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btn1(View view){
        if (btn1.getText() == manswer){
            mPuntos++;
            btn1.setBackground(getDrawable(R.drawable.box_correct_verde));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn1.setBackground(getDrawable(R.drawable.button));

                }

            },200);

            textPuntos.setText("" +mPuntos);
            Cancel();
            if (mPuntos >= 5) {
                Cancel();
                gameOver();

            }else{
                updatePregunta(r.nextInt(mPreguntaLenght));
                sstart();}


        }else{
            vidas--;
            btn1.setBackground(getDrawable(R.drawable.box_incorrect));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn1.setBackground(getDrawable(R.drawable.button));

                }

            },200);

            if (vidas == 0) {
                gameOver();

            } else if (vidas == 2) {
            } else if (vidas == 1) {
                dialog4();
                dialog4 = alertDialogbuilder4.create();

                dialog4.show();
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void btn2(View view){
        if (btn2.getText() == manswer){
            mPuntos++;
            btn2.setBackground(getDrawable(R.drawable.box_correct_verde));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn2.setBackground(getDrawable(R.drawable.button));

                }

            },200);
            textPuntos.setText("" +mPuntos);
            Cancel();
            if (mPuntos >= 5) {
                Cancel();
                gameOver();

            }else{
                updatePregunta(r.nextInt(mPreguntaLenght));

                sstart();}
        }else{
            vidas--;
            btn2.setBackground(getDrawable(R.drawable.box_incorrect));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn2.setBackground(getDrawable(R.drawable.button));

                }

            },200);
            if (vidas == 0) {
                gameOver();


            } else if (vidas == 2) {
            } else if (vidas == 1) {
                dialog4();
                dialog4 = alertDialogbuilder4.create();

                dialog4.show();
            }

        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void btn3(View view){
        if (btn3.getText() == manswer){
            mPuntos++;
            btn3.setBackground(getDrawable(R.drawable.box_correct_verde));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn3.setBackground(getDrawable(R.drawable.button));

                }

            },200);
            textPuntos.setText("" +mPuntos);
            Cancel();
            if (mPuntos >= 5) {
                Cancel();
                gameOver();

            }else{
                updatePregunta(r.nextInt(mPreguntaLenght));

                sstart();}

        }else{
            vidas--;
            btn3.setBackground(getDrawable(R.drawable.box_incorrect));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn3.setBackground(getDrawable(R.drawable.button));

                }

            },200);
            if (vidas == 0) {
                gameOver();


            } else if (vidas == 2) {
            } else if (vidas == 1) {
                dialog4();
                dialog4 = alertDialogbuilder4.create();

                dialog4.show();
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btn4(View view){
        if (btn4.getText() == manswer){
            mPuntos++;
            btn4.setBackground(getDrawable(R.drawable.box_correct_verde));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn4.setBackground(getDrawable(R.drawable.button));

                }

            },200);
            textPuntos.setText("" +mPuntos);
            Cancel();
            if (mPuntos >= 5) {
                gameOver();
                Cancel();

            }else{
                updatePregunta(r.nextInt(mPreguntaLenght));

                sstart();}
        }else{
            vidas--;
            btn4.setBackground(getDrawable(R.drawable.box_incorrect));
            new Handler().postDelayed(new Runnable(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btn4.setBackground(getDrawable(R.drawable.button));

                }

            },200);
            if (vidas == 0) {
                gameOver();

            } else if (vidas == 2) {
            } else if (vidas == 1) {
                dialog4();
                dialog4 = alertDialogbuilder4.create();

                dialog4.show();
            }

        }

    }

}
