package com.richardmeoli.letitfly.logic.users.authentication;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.richardmeoli.letitfly.logic.database.online.firestore.Firestore;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreAttributes;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnActionCallback;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthUserExistsCallback;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthRetrieveEmailCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnTransactionCallback;
import com.richardmeoli.letitfly.logic.database.online.callbacks.FirestoreOnSingleQueryCallback;

import java.util.Map;

public class Authenticator implements FirestoreAttributes {

    private static Authenticator instance;
    private static final String TAG = "Authenticator";

    private Authenticator() {
    }

    public static Authenticator getInstance() { // singleton pattern

        if (instance == null) {
            instance = new Authenticator();
        }

        return instance;
    }

    //--------------- Authentication public methods ---------------//

    public void checkUsernameUnique(String username, final AuthUserExistsCallback callback) {

        Firestore fs = Firestore.getInstance();

        fs.selectDocumentById(USERS_COLLECTION, null, username, new FirestoreOnSingleQueryCallback() {
            @Override
            public void onSuccess(Map<String, Object> map) {
                callback.onUserExists(map.size() != 0);

            }

            @Override
            public void onFailure(FirestoreError error) {

                if (error == FirestoreError.NO_SUCH_DOCUMENT) { // it implies the user does not exist either
                    callback.onUserExists(false);
                    return;
                }

                callback.onUserExists(true); // pass true in case of unexpected errors (in order to stop the code flow)

            }
        });

    }

