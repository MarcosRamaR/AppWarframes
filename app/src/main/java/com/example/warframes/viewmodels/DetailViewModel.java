package com.example.warframes.viewmodels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warframes.repositories.FavoriteRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DetailViewModel extends ViewModel {
    private final FavoriteRepository repository;
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public DetailViewModel() {
        repository = new FavoriteRepository(
                FirebaseAuth.getInstance(),
                FirebaseDatabase.getInstance()
        );
    }

    public LiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void checkIsFavorite(String warframeName) {
        repository.checkIsFavorite(warframeName, isFavorite);
    }

    public void toggleFavorite(String warframeName) {
        Log.d("DetailViewModel", "Intentando agregar/quitar favorito para el nombre: " + warframeName);
        repository.toggleFavorite(warframeName, new FavoriteRepository.OnFavoriteToggleListener() {
            @Override
            public void onSuccess(boolean newFavoriteStatus) {
                isFavorite.setValue(newFavoriteStatus); // Actualiza el botón
                Log.d("Favorite", "Estado del favorito actualizado con éxito");
            }

            @Override
            public void onError(Exception e) {
                errorMessage.setValue("Error al actualizar favoritos: " + e.getMessage());
                Log.e("Favorite", "Error al actualizar favoritos", e);
            }
        });
    }
}
