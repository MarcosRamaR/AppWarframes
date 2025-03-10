package com.example.warframes.views;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.warframes.R;
import com.example.warframes.databinding.DetailFragmentBinding;
import com.example.warframes.viewmodels.DetailViewModel;

public class DetailFragment extends Fragment {
    private DetailFragmentBinding binding;
    private DetailViewModel viewModel;
    private String warframeName;

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragmento y configurar el binding
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false);

        // Obtener el ViewModel
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // Obtener los datos del Bundle
        if (getArguments() != null) {
            warframeName = getArguments().getString("name");
        }

        Log.d("DetailFragment", "warframeId obtenido de los argumentos: " + warframeName);
        if (warframeName != null) {
            viewModel.checkIsFavorite(warframeName);
        } else {
            Log.e("DetailFragment", "warframeId es null al recibir los argumentos");
        }

        // Extraer datos del Bundle
        String name = getArguments().getString("name");
        String description = getArguments().getString("description");
        String url = getArguments().getString("url");

        // Validar que los datos no estén vacíos
        if (name == null || description == null || url == null) {
            Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            return null;  // Si no se puede cargar, el fragmento no debe continuar
        }

        // Establecer los datos en los elementos de la vista
        binding.titleTextView.setText(name);
        binding.descriptionTextView.setText(description);
        Glide.with(getContext()).load(url).into(binding.imageView);


        // Configurar el botón de favorito
        setupFavoriteButton();

        return binding.getRoot();  // Retorna la vista inflada
    }

    private void setupFavoriteButton() {
        // Observar el estado de "favorito"
        viewModel.getIsFavorite().observe(getViewLifecycleOwner(), isFavorite -> {
            if (isFavorite != null) {
                if (isFavorite) {
                    binding.favFavorite.setImageResource(R.drawable.si_fav);  // Corazón favorito
                } else {
                    binding.favFavorite.setImageResource(R.drawable.no_fav);  // Corazón NO favorito
                }
            }
        });

        // Configurar el clic del botón de favorito
        binding.favFavorite.setOnClickListener(v -> {
            if (warframeName != null) {
                Log.d("DetailFragment", "Botón de favoritos pulsado, Warframe Name: " + warframeName);
                // Llamar a toggleFavorite en ViewModel
                viewModel.toggleFavorite(warframeName);
            } else {
                Log.e("DetailFragment", "warframeName es null al hacer clic en favoritos");
            }
        });
    }

    // Método para crear una nueva instancia del fragmento y pasarle los argumentos
    public static DetailFragment newInstance(String name, String description, String url) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
}


