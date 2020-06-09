package com.cz.gyminterval.ext

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cz.gyminterval.AppComponent

import java.io.File


/**
 * Created by cz on 3/28/18.
 */

fun <T> Fragment.callbacks(): T? {
    @Suppress("UNCHECKED_CAST")
    return (parentFragment as? T) ?: (activity as? T)
}

val Context.appComponent: AppComponent
    get() = applicationContext as AppComponent

val Fragment.appComponent: AppComponent
    get() = activity!!.appComponent

fun DialogFragment.showOnce(fragmentManager: FragmentManager, tag: String) {
    if (fragmentManager.findFragmentByTag(tag) == null) {
        show(fragmentManager, tag)
        fragmentManager.executePendingTransactions()
    }
}

fun Context.getUriFromFile(file: File): Uri {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        FileProvider.getUriForFile(this, this.packageName + ".fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
}

@SuppressLint("NewApi")
fun Context.fromUriToFilePath(uri: Uri?): String? {
    if (uri == null)
        return null

    // 判斷是否為Android 4.4之後的版本
    val after44 = Build.VERSION.SDK_INT >= 19
    if (after44 && DocumentsContract.isDocumentUri(this, uri)) {
        // 如果是Android 4.4之後的版本，而且屬於文件URI
        val authority = uri.authority
        // 判斷Authority是否為本地端檔案所使用的
        when (authority) {
            "com.android.externalstorage.documents" -> {
                // 外部儲存空間
                val docId = DocumentsContract.getDocumentId(uri)
                val divide = docId.split(":")
                val type = divide[0]
                return if ("primary" == type) {
                    Environment.getExternalStorageDirectory().absolutePath + "/" + divide[1]
                } else {
                    "/storage/" + type + "/" + divide[1]
                }
            }
            "com.android.providers.downloads.documents" -> {
                // 下載目錄
                val docId = DocumentsContract.getDocumentId(uri);
                if (docId.startsWith("raw:")) {
                    return docId.replaceFirst("raw:", "");
                }
                val downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong())
                return queryAbsolutePath(this, downloadUri)

            }
            "com.android.providers.media.documents" -> {
                // 圖片、影音檔案
                val docId = DocumentsContract.getDocumentId(uri)
                val divide = docId.split(":")
                val type = divide[0]
                var mediaUri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> return null
                }
                mediaUri = ContentUris.withAppendedId(mediaUri, divide[1].toLong())
                return queryAbsolutePath(this, mediaUri)

            }
            else -> {
            }
        }
    } else {
        // 如果是一般的URI
        val scheme = uri.scheme

        if ("content" == scheme) {
            // 內容URI
            return queryAbsolutePath(this, uri)
        } else if ("file" == scheme) {
            // 檔案URI
            return uri.path
        }
    }
    return null
}

private fun queryAbsolutePath(context: Context, uri: Uri?): String? {
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    var cursor: Cursor? = null
    try {
        cursor = uri?.let { context.contentResolver.query(it, projection, null, null, null) };
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            return cursor.getString(index);
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        cursor?.close()
    }
    return null
}