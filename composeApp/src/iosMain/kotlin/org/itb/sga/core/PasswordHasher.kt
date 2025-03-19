package org.itb.sga.core

import platform.CoreCrypto.CC_SHA512
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault
import kotlinx.cinterop.*

actual object PasswordHasher {

    actual fun hashPassword(password: String): String {
        val salt = generateSalt()
        val saltedPassword = salt + password
        val hash = sha512(saltedPassword)
        return "$salt:$hash"
    }

    actual fun verifyPassword(password: String, hashedPassword: String): Boolean {
        val parts = hashedPassword.split(":")
        if (parts.size != 2) return false

        val salt = parts[0]
        val storedHash = parts[1]
        val computedHash = sha512(salt + password)

        return computedHash == storedHash
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun generateSalt(): String {
        val saltBytes = UByteArray(16) // 16 bytes para la sal
        SecRandomCopyBytes(kSecRandomDefault, saltBytes.size.toULong(), saltBytes.refTo(0))
        return saltBytes.joinToString("") { byteToHex(it) }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun sha512(input: String): String {
        val data = input.encodeToByteArray()
        val hash = UByteArray(64) // SHA-512 produces a 64-byte hash

        // Use pinned arrays to prevent garbage collection during hashing
        data.usePinned { pinnedData ->
            hash.usePinned { pinnedHash ->
                // Passing the address of the pinned byte arrays
                CC_SHA512(pinnedData.addressOf(0), data.size.toUInt(), pinnedHash.addressOf(0))
            }
        }

        return hash.joinToString("") { byteToHex(it) }
    }

    private fun byteToHex(byte: UByte): String {
        val hexChar = "0123456789abcdef"
        val high = (byte.toInt() and 0xF0) shr 4
        val low = byte.toInt() and 0x0F
        return "${hexChar[high]}${hexChar[low]}"
    }
}
