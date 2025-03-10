package com.example.warframes.repositories;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRepository {
    private final DatabaseReference usersRef;
    private final FirebaseAuth firebaseAuth;

    public FavoriteRepository(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase) {
        this.firebaseAuth = firebaseAuth;
        this.usersRef = firebaseDatabase.getReference("users");
    }

    // Añadir o eliminar un favorito
    public void toggleFavorite(String warframeName, OnFavoriteToggleListener listener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            listener.onError(new Exception("Usuario no autenticado"));
            return;
        }
        String userId = currentUser.getUid();
        Log.d("FavoriteRepository", "Usuario autenticado, ID: " + userId);

        // Uso el name en lugar del id para almacenar en Firebase
        DatabaseReference userFavoritesRef = usersRef.child(userId).child("favorites").child(warframeName);

        userFavoritesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                if (task.getResult().exists()) {
                    // Eliminar favorito si ya está
                    userFavoritesRef.removeValue()
                            .addOnSuccessListener(aVoid -> listener.onSuccess(false))
                            .addOnFailureListener(listener::onError);
                } else {
                    // Añadir favorito
                    userFavoritesRef.setValue(true)
                            .addOnSuccessListener(aVoid -> listener.onSuccess(true))
                            .addOnFailureListener(listener::onError);
                }
            } else {
                listener.onError(task.getException());
            }
        });
    }

    // Verificar si un warframe es favorito
    public void checkIsFavorite(String warframeName, MutableLiveData<Boolean> isFavoriteLiveData) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userFavoritesRef = usersRef.child(userId).child("favorites").child(warframeName);

        userFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isFavoriteLiveData.setValue(snapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                isFavoriteLiveData.setValue(false);
            }
        });
    }

    // Leer favoritos del usuario
    public void getFavoriteWarframes(MutableLiveData<List<String>> favoritesLiveData) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userFavoritesRef = usersRef.child(userId).child("favorites");

        userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> warframeFav = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    warframeFav.add(child.getKey());
                }
                favoritesLiveData.setValue(warframeFav);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FavoriteRepository", "Error en onCancelled de get");
                favoritesLiveData.setValue(new ArrayList<>());  // Error al leer

            }
        });
    }

    public interface OnFavoriteToggleListener {
        void onSuccess(boolean newFavoriteStatus);
        void onError(Exception e);
    }
}