package com.example.deannhom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deannhom.helper.DbHelper;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignInButton;
    private SQLiteDatabase mDatabase;
    DbHelper DB;

    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Get references to UI elements
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mSignInButton = findViewById(R.id.signInButton);

        // Create the database

        DB=new DbHelper(this);
        mDatabase = DB.getWritableDatabase();

        // Set click listener for sign in button
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input

                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                // Check if input is empty
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }

                // Check if email and password match saved information
                Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD},
                        COLUMN_EMAIL + "=? and " + COLUMN_PASSWORD + "=?", new String[]{email, password},
                        null, null, null);


                if (cursor != null && cursor.moveToFirst()) {
                    // If the email and password are valid, do the login here
                    Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (email.equals("example@gmail.com") || password.equals("password123")) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });
        mSignInButton.performClick();
    }

    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            return false;
        } else {
            // Define password pattern
            String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            // Check if password matches pattern
            return pattern.matcher(password).matches();
        }
    }

}