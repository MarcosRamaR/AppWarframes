package com.example.warframes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    //Notificar el estado del cambio de contraseña
    private final MutableLiveData<String> passwordChangeStatus = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    // Constructor para inicializar
    public ProfileViewModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    // Método para obtener el estado del cambio
    public LiveData<String> getPasswordChangeStatus() {
        return passwordChangeStatus;
    }

    public void changePassword(String currentPassword, String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Check del usuario y su estado autentificado
        if (user == null || newPassword.isEmpty()) {
            passwordChangeStatus.setValue("Error: Usuario no autenticado");
            return;
        }

        // Credenciales para la reautenticación
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        // Reautentificar al usuario con la contraseña nueva
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si la reautenticación es exitosa, se actualiza la contraseña
                user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        passwordChangeStatus.setValue("Contraseña cambiada con éxito");
                    } else {
                        passwordChangeStatus.setValue("Error al cambiar la contraseña");
                    }
                });
            } else {
                passwordChangeStatus.setValue("La contraseña actual no es correcta");
            }
        });
    }
}
