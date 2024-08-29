package com.example.afinal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.afinal.model.UserModel;
import com.example.afinal.utils.AndoridUtil;
import com.example.afinal.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    ImageView profilePic;
    EditText usernameInput;
    EditText phoneInput;
    ProgressBar progressBar;
    TextView logoutBtn;
    Button updateBtn;
    UserModel cuurentUserModel;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickerLuncher;
    // TODO: Rename parameter arguments, choose names that match

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLuncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndoridUtil.setProfilePic(getContext(), selectedImageUri,profilePic);

                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePic = view.findViewById(R.id.profile_image_view);
        usernameInput = view.findViewById(R.id.profile_username_input);
        phoneInput = view.findViewById(R.id.profile_phone_input);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        updateBtn = view.findViewById(R.id.profile_update_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);
        getUserData();

        updateBtn.setOnClickListener(v -> {
            updateBtnClick();
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseUtil.logout();
            Intent intent = new Intent(getContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        profilePic.setOnClickListener(v->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickerLuncher.launch(intent);
                            return null;
                        }
                    });
        });
        return view;
    }

    void updateBtnClick() {
        String newUsername = usernameInput.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() < 3) {
            usernameInput.setError("Username must be at leat 3 chars");
            return;
        }
        cuurentUserModel.setUsername(newUsername);
        setInProgress(true);


        if (selectedImageUri!=null) {
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri).addOnCompleteListener(task -> {
                updateToFirestore();
            });

        }else {
            updateToFirestore();
        }
    }

    private void updateToFirestore() {
        FirebaseUtil.currentUserDetails().set(cuurentUserModel).addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
                AndoridUtil.showToast(getContext(), "Updated successfully");
            } else {
                AndoridUtil.showToast(getContext(), "Updated failed");
            }
        });
    }

    public void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            updateBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            updateBtn.setVisibility(View.VISIBLE);
        }
    }

    void getUserData() {
        setInProgress(true);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndoridUtil.setProfilePic(getContext(),uri,profilePic);
                            }
                        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            cuurentUserModel = task.getResult().toObject(UserModel.class);
            usernameInput.setText(cuurentUserModel.getUsername());
            phoneInput.setText(cuurentUserModel.getPhone());
        });
    }


}