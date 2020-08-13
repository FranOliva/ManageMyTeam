package es.us.managemyteam.repository.util

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class PasswordUtil {

    companion object {

        fun hashPassword(password: String): String {
            val digest: MessageDigest
            try {
                digest = MessageDigest.getInstance("MD5")
                digest.update(password.toByteArray(Charset.forName("US-ASCII")), 0, password.length)
                val magnitude: ByteArray = digest.digest()
                val bi = BigInteger(1, magnitude)
                return java.lang.String.format("%0" + (magnitude.size shl 1) + "x", bi)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}