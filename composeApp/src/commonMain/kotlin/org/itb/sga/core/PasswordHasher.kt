package org.itb.sga.core

expect object PasswordHasher {
    fun hashPassword(password: String): String
    fun verifyPassword(password: String, hashedPassword: String): Boolean
}
