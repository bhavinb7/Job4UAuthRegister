package com.example.dhoni.authregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {
    public static final int GOOGLE_SIGN_IN_CODE = 1005;
    EditText email,pass;
    Button login;
    SignInButton Signin;
    TextView create,forgot;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.pass);
        login = (Button)findViewById(R.id.login_btn);
        create = (TextView)findViewById(R.id.create_acc);
        Signin = (SignInButton)findViewById(R.id.google_signin);
        forgot = (TextView)findViewById(R.id.forgot_pass);
        mAuth = FirebaseAuth.getInstance();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("883060249315-opo92gmr1jekkdj133gv7n6u2qa528lb.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(LoginActivity.this,gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

        if(signInAccount != null)
        {
            Intent i = new Intent(LoginActivity.this,SelectCategory.class);
        }
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = signInClient.getSignInIntent();
                startActivityForResult(sign,GOOGLE_SIGN_IN_CODE);

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(i);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }

            private void userlogin() {
                String Email = email.getText().toString();
                String Pass = pass.getText().toString();

                if(Email.isEmpty())
                {
                    email.setError("Email is Required");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Please Enter valid email");
                    email.requestFocus();
                    return;
                }
                if(Pass.isEmpty())
                {
                    pass.setError("Password is reuired");
                    pass.requestFocus();
                    return;
                }
                if(Pass.length() < 6)
                {
                    pass.setError("Min Length of password is 6 characters");
                    pass.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified())
                            {
                                Intent i = new Intent(LoginActivity.this, SelectCategory.class);
                                startActivity(i);
                                finishAfterTransition();
                            }
                            else {
                                user.sendEmailVerification();
                                Toast.makeText(LoginActivity.this, "Please check your email & verify to login", Toast.LENGTH_LONG).show();
                            }

                        }else
                        {
                            Toast.makeText(LoginActivity.this, "Failed to Login! Please Check Your Credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this,Signup.class);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount>signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signinacc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signinacc.getIdToken(),null);

                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent i = new Intent(LoginActivity.this,SelectCategory.class);
                        startActivity(i);

                    }
                });

                Toast.makeText(this, "Your Account is connected to our application", Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

    }
}