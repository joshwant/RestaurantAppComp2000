package com.example.assessment2restaurantapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Map<String, String> userCredentials;
    private SharedViewModel sharedViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signupText = findViewById(R.id.signupText);
        Button loginButton = findViewById(R.id.loginButton);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        TextView errorMessage = findViewById(R.id.loginErrorMessage);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        userCredentials = new HashMap<>();
        //Users
        userCredentials.put("user", "1234");
        userCredentials.put("john@email.com", "password");
        userCredentials.put("", "");

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                //Working authentication system:
                /*if(userCredentials.containsKey(enteredEmail) && userCredentials.get(enteredEmail).equals(enteredPassword)){
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    //Open home activity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("enteredEmail", enteredEmail);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    errorMessage.setVisibility(View.VISIBLE);
                }*/

                //Regular login for assignment marking purposes:
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                //Open home activity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("enteredEmail", enteredEmail);

                startActivity(intent);
            }
        });
    }
}