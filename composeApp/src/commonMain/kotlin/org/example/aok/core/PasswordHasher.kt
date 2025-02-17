package org.example.aok.core

expect object PasswordHasher {
    fun hashPassword(password: String): String
    fun verifyPassword(password: String, hashedPassword: String): Boolean
}
