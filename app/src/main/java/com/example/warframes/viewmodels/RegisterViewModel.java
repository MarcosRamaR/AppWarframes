package com.example.warframes.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.warframes.models.User;
import com.example.warframes.repositories.UserRepository;


//Casi lo mismo que LoginViewModel pero para el registro

public class RegisterViewModel extends ViewModel {
    private final UserRepository repository;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public RegisterViewModel() {
        repository = new UserRepository();
    }

    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getRegistrationSuccess() { return registrationSuccess; }

    public void registerUser(String name, String email, String password, String repassword,
                             String telephone, String address) {

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                repassword.isEmpty() || telephone.isEmpty() || address.isEmpty()) {
            errorMessage.setValue("Todos los campos son obligatorios");
            return;
        }

        if (!password.equals(repassword)) {
            errorMessage.setValue("Las contraseñas no coinciden");
            return;
        }

        isLoading.setValue(true);

        repository.registerUser(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    User newUser = new User(uid, name, email, telephone, address);

                    repository.saveUserData(newUser)
                            .addOnSuccessListener(aVoid -> {
                                registrationSuccess.setValue(true);
                                isLoading.setValue(false);
                            })
                            .addOnFailureListener(e -> {
                                errorMessage.setValue("Error al guardar datos: " + e.getMessage());
                                isLoading.setValue(false);
                            });
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Error en el registro: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }
}
