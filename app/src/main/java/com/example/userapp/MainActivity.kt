// MainActivity.kt

package com.example.userapp

import LoginScreen
import UserSingleton
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(UserSingleton)
        }
    }

    @Preview
    @Composable
    fun VistaPrevia (
    ) {
        LoginScreen(
           UserSingleton
        )
    }
}

