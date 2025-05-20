package ru.niffer_android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import android.util.Base64
import android.webkit.MimeTypeMap
import coil.load

fun ImageView.loadBase64Image(base64String: String): Bitmap? {
    try {
        val res = base64String.split(",")
        val decodedBytes = Base64.decode(res[1], Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        this.load(bitmap)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun uriToBase64(context: Context, imageUri: Uri): String? {
    return try {
        val mimeType = getMimeTypeFromUri(imageUri) ?: return null
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
            "data:$mimeType;base64,$base64"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getMimeTypeFromUri(uri: Uri): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
    return if (!extension.isNullOrEmpty()) {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
    } else null
}
