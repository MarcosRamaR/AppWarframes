package com.example.warframes.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.warframes.R;
import com.example.warframes.viewmodels.RegisterViewModel;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;


//Casi igual que LoginActivity
public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SharedPreferences preferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean("darkMode", false);

        // Aplicar el tema según la preferencia
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        setupObservers();
        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());
    }

    private void setupObservers() {
        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getRegistrationSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }


    private void registerUser() {
        //Obtiene los valores de los campos del registro
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailRegisterEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordRegisterEditText)).getText().toString();
        String repassword = ((EditText) findViewById(R.id.passwordConfirmRegisterEditText)).getText().toString();
        String telephone = ((EditText) findViewById(R.id.telephoneEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();

        viewModel.registerUser(name, email, password, repassword, telephone, address);
    }
}