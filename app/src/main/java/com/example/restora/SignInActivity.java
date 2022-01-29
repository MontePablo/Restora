package com.example.restora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import daos.UserDao;
import models.User;

public class SignInActivity extends AppCompatActivity {
    private int RC_SIGN_IN=123;
    GoogleSignInClient mGoogleSignInClient;
    public FirebaseAuth firebaseAuth;
    SignInButton signInButtonSeller;
    SignInButton signInButtonBuyer;
    ProgressBar progressBar;
//    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButtonBuyer=findViewById(R.id.signInButtonBuyer);
//        signInButtonSeller=findViewById(R.id.signInButtonSeller);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("289530324134-9kkk10j1hmlrb6730an75kmktdorrjm9.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButtonBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                userType="Buyer";
                signIn();
            }
        });
//        signInButtonSeller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userType="Seller";
//                signIn();
//            }
//        });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //noinspection deprecation
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("TAG","Exception : "+e.getMessage());
                // Google Sign In failed, update UI appropriately
            }
            Log.d("TAG","onActivityResult finish");
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        Log.d("TAG","idtoken:"+idToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        signInButtonBuyer.setVisibility(View.GONE);
//        signInButtonSeller.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                             Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d("TAG","task.getResult().toString()="+task.getResult().toString());
                            updateUI(user);
                        } else {
//                             If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser!=null) {
            Log.d("TAG","updateUi firebaseUser!=null");

            User user=new User(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString());
            UserDao userDao=new UserDao();
            userDao.addUser(user);
//            if(userType!="Seller"){
                startActivity(new Intent(this, HomeActivity.class));
//            }else{
//                startActivity(new Intent(this,HomeForSeller.class));
//            }
            finish();
        }else{
            Log.d("TAG","firebaseUser==null");

            signInButtonBuyer.setVisibility(View.VISIBLE);
//            signInButtonSeller.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
        Log.d("TAG","onStart finish");
    }
}