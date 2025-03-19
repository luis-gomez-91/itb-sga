package org.itb.sga.core

import org.mindrot.jbcrypt.BCrypt

actual object PasswordHasher {
    actual fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt()) // Genera el hash con un salt
    }

    actual fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}
