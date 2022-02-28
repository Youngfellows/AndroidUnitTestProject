package com.task.data.dto.login

/**
 * 登录响应体
 * @property id
 * @property firstName
 * @property lastName
 * @property streetName
 * @property buildingNumber
 * @property postalCode
 * @property state
 * @property country
 * @property email
 */
data class LoginResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val streetName: String,
    val buildingNumber: String,
    val postalCode: String,
    val state: String,
    val country: String,
    val email: String
)
