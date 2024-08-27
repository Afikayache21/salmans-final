package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.hbb20.CountryCodePicker;

public class LogInActivity extends AppCompatActivity {
CountryCodePicker cp;
EditText phoneInput;
Button sendOtpBtn;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_log_in);

        cp = findViewById(R.id.login_country_code);
        phoneInput = findViewById(R.id.login_mobile_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        //to not see the progress bar at the start
        progressBar.setVisibility(View.GONE);


        cp.registerCarrierNumberEditText(phoneInput);

        sendOtpBtn.setOnClickListener((v)->{
            if(!cp.isValidFullNumber()){
                phoneInput.setError("Phone number not valid");
                return;
            }
            Intent intent = new Intent(LogInActivity.this,LoginOtpActivity.class);
            intent.putExtra("phone",cp.getFullNumberWithPlus());
            startActivity(intent);
        });

    }
}