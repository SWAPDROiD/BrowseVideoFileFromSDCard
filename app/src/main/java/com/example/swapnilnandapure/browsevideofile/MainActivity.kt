package com.example.swapnilnandapure.browsevideofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import com.example.swapnilnandapure.browsevideofile.databinding.ActivityMainBinding
import com.example.swapnilnandapure.browsevideofile.utils.FilePath
import com.example.swapnilnandapure.browsevideofile.utils.FileUtil
import com.example.swapnilnandapure.browsevideofile.utils.Utility
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File
import android.widget.VideoView

/**
 * Created by Swapnil Nandapure
 *  In this activity you can browse video file from anywhere
 */

class MainActivity : AppCompatActivity() {

    private var TAG: String = MainActivity::class.java.simpleName
    private var PERMISSION_REQUEST_CODE: Int = 1
    private val BROWSE_VIDEO_REQUEST = 2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.butBrowseVid.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                val mimetypes = arrayOf("video/*")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
                startActivityForResult(intent, BROWSE_VIDEO_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
                Utility.showToast(this@MainActivity, getString(R.string.no_file_explorer), Toast.LENGTH_SHORT)
            }
        }
    }

    /**
     * initialize.play video from storage location
     */
    private fun initVideo(videoPath: String){
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        binding.videoView.keepScreenOn = true
        binding.videoView.setVideoPath(videoPath)
        binding.videoView.start()
        binding.videoView.requestFocus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO Auto-generated method stub
        when (requestCode) {
            BROWSE_VIDEO_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                try {
                    val PathHolder = data!!.data!!.path
                    Log.e(TAG, "SWAPLOG PATH = " + PathHolder!!)

                    if (FileUtil.isFileExist(PathHolder)) {  // /storage/UsbDriveA/React_Native.mp4
                        Log.e(TAG, "SWAPLOG PATH = exist")
                        if (PathHolder.endsWith(".mp4") || PathHolder.endsWith(".3gp") || PathHolder.endsWith(".avi") || PathHolder.endsWith(
                                ".MP4"
                            ) || PathHolder.endsWith(".3GP") || PathHolder.endsWith(".AVI")
                        ) {
                            //move for video
                            initVideo(PathHolder)
                        } else {
                            Utility.showToast(
                                this@MainActivity,
                                getString(R.string.select_any_video_file),
                                Toast.LENGTH_SHORT
                            )
                        }
                    } else {  // /document/video:27948
                        Log.e(TAG, "SWAPLOG PATH = not exist")
                        FromSDCard(data.data)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Utility.showToast(
                        this@MainActivity,
                        getString(R.string.video_is_not_available),
                        Toast.LENGTH_SHORT
                    )
                }

            }
        }
    }

    /**
     * get Data from SDCard
     */
    private fun FromSDCard(uri: Uri?) {
        try {
            val selectedFilePath = FilePath.getPath(this@MainActivity, uri!!)
            Log.e(TAG, "SWAPLOG PATH selectedFilePath = " + selectedFilePath!!)
            if (selectedFilePath != null) {
                val file = File(selectedFilePath!!)
                Log.e(TAG, "SWAPLOG PATH file = $file")
                if (file.exists()) {
                    Log.e(TAG, "SWAPLOG PATH = exist")
                    val filePath = file.getPath()
                    if (filePath.endsWith(".mp4") || filePath.endsWith(".3gp") || filePath.endsWith(".avi") || filePath.endsWith(
                            ".MP4"
                        ) || filePath.endsWith(".3GP") || filePath.endsWith(".AVI")
                    ) {
                        //move for video
                        initVideo(filePath)
                    } else {
                        Utility.showToast(
                            this@MainActivity,
                            getString(R.string.select_any_video_file),
                            Toast.LENGTH_SHORT
                        )
                    }
                } else {
                    Log.e(TAG, "SWAPLOG PATH = not exist")
                    getFromDocument(uri!!)
                }
            } else {
                Log.e(TAG, "SWAPLOG PATH = is null")
                getFromDocument(uri!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            getFromDocument(uri!!)
        }

    }

    /**
     * get Data from document path
     */
    private fun getFromDocument(uri: Uri) {
        Log.e(TAG, "SWAPLOG PATH = getFromDocument")
        val orignalPath = uri.path
        try {
            if (orignalPath!!.endsWith(".mp4") || orignalPath.endsWith(".3gp") || orignalPath.endsWith(".avi") || orignalPath.endsWith(
                    ".MP4"
                ) || orignalPath.endsWith(".3GP") || orignalPath.endsWith(".AVI")
            ) { // /document/C26B-6A27:React_Native.mp4
                val getFilePath = FilePath.getDocumentPath(uri)
                Log.e(TAG, "SWAPLOG PATH = " + getFilePath!!)
                if (getFilePath != null) {
                    if (FileUtil.isFileExist(getFilePath)) {
                        //move for video
                        initVideo(getFilePath)
                    } else {
                        Utility.showToast(
                            this@MainActivity,
                            getString(R.string.video_is_not_available),
                            Toast.LENGTH_SHORT
                        )
                    }
                } else {
                    Utility.showToast(
                        this@MainActivity,
                        getString(R.string.video_is_not_available),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Utility.showToast(this@MainActivity, getString(R.string.video_is_not_available), Toast.LENGTH_SHORT)
        }

    }


    override fun onResume() {
        super.onResume()
        permissions()
    }

    /*
    * Check external storage permission
    * */
    private fun permissions(){
        try {
            // Rx permissions to read external storage
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe { granted ->
                if (granted!!) { // Always true pre-M
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) // Code for check external storage permission
                        {
                            // Code for above or equal 23 API Oriented Device
                        } else {
                            requestPermission(); // Code for request permission
                        }
                    } else {
                        // Code for Below 23 API Oriented Device
                    }
                } else {
                    Utility.showToast(this@MainActivity, getString(R.string.external_storage_permission_to_store_videos), Toast.LENGTH_LONG)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    /*
    * Check external storage permission
    * */
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return if (result == PackageManager.PERMISSION_GRANTED) true else false
    }

    /*
    * request write external storage permission
    * */
    private fun requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utility.showToast(this@MainActivity, getString(R.string.external_storage_permission_to_store_videos), Toast.LENGTH_LONG)
        } else {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    /*
    * read result of requested write storage permission
    * */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .")
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .")
            }
        }
    }


}
