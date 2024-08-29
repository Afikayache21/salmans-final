package com.example.afinal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.ChatActivity;
import com.example.afinal.R;
import com.example.afinal.model.UserModel;
import com.example.afinal.utils.AndoridUtil;
import com.example.afinal.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.auth.User;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewModel> {

    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull UserModelViewModel holder, int position, @NonNull UserModel model) {
        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
        if (model.getUserId().equals((FirebaseUtil.currentUserId()))) {
            holder.usernameText.setText(model.getUsername() + " (Me)");
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndoridUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public UserModelViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewModel(view);
    }

    class UserModelViewModel extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;


        public UserModelViewModel(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.user_phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }

}
