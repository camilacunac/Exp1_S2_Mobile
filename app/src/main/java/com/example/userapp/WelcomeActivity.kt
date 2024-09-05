package com.example.userapp

import WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class WelcomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userName = intent.getStringExtra("userName") ?: "Usuario"
        val user = User(userName, "", "")

        setContent { WelcomeScreen(user = user)
        }
    }
}
