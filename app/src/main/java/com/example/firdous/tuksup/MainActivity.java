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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewSignIn;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegister = (Button) findViewById(R.id.registerButton);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        textViewSignIn= (TextView) findViewById(R.id.signInTextview);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override

    public void onClick(View view) {

        if (view == buttonRegister) {

            registerUser();

        }
        else if (view == textViewSignIn) {

            // Open login activity
            startActivity(new Intent(this,LoginActivity.class));

        }
    }

    private void registerUser() {
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

        // validations passed
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task){
                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Registered Successfully", Toast.LENGTH_LONG).show();
                    updateUI();
                }
                else{

                    Toast.makeText(MainActivity.this,"Could not register. Please try again.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void updateUI() {
        startActivity(new Intent(this,LoginActivity.class));
    }

}
