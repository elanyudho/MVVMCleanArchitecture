package com.elanyudho.core.security

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

/**
 * DataStore Serializer that encrypts on write and decrypts on read.
 * 
 * This is the "bridge" between DataStore and Tink:
 * - DataStore calls writeTo() → we encrypt the protobuf bytes before writing to disk
 * - DataStore calls readFrom() → we decrypt the bytes from disk before parsing protobuf
 */
class AuthPreferencesSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<AuthPreferences> {

    override val defaultValue: AuthPreferences = AuthPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthPreferences {
        return try {
            val decryptedStream = cryptoManager.decrypt(input)
            AuthPreferences.parseFrom(decryptedStream)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read encrypted auth preferences", e)
        }
    }

    override suspend fun writeTo(t: AuthPreferences, output: OutputStream) {
        val encryptedStream = cryptoManager.encrypt(output)
        t.writeTo(encryptedStream)
        withContext(Dispatchers.IO) {
            encryptedStream.flush()
            encryptedStream.close()
        }
    }
}
