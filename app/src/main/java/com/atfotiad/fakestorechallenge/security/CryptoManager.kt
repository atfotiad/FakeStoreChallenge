package com.atfotiad.fakestorechallenge.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

/** Class that encrypts and decrypts login token using AES
 * [CryptoManager]
 * @property keyStore is an instance of [KeyStore]
 * @property encryptCipher is an instance of [Cipher]
 * */
class CryptoManager @Inject constructor() {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher: Cipher =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    /**
     *  [getDecryptCipherForIv] is a function that returns a [Cipher] object for decrypting data.
     *  @param iv is the initialization vector.
     * */
    private fun getDecryptCipherForIv(iv: ByteArray): Cipher =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv)
            )
        }

    /**
     *  [getKey] is a function that returns a [SecretKey] object.
     * */
    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(
            KEY_NAME, null
        ) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    /**
     *  [createKey] is a function that creates a [SecretKey] object.
     * */
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

    /** [encrypt] is a function that encrypts data.
     * @param data is the data to be encrypted.
     * @return a [String] object that contains the encrypted data.
     * */
    fun encrypt(data: String): String {
        Log.d("CryptoManager", "encrypt: String before encryption: $data")
        val bytes = encryptCipher.doFinal(data.toByteArray())
        val encryptedString = "${bytes.toHexString()}:${encryptCipher.iv.toHexString()}"
        Log.d("CryptoManager", "encrypt: String after encryption: $encryptedString")
        return encryptedString
    }

    /**
     * [decrypt] is a function that decrypts data.
     * @param data is the data to be decrypted.
     * @return a [String] object that contains the decrypted data.
     * */
    fun decrypt(data: String): String {
        Log.d("CryptoManager", "decrypt: String before decryption: $data")
        val (bytes, iv) = data.split(":").let {
            it[0].hexToByteArray() to it[1].hexToByteArray()
        }
        val decryptedString = getDecryptCipherForIv(iv).doFinal(bytes).toString(Charsets.UTF_8)
        Log.d("CryptoManager", "decrypt: String after decryption: $decryptedString")
        return decryptedString
    }

    /** Companion object that contains constants for encryption.
     * */
    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val KEY_NAME = "secret_key"
    }
}

/** Extension function that converts a [ByteArray] to a [String] object.
 * */
fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

/** Extension function that converts a [String] object to a [ByteArray].
 * */
fun String.hexToByteArray() = ByteArray(this.length / 2) {
    this.substring(it * 2, it * 2 + 2).toInt(16).toByte()
}