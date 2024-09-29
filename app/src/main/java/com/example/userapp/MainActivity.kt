package com.example.userapp

import LoginScreen
import UserSingleton
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    // Variables para almacenar el texto de reconocimiento de voz
    private var emailFromVoice: MutableState<String> = mutableStateOf("")
    private var passwordFromVoice: MutableState<String> = mutableStateOf("")

    // ActivityResultLauncher para gestionar permisos
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de ActivityResultLauncher para solicitar permisos
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido
                Toast.makeText(this, "Permiso de micrófono concedido", Toast.LENGTH_SHORT).show()
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show()
            }
        }

        // Verificar y solicitar permiso de micrófono
        checkAudioPermission()

        setContent {
            // Pasar los valores obtenidos por voz a LoginScreen
            LoginScreen(
                emailState = emailFromVoice,
                passwordState = passwordFromVoice,
                onVoiceInputRequest = { requestCode ->
                    startVoiceRecognition(requestCode)
                }
            )
        }
    }

    // Método para manejar los resultados del reconocimiento de voz
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result.isNotEmpty()) {
                val recognizedText = result[0].replace("arroba", "@")
                when (requestCode) {
                    1 -> { // Request code para el correo electrónico
                        emailFromVoice.value = recognizedText
                    }
                    2 -> { // Request code para la contraseña
                        passwordFromVoice.value = recognizedText
                    }
                }
            }
        }
    }

    // Función para iniciar el reconocimiento de voz
    private fun startVoiceRecognition(requestCode: Int) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")  // Configura el idioma en español
        }
        startActivityForResult(intent, requestCode)
    }

    // Función para verificar y solicitar permisos de micrófono
    private fun checkAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                // El permiso ya está concedido, puedes utilizar el micrófono
                Toast.makeText(this, "Permiso de micrófono ya concedido", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Solicitar el permiso
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    @Preview
    @Composable
    fun VistaPrevia() {
        LoginScreen( emailState = remember { mutableStateOf("") }, passwordState = remember { mutableStateOf("") }, onVoiceInputRequest = {})
    }
}
