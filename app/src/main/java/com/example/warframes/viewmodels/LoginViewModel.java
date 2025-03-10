package com.example.warframes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warframes.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    //Respositorio para operar con el usuario
    private final UserRepository repository;

    //LiveData para distintos estados y errores
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Void> navigateToDashboard = new MutableLiveData<>();

    //Constructor para el respositorio de usuario
    public LoginViewModel() {
        repository = new UserRepository();
    }

    //Metodos para observar los LiveData
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<Void> getNavigateToDashboard() { return navigateToDashboard; }

    //Método para el login del usuario
    public void loginUser(String email, String password) {
        //Valida si hay datos necesarios vacíos
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Todos los campos son obligatorios");
            return;
        }
        //Establece estado de carga
        isLoading.setValue(true);

        //Intenta el inicio de sesión en base al método en UserRepository enviando email y password
        repository.loginUser(email, password)
                .addOnSuccessListener(authResult -> {
                    //Si hay exito actualiza esto
                    loginSuccess.setValue(true);
                    isLoading.setValue(false);
                    navigateToDashboard.setValue(null);

                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Error en el registro: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

}