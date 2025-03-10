package com.example.warframes.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.warframes.R;
import com.example.warframes.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private Context context = this;
    private FirebaseAuth mAuth;
    //ViewModel para la lógica de inicio de sesión
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean("darkMode", false);
        // Aplicar el tema según la preferencia
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Inicializa el viewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //Establece los obversadores para eventos
        setupObservers();

        Button registerButton = findViewById(R.id.registerButton);

        // Establecemos el listener para el botón de login.
        findViewById(R.id.loginButton).setOnClickListener(v -> loginUser());

        // Establecemos el listener para el botón de registro.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, RegisterActivity.class);
                context.startActivity(myIntent);
            }
        });
    }
    //Método para configurar los observadores de envtos
    private void setupObservers() {
        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    private void loginUser() {
        //Obtiene referencias de los datos necesarios para el login
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        //Comprueba que existen esos elementos en la UI
        if (emailEditText == null || passwordEditText == null) {
            Toast.makeText(this, "Error: Elementos de la UI no encontrados", Toast.LENGTH_SHORT).show();
            return;
        }

        //Obtiene los valores desde los elementos previos
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //Llama al método del viewModel para iniciar sesión
        viewModel.loginUser(email, password);
    }

}