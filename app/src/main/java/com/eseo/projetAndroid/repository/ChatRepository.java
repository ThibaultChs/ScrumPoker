package com.eseo.projetAndroid.repository;

import android.util.Log;

import com.eseo.projetAndroid.manager.UserManager;
import com.eseo.projetAndroid.models.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public final class ChatRepository {

    private static final String SALON_COLLECTION = "salons";
    private static final String MESSAGE_COLLECTION = "messages";
    private static volatile ChatRepository instance;
    private UserManager userManager;

    private ChatRepository() { this.userManager = UserManager.getInstance(); }


    public static ChatRepository getInstance() {
        ChatRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ChatRepository.class) {
            if (instance == null) {
                instance = new ChatRepository();
            }
            return instance;
        }
    }

    public CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(SALON_COLLECTION);
    }

    public Query getAllMessageForChat(String salon){
        return this.getChatCollection()
                .document(salon)
                .collection(MESSAGE_COLLECTION)
                .orderBy("dateCreated")
                .limit(50);
    }

    public void createMessageForChat(String textMessage, String salon){

        userManager.getUserData().addOnSuccessListener(user -> {
            // Create the Message object
            Message message = new Message(textMessage, user);

            // Store Message to Firestore
            this.getChatCollection()
                    .document(salon)
                    .collection(MESSAGE_COLLECTION)
                    .add(message);
        });

    }

}