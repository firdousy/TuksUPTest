package com.example.firdous.tuksup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firdous.tuksup.Singletons.SessionSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSignIn;
    EditText editTextEmail;
    EditText editTextPass;
    TextView signUpTextView;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        buttonSignIn = (Button) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);

        buttonSignIn.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){
            //user is logged in
            updateUI();
        }

    }

    private void updateUI() {
        SessionSingleton.getInstance().setUserId(firebaseAuth.getCurrentUser().getUid());
        startActivity(new Intent(this,BottomNavActivity.class));
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {

            userLogin();

        }
        else if (view == signUpTextView){

            finish();

            startActivity(new Intent(this,MainActivity.class));

        }
    }

    private void userLogin(){

        String email = editTextEmail.getText().toString().trim();

        String pass = editTextPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter email", Toast.LENGTH_LONG).show();

            return;
        }
        else if (TextUtils.isEmpty(pass)){

            //pass is empty

            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();

            return;

        }

        // validation complete
        //Firebase login
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login successfully", Toast.LENGTH_LONG).show();
                    updateUI();
                }else{
                    Toast.makeText(LoginActivity.this,"Could not login. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
