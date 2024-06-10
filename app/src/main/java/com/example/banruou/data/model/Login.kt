package com.example.banruou.data.model

class LoginRequest(s: String, s1: String) {
    val USERNAME: String = s
    val PASSWORD: String = s1
}
data class AccessTokenResponse(
    val accessToken: String,
)