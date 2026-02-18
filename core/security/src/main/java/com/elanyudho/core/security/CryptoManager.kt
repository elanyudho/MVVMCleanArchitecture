package com.elanyudho.core.security

import android.content.Context
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.streamingaead.StreamingAeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.KeyTemplates
import java.io.InputStream
import java.io.OutputStream

/**
 * Manages encryption/decryption using Google Tink.
 * 
 * Uses StreamingAead with AES256-GCM-HKDF-4KB:
 * - AES256: 256-bit key strength
 * - GCM: Galois/Counter Mode (authenticated encryption)
 * - HKDF: Key derivation function
 * - 4KB: Chunk size (good for small protobuf data)
 * 
 * Keys are stored in Android Keystore — never leave the secure hardware.
 */
class CryptoManager(context: Context) {

    init {
        StreamingAeadConfig.register()
    }

    private val aead: StreamingAead = AndroidKeysetManager.Builder()
        .withSharedPref(context, KEYSET_NAME, KEYSET_PREF_NAME)
        .withKeyTemplate(KeyTemplates.get("AES256_GCM_HKDF_4KB"))
        .withMasterKeyUri(MASTER_KEY_URI)
        .build()
        .keysetHandle
        .getPrimitive(RegistryConfiguration.get(), StreamingAead::class.java)

    /**
     * Wraps an OutputStream with encryption.
     * Everything written to the returned stream is encrypted before hitting disk.
     */
    fun encrypt(output: OutputStream): OutputStream {
        return aead.newEncryptingStream(output, ASSOCIATED_DATA)
    }

    /**
     * Wraps an InputStream with decryption.
     * Everything read from the returned stream is decrypted transparently.
     */
    fun decrypt(input: InputStream): InputStream {
        return aead.newDecryptingStream(input, ASSOCIATED_DATA)
    }

    companion object {
        private const val KEYSET_NAME = "app_keyset"
        private const val KEYSET_PREF_NAME = "app_keyset_pref"
        private const val MASTER_KEY_URI = "android-keystore://master_key"
        private val ASSOCIATED_DATA = ByteArray(0)
    }
}
