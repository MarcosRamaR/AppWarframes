package com.example.warframes.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.warframes.R;
import com.example.warframes.viewmodels.ProfileViewModel;


public class ProfileFragment extends Fragment {
    private ProfileViewModel viewModel;
    private EditText currentPasswordEditText, newPasswordEditText;
    // Switch para activar/desactivar el modo oscuro
    private Switch darkModeSwitch;

    // Constructor vacio para los fragments
    public ProfileFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        // Inicializamos el ViewModel del fragment
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        // Estado  del modo oscuro
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        darkModeSwitch.setChecked(isDarkMode);

        Button changePasswordButton = view.findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(v -> changePassword());

        // Listener para el switch
        darkModeSwitch.setOnCheckedChangeListener((compoundButton, checked) -> toggleDarkMode(checked));

        return view;
    }

    private void changePassword() {
        // Valores de los campos de texto en el layout
        String currentPass = currentPasswordEditText.getText().toString();
        String newPass = newPasswordEditText.getText().toString();

        // Método del ViewModel
        viewModel.changePassword(currentPass, newPass);

        // Observar el estado del cambio de contraseña
        viewModel.getPasswordChangeStatus().observe(getViewLifecycleOwner(), status ->
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show()
        );
    }

    private void toggleDarkMode(boolean enableDarkMode) {
        // Guardar en Shared Preferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("darkMode", enableDarkMode);
        boolean isSaved = editor.commit(); // Usamos commit() para asegurar el guardado

        if (isSaved) {
            Log.d("ProfileFragment", "Preferencia de modo oscuro guardada correctamente.");
        } else {
            Log.e("ProfileFragment", "Error al guardar la preferencia de modo oscuro.");
        }

        // Aplicar el modo
        if (enableDarkMode) {
            Log.d("ProfileFragment", "Activando modo oscuro");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            Log.d("ProfileFragment", "Desactivando modo oscuro");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Recrear para aplicar los cambios
        requireActivity().recreate();
    }
}
