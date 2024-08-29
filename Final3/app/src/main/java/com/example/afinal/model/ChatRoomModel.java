package com.example.afinal.model;

import java.sql.Timestamp;
import java.util.List;

public class ChatRoomModel {
    String chatRoomId;
    List<String> userIds;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderId;

    public ChatRoomModel() {
    }


    public ChatRoomModel(String chatroomId, List<String> list, com.google.firebase.Timestamp now, String lastMessageSenderId) {
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }
}
