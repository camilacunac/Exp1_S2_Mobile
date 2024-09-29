package com.example.userapp

import RegisterScreen
import UserSingleton
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class RegisterActivity : ComponentActivity() {

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registrar permisos de ubicación
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permiso concedido, no mostrar Toast adicional
                // La aplicación puede proceder a obtener la ubicación
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            RegisterScreen(
                userSingleton = UserSingleton,
                onRequestLocationPermission = { requestLocationPermission() }
            )
        }
    }

    // Método para solicitar permisos de ubicación
    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                // El permiso ya fue concedido, no es necesario solicitarlo nuevamente
                Toast.makeText(this, "Permiso de ubicación ya concedido", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Solicitar el permiso de ubicación solo si no está concedido
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}
