package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.afinal.model.UserModel;
import com.example.afinal.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsernameActivity extends AppCompatActivity {
    EditText usernameInput;
    Button letMeInBtn;
    ProgressBar pg;
    String phoneNumber;
    UserModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);
        usernameInput = findViewById(R.id.login_username);
        letMeInBtn = findViewById(R.id.login_lmi);
        pg = findViewById(R.id.login_progress_bar);

        phoneNumber=getIntent().getExtras().getString("phone");
        getUsername();

        letMeInBtn.setOnClickListener(v ->{
            setUsername();
        });
    }
    private void setUsername(){
        setInProgress(true);
        String username = usernameInput.getText().toString();
        if (username.isEmpty()||username.length()<3){
            usernameInput.setError("Username must be at leat 3 chars");
            return;
        }

        if(user!=null){
            user.setUsername(username);
        }else {
            user=new UserModel(phoneNumber,username, Timestamp.now(),FirebaseUtil.currentUserId());
        }
        FirebaseUtil.currentUserDetails().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginUsernameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    private void getUsername() {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                  user =   task.getResult().toObject(UserModel.class);
                 if (user != null){
                     usernameInput.setText(user.getUsername());
                 }
                }
            }
        });
    }

    public void setInProgress(boolean inProgress){
        if (inProgress){
            pg.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        }else{
            pg.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }

}