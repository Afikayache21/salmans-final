package com.example.afinal.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(currentUserId());
    }
    public static DocumentReference getChatRoomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageRefrence(String chatroomId){
        return getChatRoomReference(chatroomId).collection("chats");
    }


    public static boolean isLoggedIn(){
        return currentUserId() != null;
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId()).getParent();
    }
    public static String getChatRoomId(String uid1, String uid2){
        if (uid1.hashCode()<uid2.hashCode()){
            return uid1+"_"+uid2;
        }else {
            return uid2+"_"+uid1;
        }
    }

    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserId());
    }
    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