    public void registerUser(String username, String email, String password, final AuthOnActionCallback callback) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Log.w(TAG, AuthenticationError.USER_COLLISION_ERROR.toString());
                            callback.onFailure(AuthenticationError.USER_COLLISION_ERROR);
                            return;
                        }

                        Log.e(TAG, AuthenticationError.UNEXPECTED_ERROR.toString(), task.getException());
                        callback.onFailure(AuthenticationError.UNEXPECTED_ERROR);
                        return;
                    }

                    FirebaseUser user = auth.getCurrentUser();

                    if (user == null) {
                        Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString());
                        callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                        return;
                    }

                    Log.d(TAG, "Successfully created user with email: " + user.getEmail());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnSuccessListener(unused -> {
                                Log.d(TAG, "Successfully updated '" + user.getEmail() + "' username to: " + username);

                                Firestore fs = Firestore.getInstance();

                                fs.addDocument(USERS_COLLECTION, username, new String[]{user.getEmail(), user.getUid()}, new FirestoreOnTransactionCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "Successfully associated '" + username + "' username to its uid: " + user.getUid());
                                        callback.onSuccess();
                                    }

                                    @Override
                                    public void onFailure(FirestoreError error) {
                                        auth.signOut();
                                        user.delete();
                                        Log.e(TAG, error.toString());
                                        callback.onFailure(AuthenticationError.FIRESTORE_ERROR);
                                    }
                                });

                            })
                            .addOnFailureListener(e -> {
                                auth.signOut();
                                user.delete();
                                Log.e(TAG, AuthenticationError.USERNAME_ASSIGNMENT_ERROR.toString(), e);
                                callback.onFailure(AuthenticationError.USERNAME_ASSIGNMENT_ERROR);


                            });

                });


    }

    public void sendVerificationEmail(final AuthOnActionCallback callback) {

        getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Verification email successfully sent!");
                        callback.onSuccess();
                    } else {
                        Log.w(TAG, AuthenticationError.EMAIL_VERIFICATION_ERROR.toString());
                        callback.onFailure(AuthenticationError.EMAIL_VERIFICATION_ERROR);
                    }
                });
    }


    public void signInUser(String emailOrUsername, String password, final AuthOnActionCallback callback) {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {

            signInUserByEmail(emailOrUsername, password, new AuthOnActionCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    callback.onFailure(error);
                }
            });

        } else {

            retriveEmailFromUsername(emailOrUsername, new AuthRetrieveEmailCallback() {
                @Override
                public void onSuccess(String email) {
                    signInUserByEmail(email, password, new AuthOnActionCallback() {
                        @Override
                        public void onSuccess() {
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(AuthenticationError error) {
                            callback.onFailure(error);
                        }
                    });
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    callback.onFailure(error);
                }
            });


        }

    }

    public void signOutUser() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    public void isAccountVerified(final AuthOnActionCallback callback) {

        reload(new AuthOnActionCallback() {
            @Override
            public void onSuccess() {

                if (getCurrentUser().isEmailVerified()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(AuthenticationError.ACCOUNT_NOT_VERIFIED_ERROR);
                }

            }

            @Override
            public void onFailure(AuthenticationError error) {
                callback.onFailure(error);

            }
        });
    }

    public void resetPassword(String emailOrUsername, final AuthOnActionCallback callback) {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {

            performPasswordReset(emailOrUsername, new AuthOnActionCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    callback.onFailure(error);
                }
            });

        } else {
            retriveEmailFromUsername(emailOrUsername, new AuthRetrieveEmailCallback() {
                @Override
                public void onSuccess(String email) {

                    performPasswordReset(email, new AuthOnActionCallback() {
                        @Override
                        public void onSuccess() {
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(AuthenticationError error) {
                            callback.onFailure(error);
                        }
                    });
                }

                @Override
                public void onFailure(AuthenticationError error) {
                    callback.onFailure(error);
                }
            });

        }


    }

    public FirebaseUser getCurrentUser() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser();
    }


    public void reload(final AuthOnActionCallback callback) {

        getCurrentUser().reload()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(AuthenticationError.USER_RELOADING_ERROR);
                    }
                });

    }


    //--------------- Authentication private methods ---------------//

    private void retriveEmailFromUsername(String username, final AuthRetrieveEmailCallback callback) {

        Firestore fs = Firestore.getInstance();

        fs.selectDocumentById(USERS_COLLECTION, new String[]{U_FIELD_EMAIL}, username, new FirestoreOnSingleQueryCallback() {
            @Override
            public void onSuccess(Map<String, Object> map) {

                if (map.size() == 0 || map.get(U_FIELD_EMAIL) == null) {
                    Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString());
                    callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                    return;
                }

                @SuppressWarnings("ConstantConditions") // it cannot be null because i have checked it in the previous if statement
                String email = map.get(U_FIELD_EMAIL).toString();
                callback.onSuccess(email);


            }

            @Override
            public void onFailure(FirestoreError error) {

                if (error == FirestoreError.NO_SUCH_DOCUMENT) {
                    Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString());
                    callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                    return;

                }

                Log.e(TAG, error.toString());
                callback.onFailure(AuthenticationError.FIRESTORE_ERROR);
            }
        });

    }

    private void signInUserByEmail(String email, String password, final AuthOnActionCallback callback) { // privated because the class already has a public method which handles both email and username signing in functions

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Log.d(TAG, "Successfully signed in user with email: " + getCurrentUser().getEmail());
                        callback.onSuccess();

                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.w(TAG, AuthenticationError.INVALID_PASSWORD_ERROR.toString());
                            callback.onFailure(AuthenticationError.INVALID_PASSWORD_ERROR);
                            return;
                        }

                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString());
                            callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                            return;
                        }

                        if (task.getException() instanceof FirebaseTooManyRequestsException) {
                            Log.w(TAG, AuthenticationError.TOO_MANY_REQUESTS_ERROR.toString());
                            callback.onFailure(AuthenticationError.TOO_MANY_REQUESTS_ERROR);
                            return;
                        }

                        Log.e(TAG, AuthenticationError.UNEXPECTED_ERROR.toString(), task.getException());
                        callback.onFailure(AuthenticationError.UNEXPECTED_ERROR);
                    }
                });


    }

    private void performPasswordReset(String email, final AuthOnActionCallback callback) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Reset password email successfully sent!");
                        callback.onSuccess();
                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                            return;
                        }

                        Log.e(TAG, AuthenticationError.EMAIL_RESET_ERROR.toString(), task.getException());
                        callback.onFailure(AuthenticationError.EMAIL_RESET_ERROR);
                    }
                });


    }

}