package com.dnnt.touch.util

import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by dnnt on 18-4-16.
 */

fun bytesToHex(bytes: ByteArray): String {
    val hexArray = "0123456789ABCDEF".toCharArray()
    val hexChars = CharArray(bytes.size shl 1)
    for (j in bytes.indices) {
        val v = bytes[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}

fun hexToBytes(hex: String): ByteArray{
    val bytes = ByteArray(hex.length shr 1)
    for(i in 0 until hex.length step  2){
        val d1 = Character.digit(hex[i],16) shl 4
        val d2 = Character.digit(hex[i + 1],16)
        bytes[i shr 1] = (d1 xor d2).toByte()
    }
    return bytes
}

fun rsaDecrypt(key: Key, bytes: ByteArray): ByteArray{
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.DECRYPT_MODE,key)
    return cipher.doFinal(bytes)
}

fun rsaEncrypt(key: Key, bytes: ByteArray): ByteArray{
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE,key)
    return cipher.doFinal(bytes)
}

fun aesEncrypt(key: Key, bytes: ByteArray): ByteArray{
    val cipher = Cipher.getInstance("AES")
//    val ivSpec = IvParameterSpec(key.encoded)
    cipher.init(Cipher.ENCRYPT_MODE,key)
    return cipher.doFinal(bytes)
}

fun aesDecrypt(key: Key, bytes: ByteArray): ByteArray{
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE,key)
    return cipher.doFinal(bytes)
}

fun genSecretKey(hex: String): SecretKey{
    val keyGen = KeyGenerator.getInstance("AES")
    val random = SecureRandom.getInstance("SHA1PRNG")
    random.setSeed(hex.toByteArray())
    keyGen.init(128,random)
    return keyGen.generateKey()
}
//
//fun aesDecrypt(key: Key, bytes: ByteArray): ByteArray{
//    val keyGen = KeyGenerator.getInstance("AES")
//    val cipher = Cipher.getInstance("AES")
//    val random = SecureRandom.getInstance("SHA1PRNG")
//    random.setSeed(key.encoded)
//    keyGen.init(128,random)
//    val realKey = keyGen.generateKey()
//    cipher.init(Cipher.DECRYPT_MODE,realKey)
//    return cipher.doFinal(bytes)
//}