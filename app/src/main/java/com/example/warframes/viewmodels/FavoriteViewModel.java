package com.example.warframes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warframes.models.Warframe;
import com.example.warframes.repositories.DashboardRepository;
import com.example.warframes.repositories.FavoriteRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;
    private final DashboardRepository dashboardRepository;
    private final MutableLiveData<List<Warframe>> favoriteWarframes = new MutableLiveData<>();
    private final MutableLiveData<List<String>> warframeName = new MutableLiveData<>();
    private final MutableLiveData<List<Warframe>> allWarframes = new MutableLiveData<>();

    public FavoriteViewModel() {
        favoriteRepository = new FavoriteRepository(
                FirebaseAuth.getInstance(),
                FirebaseDatabase.getInstance()
        );
        dashboardRepository = new DashboardRepository();
        loadFavorites();
    }

    public LiveData<List<Warframe>> getFavoriteWarframes() {
        return favoriteWarframes;
    }

    private void loadFavorites() {
        //Obtiene los favoritos del usuario y los guarda en warframeName
        favoriteRepository.getFavoriteWarframes(warframeName);
        //Obtiene todos los warframes que haya
        dashboardRepository.getWarframes(allWarframes);
        //Al cambiar el contenido de warframeName o allWarframes llama a updateFavoriteWarframe
        warframeName.observeForever(names -> updateFavoriteWarframes()); //Devuelve los favoritos pero sin informacion
        allWarframes.observeForever(warframes -> updateFavoriteWarframes()); //Proporciona los datos de cada Warframe
    }

    //Actualizar favoritos
    private void updateFavoriteWarframes() {
        List<String> names = warframeName.getValue();
        List<Warframe> warframes = allWarframes.getValue();

        // Si una de las listas es nula, no hagas nada hasta que ambas tengan datos
        if (names == null || warframes == null) {
            return;
        }

        List<Warframe> favorites = new ArrayList<>();
        for (Warframe warframe : warframes) {
            if (names.contains(warframe.getName())) {
                favorites.add(warframe);
            }
        }

        // Solo actualiza si hay cambios
        if (!favorites.equals(favoriteWarframes.getValue())) {
            favoriteWarframes.setValue(favorites);
        }
    }
}