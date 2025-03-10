package com.example.warframes.viewmodels;


import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warframes.models.Warframe;
import com.example.warframes.repositories.FavoriteRepository;
import com.example.warframes.repositories.RandomRepository;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;



public class RandomViewModel extends ViewModel {
    private final MutableLiveData<Warframe> warframeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final RandomRepository randomRepository;


    public RandomViewModel() {
        randomRepository = new RandomRepository();
        loadRandomWarframe();
    }

    public LiveData<Warframe> getWarframeLiveData() {
        return warframeLiveData;
    }


    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadRandomWarframe() {
        randomRepository.getRandomWarframe(warframeLiveData, errorMessage);

    }

}