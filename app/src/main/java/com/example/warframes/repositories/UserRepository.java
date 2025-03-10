package com.example.warframes.repositories;

import com.example.warframes.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;


public class UserRepository {
    private final DatabaseReference productRef;
    private final FirebaseAuth firebaseAuth;


    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        productRef = FirebaseDatabase.getInstance().getReference();
    }

    //Métodos par registrar, guardar datos y logear al usuario

    public Task<AuthResult> registerUser(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<Void> saveUserData(User user) {
        return productRef.child("users")
                .child(user.getUid())
                .setValue(user);
    }

    public Task<AuthResult> loginUser(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }
}
