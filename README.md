## Browse Video File From SDCard :
You can use this project to Browse Video File from SDCard, USB, Internal and External Storage.
This project is in Kotlin.

##### You can select video audio or any kind of files from anywhere if you are facing problems like from below path :
/document/video:27948 <br>
/document/C26B-6A27:ACTION_CREATORS_Full_HD.mp4

By using this sample code you can select any file from anywhere like USB/SDCard/Gallery/Documents etc except GoogleDrive and Dropbox etc.

### For picking multiple videos :
```
private fun pickVideo() {
        try {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            val mimetypes = arrayOf("video/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, BROWSE_VIDEO_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            // No file explorer available
        }
    }
```

### Output :

<p align="center">
  <img src="https://github.com/SWAPDROiD/BrowseVideoFileFromSDCard/blob/master/Images/First.jpg" width="200">
<img src="https://github.com/SWAPDROiD/BrowseVideoFileFromSDCard/blob/master/Images/Second.jpg" width="200">
<img src="https://github.com/SWAPDROiD/BrowseVideoFileFromSDCard/blob/master/Images/Third.jpg" width="200">
</p>
