package com.example.warframes.views;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warframes.R;
import com.example.warframes.adapters.WarframeAdapter;
import com.example.warframes.viewmodels.DashboardViewModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private DashboardViewModel warframeViewModel;
    private WarframeAdapter warframeAdapter;

    public DashboardFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        Log.d("DashboardFragment", "Inflando fragment");

        // Configurar el adapter
        warframeAdapter = new WarframeAdapter(new ArrayList<>(), warframe -> {
            Log.d("DashboardFragment", "warframeId: " + warframe.getId());

            // Crear una nueva instancia de DetailFragment con los datos
            DetailFragment detailFragment = DetailFragment.newInstance(
                    warframe.getName(),
                    warframe.getDescription(),
                    warframe.getUrl()
            );

            // Realizar la transacción de fragmento
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)  // Asegúrate de que "container" es el contenedor donde se añadirá el fragmento
                    .addToBackStack(null)  // Opcional: Agrega el fragmento a la pila de retroceso
                    .commit();
        });

        // Configurar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(warframeAdapter);

        // Inicializar ViewModel
        warframeViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        warframeViewModel.getWarframeLiveData().observe(getViewLifecycleOwner(), warframes -> {
            Log.d("DashboardFragment", "LiveData se activó, tamaño: " + warframes.size());
            if (warframes.isEmpty()) {
                Log.d("DashboardFragment", "La lista está vacía, posible problema con el ViewModel.");
            }
            warframeAdapter.setWarframe(warframes);
        });

        return view;
    }
}