package com.example.warframes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warframes.models.Warframe;
import com.example.warframes.repositories.DashboardRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel {
    //LiveData para almacenar la lista de warframes
    private final MutableLiveData<List<Warframe>> warframeLiveData = new MutableLiveData<>();
    private final DashboardRepository warframeRepository;

    //Construcor que inicializa el respositorio y carga los warframes
    public DashboardViewModel() {
        warframeRepository = new DashboardRepository();
        loadWarframes();
    }

    //Obtiene los warframes como LiveData
    public LiveData<List<Warframe>> getWarframeLiveData() {

        return warframeLiveData;
    }

    //Metoro para cargar Warframes desde el respositorio
    private void loadWarframes() {
        warframeRepository.getWarframes(warframeLiveData);
    }
}