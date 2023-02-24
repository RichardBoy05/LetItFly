package com.richardmeoli.letitfly.logic.users.authentication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.richardmeoli.letitfly.logic.database.online.firestore.Firestore;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreAttributes;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnSingleQueryCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthUserExistsCallback;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnAccountRegistrationCallback;

import java.util.Map;

public class Authenticator implements FirestoreAttributes {

    private static Authenticator instance;

    private Authenticator() {
    }

    public static Authenticator getInstance() { // singleton pattern

        if (instance == null) {
            instance = new Authenticator();
        }

        return instance;
    }

    //--------------- Authentication tasks ---------------//

    public void checkUsernameUnique(String username, final AuthUserExistsCallback callback) {

        Firestore fs = Firestore.getInstance();

        fs.selectDocumentById(USERS_COLLECTION, null, username, new FirestoreOnSingleQueryCallback() {
            @Override
            public void onSuccess(Map<String, Object> list) {
                callback.onUserExists(list.size() != 0);

            }

            @Override
            public void onFailure(FirestoreError error) {
                callback.onUserExists(true); // pass true because in case of errors i cant be sure about its existence or not,
                // so i will stop the registering progress

            }
        });

    }

    public void sendVerficationEmail(String email) {
        System.out.println("Sending auth mail");
    }

    public void registerUser(String username, String email, String password, final AuthOnAccountRegistrationCallback callback) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser user = auth.getCurrentUser();

                    if (user == null) {
                        callback.onFailure("Failed account creation! User is null!");
                        return;
                    }

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnSuccessListener(unused -> {

                                Firestore fs = Firestore.getInstance();
                                fs.addDocument(USERS_COLLECTION, username, new String[]{user.getDisplayName()}, new FirestoreOnTransactionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        callback.onSuccess(user);
                                    }

                                    @Override
                                    public void onFailure(FirestoreError error) {
                                        callback.onFailure(error.toString());
                                    }
                                });

                            }).addOnFailureListener(e -> callback.onFailure("Couldn't update profile!"));


                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });

    }

    public void loginUser(String emailOrUsername, String password) {
    }

    public void logOutUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }


    public void resetPassword(String emailOrUsername) {
    }

}
