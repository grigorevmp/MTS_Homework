package com.mikhailgrigorev.mts_home.userData

data class User(
    val id: Long = 0,
    val login: String,
    val email: String,
    val passwordHash: String,
    val name: String,
    val phone: String,
)
