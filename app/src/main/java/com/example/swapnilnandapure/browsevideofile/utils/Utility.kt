@file:JvmName("Utilitys")
@file:JvmMultifileClass

package com.example.swapnilnandapure.browsevideofile.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import android.widget.TextView
import java.io.File
import android.content.res.Resources
import com.example.swapnilnandapure.browsevideofile.R


/**
 * Created by Swapnil Nandapure
 *  This class provides re usable functions
 */
object Utility {

    /**
     *  to convert seconds to time format
     */
    fun convertSecondsToTime(seconds: Long): String {
        var timeStr: String? = null
        var hour = 0
        var minute = 0
        var second = 0
        if (seconds <= 0) {
            return "00:00"
        } else {
            minute = seconds.toInt() / 60
            if (minute < 60) {
                second = seconds.toInt() % 60
                timeStr = "00:" + unitFormat(minute) + ":" +
                        unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99) return "99:59:59"
                minute = minute % 60
                second = (seconds - (hour * 3600).toLong() - (minute * 60).toLong()).toInt()
                timeStr = unitFormat(hour) + ":" +
                        unitFormat(minute) + ":" +
                        unitFormat(second)
            }
        }
        return timeStr
    }

    /**
     *  to do formatting for timing
     */
    private fun unitFormat(i: Int): String {
        var retStr: String? = null
        retStr = if (i in 0..9) {
            "0" + Integer.toString(i)
        } else {
            "" + i
        }
        return retStr
    }

    @JvmStatic
    fun showToast(mContext: Context, message: String, durationForMessageToAppear: Int) {
        if (durationForMessageToAppear == 1) {
            val toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG)
            val view = toast.view
            val text = view.findViewById<TextView>(android.R.id.message)
//            view.getBackground().setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN);
            text.setTextColor(mContext.resources.getColor(R.color.colorPrimary))
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            val toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
            val view = toast.view
            val text = view.findViewById<TextView>(android.R.id.message)
//            view.getBackground().setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN);
            text.setTextColor(mContext.resources.getColor(R.color.colorPrimary))
            toast.setGravity(Gravity.CENTER, 0, 0)

            toast.show()
        }

    }

    /**
     * This function is to refresh Select Video gallery to notify there is new trimmed video is added
     *
     * @param path    - is an trimmed video path new generated
     * @param context - context of this class
     */
    @JvmStatic
    fun refreshGallery(path: String, context: Context) {
        val file = File(path)
        try {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(file)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
            println("VrViewAct - Gallery Refreshed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Delete trimmed video file from external storage after making video with audio file
     *
     * @param filename - is an trimmed video file
     */
    @JvmStatic
    fun deleteFileFromSD(filename: String): Boolean {
        var deleted: Boolean = false;
        try {
            val file = File(filename)
            file.delete()
            println("VideoTrimAct - SWAPLOG - File Deleted = $filename")
            deleted = true
        } catch (e: Exception) {
            e.printStackTrace()
            deleted = false
        }
        return deleted
    }

    @JvmStatic
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    @JvmStatic
    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

}