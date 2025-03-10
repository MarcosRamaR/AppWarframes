package com.example.warframes.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.warframes.R;
import com.example.warframes.adapters.WarframeAdapter;
import com.example.warframes.databinding.FavoritesFragmentBinding;
import com.example.warframes.viewmodels.FavoriteViewModel;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private FavoriteViewModel viewModel;
    private WarframeAdapter adapter;

    public FavoritesFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FavoritesFragmentBinding binding = FavoritesFragmentBinding.inflate(inflater, container, false);

        if (binding == null) {
            throw new IllegalStateException("Error al inflar fragment_favorites");
        }

        // Configurar el Adapter con listener para clics
        adapter = new WarframeAdapter(new ArrayList<>(), warframe -> {
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
        binding.recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewFavorites.setAdapter(adapter);

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        viewModel.getFavoriteWarframes().observe(getViewLifecycleOwner(), warframes -> adapter.setWarframe(warframes));

        return binding.getRoot();
    }
}