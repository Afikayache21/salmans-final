package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.utils.AndoridUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    String  phoneNumber;
    Long timeSec =60L;
    String  verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    EditText otpInput;

    Button nextBtn;
    ProgressBar pg;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView resendOtpTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otpInput = findViewById(R.id.otp_input);
        nextBtn = findViewById(R.id.login_next_btn);
        pg = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_text_view);  // Initialize resendOtpTextView

        phoneNumber = getIntent().getExtras().getString("phone");
        Toast.makeText(getApplicationContext(),phoneNumber, Toast.LENGTH_SHORT).show();


        sendOtp(phoneNumber,false);

        nextBtn.setOnClickListener(v -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
            signIn(credential);
            setInProgress(true);
        });

        resendOtpTextView.setOnClickListener(v->{
            sendOtp(phoneNumber,true);
        });
    }

   public void sendOtp(String phoneNumber,boolean isResend){
        startResendTimer();
       setInProgress(true);
       PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
               .setPhoneNumber(phoneNumber)
               .setTimeout(timeSec, TimeUnit.SECONDS)
               .setActivity(this)
               .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                   @Override
                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                       signIn(phoneAuthCredential);
                       setInProgress(false);
                   }

                   @Override
                   public void onVerificationFailed(@NonNull FirebaseException e) {
                       AndoridUtil.showToast(getApplicationContext(),"OTP verification failed");
                       Log.w("FirebaseAuth", e.getMessage());
                       setInProgress(false);
                   }

                   @Override
                   public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       super.onCodeSent(s, forceResendingToken);
                       verificationCode = s;
                       resendingToken = forceResendingToken;
                       AndoridUtil.showToast(getApplicationContext(),"OTP sent succsessfuly");
                       setInProgress(false);
                   }
               });

       if (isResend){
           PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
       }else{
           PhoneAuthProvider.verifyPhoneNumber(builder.build());
       }
    }

    private void startResendTimer() {
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                timeSec--;
                resendOtpTextView.setText("Resend OTP in "+timeSec+"seconds");
                if(timeSec<=0){
                    timeSec = 60L;
                    timer.cancel();
                    runOnUiThread(()->{
                        resendOtpTextView.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }

    public void setInProgress(boolean inProgress){
        if (inProgress){
            pg.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }else{
            pg.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    public void signIn(PhoneAuthCredential credential) {
        setInProgress(true);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setInProgress(false);
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginOtpActivity.this,LoginUsernameActivity.class);
                            intent.putExtra("phone",phoneNumber);
                            startActivity(intent);
                        }else{
                            AndoridUtil.showToast(getApplicationContext(),"OTP verification faild");
                        }

                    }

                });
    }
}