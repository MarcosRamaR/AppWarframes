package com.example.warframes.repositories;


import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.example.warframes.models.Warframe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomRepository {
    private final DatabaseReference warframeRef;

    public RandomRepository() {
        warframeRef = FirebaseDatabase.getInstance().getReference("warframes");
    }

    public void getRandomWarframe(MutableLiveData<Warframe> warframeLiveData, MutableLiveData<String> errorLiveData) {
        warframeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("RandomRepository", "Total children: " + snapshot.getChildrenCount());
                List<Warframe> warframes = new ArrayList<>();

                // Convertir todos los warframes a una lista
                for (DataSnapshot child : snapshot.getChildren()) {
                    Warframe warframe = child.getValue(Warframe.class);
                    if (warframe != null) {
                        warframes.add(warframe);
                    }
                }

                if (!warframes.isEmpty()) {
                    // Seleccionar un warframe aleatorio
                    Random random = new Random();
                    int randomIndex = random.nextInt(warframes.size());
                    Warframe randomWarframe = warframes.get(randomIndex);

                    // Actualizar el LiveData con el warframe aleatorio
                    warframeLiveData.setValue(randomWarframe);
                } else {
                    Log.e("RandomRepository", "No se encontraron warframes");
                    errorLiveData.setValue("No se encontraron warframes disponibles");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("RandomRepository", "Error: " + error.getMessage());
                errorLiveData.setValue("Error al cargar warframes: " + error.getMessage());
            }
        });
    }
}
