package com.example.userapp

import java.io.Serializable

data class User(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val city: String = ""
) : Serializable {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "", "")
}
