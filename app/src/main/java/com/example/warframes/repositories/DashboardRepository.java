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

public class DashboardRepository {
    private final DatabaseReference warframeRef;

    public DashboardRepository() {
        warframeRef = FirebaseDatabase.getInstance().getReference("warframes");
    }

    //Método para obtener la lista de Warframes que tengas en Firebase
    public void getWarframes(MutableLiveData<List<Warframe>> warframeLiveData) {
        warframeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("Firebase", "Total children: " + snapshot.getChildrenCount());
                List<Warframe> warframes = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    //Convierte cada elemento de la snapshot en un objeto Warframe
                    Warframe warframe = child.getValue(Warframe.class);
                    Log.d("Firebase", "Warframe: " + (warframe != null ? warframe.getName() : "null"));
                    warframes.add(warframe);
                }
                //Actualiza el liveData con la lista actual de warframes
                warframeLiveData.setValue(warframes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

}

