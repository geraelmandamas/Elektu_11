package com.epifi.elektu;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epifi.elektu.Auth.LoginActivity;
import com.epifi.elektu.Auth.RegisterActivity;
import com.epifi.elektu.Utils.MatchingClass;
import com.epifi.elektu.Utils.Puntos;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private AdView mAdView;
    private Button btnEmpezar,btnFlappyCat,btnUpDown;
    private ImageView btnLogout,btnReport,btnSettings,btncanjearPuntos,btnWatchVIdeo,btnMenu,btnPlay;
    private TextView textTotalPuntos;
    private Context mContext;
    private TextView TVDialog,hasobtenido,tusPuntosSon;
    private Button Btn1Dialog,Btn2Dialog,vermispuntos,Btnhome;
    private    int  puntosyaconsegudosparasumar;
    private int sumadeTodosPuntos;
    private String userID;
    private AlertDialog dialog,dialog2,dialog3,dialog4,dialog5;
    private AlertDialog.Builder alertDialogbuilder,alertDialogbuilder2,alertDialogbuilder3,alertDialogbuilder4,alertDialogbuilder5;
    private CheckBox checkbox1, checkbox2;
    private LinearLayout LILA3;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myref;
    private RewardedVideoAd mRewardedVideoAd;
    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final String SITE_KEY = "6LfneV4UAAAAADmCb7UprRUi-x5jMV-q4v9U3_4l";
    public static final String SITE_SECRET_KEY = "6LfneV4UAAAAACCukfOKuB_oo3yqeVCmD4oKX6VE";
    private Boolean BtnMenuB = false;
    private RelativeLayout LayoutMenu;
    private  String Username,Puntos;
    String userResponseToken;
    //Design
    AnimationDrawable anim;
    AssetManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mContext = HomeActivity.this;


        myref = FirebaseDatabase.getInstance().getReference();
        LayoutMenu = findViewById(R.id.LayoutMenuR);
        LayoutMenu.setVisibility(View.GONE);

        initWidgets();



        setUpFirebaseAuth();



        //Firebase Reference
        myref = FirebaseDatabase.getInstance().getReference();



        //Design
       // am = this.getApplicationContext().getAssets();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.RLHome);
        //anim = (AnimationDrawable) container.getBackground();
        //anim.setEnterFadeDuration(100);
        //anim.setExitFadeDuration(1300);




        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);




        //AddBanner
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
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
        loadRewardedVideoAd();


    }

    private void initWidgets(){

        btnMenu = (ImageView) findViewById(R.id.Btnmenu);
        btnPlay = (ImageView) findViewById(R.id.BtnPlay);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {



                if (BtnMenuB == true){
                    btnMenu.setImageDrawable(getDrawable(R.drawable.ic_menu_white));
                     LayoutMenu = findViewById(R.id.LayoutMenuR);
                        BtnMenuB = false;


                   LayoutMenu.setVisibility(View.INVISIBLE);
                }else if (BtnMenuB == false){

                    btnMenu.setImageDrawable(getDrawable(R.drawable.ic_close_white));
                    LayoutMenu.setVisibility(View.VISIBLE);
                    BtnMenuB.equals(true);
                    BtnMenuB = true;


                }





            }
        });









    }

    private void setUpFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);
                if (user != null) {
                    //userID = mAuth.getCurrentUser().getUid();
                    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference uidRef = rootRef.child("userspuntos").child(userID);
                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                           // String uID = dataSnapshot.child("userId").getValue(String.class);
                          //  sumadeTodosPuntos = puntosyaconsegudosparasumar + 5;
                             Username = dataSnapshot.child("users").child(userID).child("username").getValue(String.class);
                             Puntos = dataSnapshot.child("users").child(userID).child("puntos").getValue(String.class);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }


            }
        };

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

    //Check if the "user " is signed in
    private void checkCurrentUser(final FirebaseUser user){


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    setUpAuthetication();


                }else{
                    Intent q = new Intent(mContext,CargandoActivity.class);
                    startActivity(q);

                    MatchingClass matchingClass = new MatchingClass(Username,Puntos);
                    String matchingClassId = myref.push().getKey();
                    myref.child("Games").child(matchingClassId).setValue(matchingClass);






                }



            }
        });






    }
    private void setUpAuthetication(){
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(mContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_auth,null);
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
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }
    @Override
    public void onRewardedVideoAdLoaded() {


    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Puntos puntos = new Puntos(sumadeTodosPuntos ,userID);
        myref.child("userspuntos").child(userID).setValue(puntos);
        myref.child("users").child(userID).child("puntos").setValue(sumadeTodosPuntos);
        Toast.makeText(mContext, "+ 5 puntos", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
    }

    @Override
    public void onRewardedVideoCompleted() {

    }




}
