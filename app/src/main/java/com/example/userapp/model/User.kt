package com.example.userapp

import java.io.Serializable

data class User(
    val fullName: String,
    val email: String,
    val password: String
): Serializable
