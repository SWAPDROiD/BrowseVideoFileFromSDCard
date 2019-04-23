package com.example.swapnilnandapure.browsevideofile.utils

import android.text.TextUtils

import java.io.File

/**
 * Created by Swapnil Nandapure
 *  This class provides re usable functions
 */

object FileUtil {

    /**
     *
     * @param filePath
     * @return
     */
    fun isFileExist(filePath: String): Boolean {
        if (TextUtils.isEmpty(filePath)) {
            return false
        }

        val file = File(filePath)
        return file.exists() && file.isFile
    }

    /**
     *
     * @param path
     * @return
     */
    fun deleteFile(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }

        val file = File(path)
        if (!file.exists()) {
            return false
        }
        if (file.isFile) {
            return file.delete()
        }
        if (!file.isDirectory) {
            return false
        }
        for (f in file.listFiles()) {
            if (f.isFile) {
                f.delete()
            } else if (f.isDirectory) {
                deleteFile(f.absolutePath)
            }
        }
        return file.delete()
    }

    fun getFileSize(path: String): Long {
        if (TextUtils.isEmpty(path)) {
            return -1
        }

        val file = File(path)
        return if (file.exists() && file.isFile) file.length() else -1
    }
}
