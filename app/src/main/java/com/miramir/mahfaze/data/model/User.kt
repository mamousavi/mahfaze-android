package com.miramir.mahfaze.data.model

data class User(
    val id: Int,
    val email: String,
    var password: String,
    val createdAt: String,
    var updatedAt: String
)