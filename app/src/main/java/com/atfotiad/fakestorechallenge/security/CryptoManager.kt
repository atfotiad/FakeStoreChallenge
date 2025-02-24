package com.atfotiad.fakestorechallenge.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class CryptoManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher: Cipher =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv)
            )
        }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(
            KEY_NAME, null
        ) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(BLOCK_MODE).setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false).setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(data: String): String {
        val bytes = encryptCipher.doFinal(data.toByteArray())
        return "${bytes.toHexString()}:${encryptCipher.iv.toHexString()}"
    }

    fun decrypt(data: String): String {
        val (bytes, iv) = data.split(":").let {
            it[0].hexToByteArray() to it[1].hexToByteArray()
        }
        return getDecryptCipherForIv(iv).doFinal(bytes).toString(Charsets.UTF_8)
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val KEY_NAME = "secret_key"
    }
}

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
fun String.hexToByteArray() = ByteArray(this.length / 2) {
    this.substring(it * 2, it * 2 + 2).toInt(16).toByte()
}