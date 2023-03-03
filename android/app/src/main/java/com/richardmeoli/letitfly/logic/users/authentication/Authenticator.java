package com.richardmeoli.letitfly.logic.users.authentication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.richardmeoli.letitfly.ui.authentication.LoginActivity;
import com.richardmeoli.letitfly.logic.database.online.firestore.Firestore;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreError;
import com.richardmeoli.letitfly.logic.database.online.firestore.FirestoreAttributes;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthOnEventCallback;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthUserExistsCallback;
import com.richardmeoli.letitfly.logic.users.authentication.callbacks.AuthGetEmailCallback;
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

    //--------------- Authenticator public methods ---------------//

    public void checkUsernameUnique(String username, final AuthUserExistsCallback callback) {
        // Checks if a user exists based on their username.

        Firestore fs = Firestore.getInstance();

        fs.selectDocumentById(USERS_COLLECTION, null, username, new FirestoreOnSingleQueryCallback() {
            @Override
            public void onSuccess(Map<String, Object> map) {
                callback.onUserExists(map.size() != 0);
                // passes false to the callback if the map is empty (size = 0), indicating that the user does not exist;
                // otherwise, passes true to the callback, indicating that the user exists.

            }

            @Override
            public void onFailure(FirestoreError error) {

                if (error == FirestoreError.NO_SUCH_DOCUMENT) {
                    // the document doesn't exist, which means the user doesn't exist either.
                    callback.onUserExists(false);
                    return;
                }

                callback.onUserExists(true);
                // an unexpected error occurred, so pass true to stop
                // the code flow and avoid continuing with invalid data.

            }
        });

    }

    public void registerUser(String username, String email, String password, final AuthOnEventCallback callback) {
        // Registers a new account based on an email and password, assigns a unique username
        // to the account, and stores a user object into the Firestore database.

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password) // creating user
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) { // failed user creation

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Log.w(TAG, AuthenticationError.USER_COLLISION_ERROR.toString());
                            callback.onFailure(AuthenticationError.USER_COLLISION_ERROR);
                            return;
                        }

                        // unexpected error
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

                    // user created successfully

                    Log.d(TAG, "Successfully created user with email: " + user.getEmail());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    user.updateProfile(profileUpdates) // assigning username
                            .addOnSuccessListener(unused -> {
                                Log.d(TAG, "Successfully updated '" + user.getEmail() + "' username to: " + username);

                                Firestore fs = Firestore.getInstance();

                                fs.addDocument(USERS_COLLECTION, username, new String[]{user.getEmail(), user.getUid()}, new FirestoreOnTransactionCallback() {

                                    // storing user to Firestore
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "Successfully associated '" + username + "' username to its uid: " + user.getUid());
                                        callback.onSuccess();
                                    }

                                    @Override
                                    public void onFailure(FirestoreError error) { // unexpected error
                                        auth.signOut();
                                        user.delete();
                                        Log.e(TAG, error.toString());
                                        callback.onFailure(AuthenticationError.FIRESTORE_ERROR);
                                    }
                                });

                            })
                            .addOnFailureListener(e -> { // username assignment error
                                auth.signOut();
                                user.delete();
                                Log.e(TAG, AuthenticationError.USERNAME_ASSIGNMENT_ERROR.toString(), e);
                                callback.onFailure(AuthenticationError.USERNAME_ASSIGNMENT_ERROR);


                            });

                });


    }

    public void sendVerificationEmail(final AuthOnEventCallback callback) {
        // Sends an email to the user to verify their email address.

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


    public void signInUser(String emailOrUsername, String password, final AuthOnEventCallback callback) {
        // Signs in the user based on their email or username and their password.

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {
            // handles the case where the entered field assumes an email pattern

            signInUserByEmail(emailOrUsername, password, new AuthOnEventCallback() {
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
            // handles the case where the entered field does not assume an email pattern, indicating a username

            retriveEmailFromUsername(emailOrUsername, new AuthGetEmailCallback() {
                @Override
                public void onSuccess(String email) {
                    signInUserByEmail(email, password, new AuthOnEventCallback() {
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
        // Signs out the currently logged-in user.

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

    }

    public void isUserVerified(final AuthOnEventCallback callback) {
        // Checks if the current user's account has been verified.

        if (getCurrentUser() == null){
            callback.onFailure(AuthenticationError.NO_USER_LOGGED_IN_ERROR);
            return;
        }

        reload(new AuthOnEventCallback() {
            @Override
            public void onSuccess() {

                if (getCurrentUser().isEmailVerified()) {

                    Log.d(TAG, "The user '" + getCurrentUser() + "' is a verified account!");
                    callback.onSuccess();

                } else {
                    Log.w(TAG, AuthenticationError.USER_NOT_VERIFIED_ERROR.toString());
                    callback.onFailure(AuthenticationError.USER_NOT_VERIFIED_ERROR);
                }

            }

            @Override
            public void onFailure(AuthenticationError error) {
                callback.onFailure(error);

            }
        });
    }

    public void resetPassword(String emailOrUsername, final AuthOnEventCallback callback) {
        // Sends a password reset email to the user associated with the specified email or username.

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {
            // handles the case where the entered field assumes an email pattern

            performPasswordReset(emailOrUsername, new AuthOnEventCallback() {
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
            // handles the case where the entered field does not assume an email pattern, indicating a username

            retriveEmailFromUsername(emailOrUsername, new AuthGetEmailCallback() {
                @Override
                public void onSuccess(String email) {

                    performPasswordReset(email, new AuthOnEventCallback() {
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
        // Returns the currently logged-in Firebase user, or null if no user is currently logged in.

        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser();
    }


    public void reload(final AuthOnEventCallback callback) {
        // Reloads the current user's profile data from the Firebase server.

        getCurrentUser().reload()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Log.d(TAG, "User data reloaded successfully!");
                        callback.onSuccess();

                    } else { // unexpected error

                        Log.e(TAG, AuthenticationError.USER_RELOADING_ERROR.toString(), task.getException());
                        callback.onFailure(AuthenticationError.USER_RELOADING_ERROR);
                    }
                });

    }

    public void redirectToLoginActivity(Context context){
        // Redirects the user to the Login activity.

        Intent myIntent = new Intent(context, LoginActivity.class);
        context.startActivity(myIntent);

    }


    //--------------- Authenticator private methods ---------------//

    private void retriveEmailFromUsername(String username, final AuthGetEmailCallback callback) {
        // Retrieves the email associated with the specified username from the Firestore database.

        Firestore fs = Firestore.getInstance();

        fs.selectDocumentById(USERS_COLLECTION, new String[]{U_FIELD_EMAIL}, username, new FirestoreOnSingleQueryCallback() {
            @Override
            public void onSuccess(Map<String, Object> map) {

                if (map.size() == 0 || map.get(U_FIELD_EMAIL) == null) { // non-existent user
                    Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString());
                    callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                    return;
                }

                // the email field cannot be null because it was checked in the previous if statement
                @SuppressWarnings("ConstantConditions")
                String email = map.get(U_FIELD_EMAIL).toString();
                callback.onSuccess(email);


            }

            @Override
            public void onFailure(FirestoreError error) {

                if (error == FirestoreError.NO_SUCH_DOCUMENT) {
                    // the document doesn't exist, which means the user doesn't exist either

                    Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString());
                    callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                    return;

                }

                Log.e(TAG, error.toString()); // unexpected error
                callback.onFailure(AuthenticationError.FIRESTORE_ERROR);
            }
        });

    }

    private void signInUserByEmail(String email, String password, final AuthOnEventCallback callback) {
        // Utility method for signing in a user with an email and password combination.
        // This method is private because the class already has a public method (signInUser)
        // that handles both email and username-based sign-in operations.

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully signed in user with email: " + getCurrentUser().getEmail());
                        callback.onSuccess();

                    } else { // handles multiple exceptions

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

                        // unexpected error
                        Log.e(TAG, AuthenticationError.UNEXPECTED_ERROR.toString(), task.getException());
                        callback.onFailure(AuthenticationError.UNEXPECTED_ERROR);
                    }
                });


    }

    private void performPasswordReset(String email, final AuthOnEventCallback callback) {
        // Utility method for resetting the password for an email account.
        // This method is private because the class already has a public method (resetPassword)
        // that handles both email and username-based password reset operations.

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)

                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.d(TAG, "Reset password email successfully sent!");
                        callback.onSuccess();

                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            Log.w(TAG, AuthenticationError.USER_DOES_NOT_EXIST_ERROR.toString(), task.getException());
                            callback.onFailure(AuthenticationError.USER_DOES_NOT_EXIST_ERROR);
                            return;
                        }

                        // unexpected error
                        Log.e(TAG, AuthenticationError.EMAIL_RESET_ERROR.toString(), task.getException());
                        callback.onFailure(AuthenticationError.EMAIL_RESET_ERROR);
                    }
                });


    }

}