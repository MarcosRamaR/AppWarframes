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

import com.example.warframes.databinding.RandomFragmentBinding;
import com.example.warframes.models.Warframe;
import com.example.warframes.viewmodels.RandomViewModel;

public class RandomFragment extends Fragment {
    private RandomFragmentBinding binding;
    private RandomViewModel viewModel;
    private String currentWarframeName;

    public RandomFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout (reutilizamos el mismo layout que DetailFragment)
        binding = DataBindingUtil.inflate(inflater, R.layout.random_fragment, container, false);

        // Obtener el ViewModel
        viewModel = new ViewModelProvider(this).get(RandomViewModel.class);

        // Configurar los observadores
        setupObservers();


        // Agregar un botón para cargar otro warframe aleatorio si has añadido el botón al layout

        binding.favRandom.setOnClickListener(view -> {
            // Llamar al método del ViewModel para cargar un nuevo warframe aleatorio
            viewModel.loadRandomWarframe();
        });
        return binding.getRoot();
    }

    private void setupObservers() {
        // Observar cambios en el warframe aleatorio
        viewModel.getWarframeLiveData().observe(getViewLifecycleOwner(), warframe -> {
            if (warframe != null) {
                updateUI(warframe);
                currentWarframeName = warframe.getName();

            }
        });

        // Observar mensajes de error
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUI(Warframe warframe) {
        binding.titleTextView.setText(warframe.getName());
        binding.descriptionTextView.setText(warframe.getDescription());
        Glide.with(requireContext()).load(warframe.getUrl()).into(binding.imageView);
    }


    // Método estático para crear una nueva instancia
    public static RandomFragment newInstance() {
        return new RandomFragment();
    }
}