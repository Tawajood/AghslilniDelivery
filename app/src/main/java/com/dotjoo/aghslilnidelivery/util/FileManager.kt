package com.dotjoo.aghslilnidelivery.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.Date


private const val TAG = "FileManager"
/**
 * Created by Mahmoud Ayman on 1/8/2022.
 */
object FileManager {

    private const val EOF = -1
    private const val DEFAULT_BUFFER_SIZE = 1024 * 4
    private fun splitFileName(fileName: String): Array<String> {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun rename(file: File, newName: String): File? {
        val newFile = File(file.parent, newName)
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
            }
            if (file.renameTo(newFile)) {
            }
        }
        return newFile
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: FileOutputStream): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF !== input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    @Throws(IOException::class)
    fun from(context: Context, uri: Uri?): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri!!)
        val fileName: String = getFileName(context, uri)
        val splitName: Array<String> = splitFileName(fileName)
        var tempFile = File.createTempFile(splitName[0], splitName[1])
        tempFile = rename(tempFile, fileName)
        tempFile.deleteOnExit()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(tempFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            copy(inputStream, out!!)
            inputStream.close()
        }
        out?.close()
        return tempFile
    }
    fun bitmapToFile(
        context: Context?,
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
    fun convertBitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "fileNameToSave")
        file.createNewFile()
        // Convert bitmap to byte array
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos) // It can be also saved it as JPEG
        val bitmapdata = baos.toByteArray()
   return file }
    fun getRealPathFromURI(activity: Context, contentURI: Uri): String {
        val result: String
        val cursor: Cursor? = activity.contentResolver
            .query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex("_data")
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
    fun getRealPathFromURI(contentURI: Uri, context: Context): String? {
        val result: String?
        val cursor: Cursor? = context.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun getBitmap(filePath: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val f = File(filePath)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: Exception) {
        //    e.showLogMessage()
        }
        return bitmap
    }

    fun pickMultipleImage(
        activity: AppCompatActivity,
        callBack: (List<Uri>) -> Unit
    ): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(GetMultipleContents()) { uris ->
            callBack.invoke(uris)
        }
    }

    fun pickOneImage(
        fragment: Fragment,
        launcher: ActivityResultLauncher<String>
    ) {
        launcher.launch("image/*")
    }
/*
    fun pickSingleImage(
        context: LifecycleOwner,
        launcher: ActivityResultLauncher<Intent>
    ) {
        val picker: ImagePicker.Builder = if (context is Activity) {
            context
            ImagePicker.with(context)
        } else {
            ImagePicker.with(context as Fragment)
        }
        picker.crop()      //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .galleryMimeTypes(  //Exclude gif images
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            ).createIntent { intent ->
                launcher.launch(intent)
            }
    }*/

    fun File.toMultiPart(serverKey: String): MultipartBody.Part {
        val reqFile = asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            serverKey,
            name, // filename, this is optional
            reqFile
        )
       /* val imagePart = MultipartBody.Part.createFormData(
            "image",
            menuItemBody.image!!.name,
            menuItemBody.image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )*/
    }  fun toMultiPart(files: ArrayList<File>?,serverKey: String): ArrayList<MultipartBody.Part> {
     var list :ArrayList<MultipartBody.Part> = arrayListOf()
        files?.forEach {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            list.add( MultipartBody.Part.createFormData(
                serverKey,
                it.name, // filename, this is optional
                reqFile
            )
            )
        }
        return list

        /* val imagePart = MultipartBody.Part.createFormData(
            "image",
            menuItemBody.image!!.name,
            menuItemBody.image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )*/
    }



    fun Context.getBitmapFromRes(@DrawableRes drawableRes: Int): Bitmap =
        BitmapFactory.decodeResource(
            resources,
            drawableRes
        )



    /**
     * @param dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir() depending on where you want to save the image.
     * @param fileName The file name.
     * @param bm The Bitmap you want to save.
     * @param format Bitmap.CompressFormat can be PNG,JPEG or WEBP.
     * @param quality quality goes from 1 to 100. (Percentage).
     * @return true if the Bitmap was saved successfully, false otherwise.
     */
    fun vreateFile(context: Context, bitmap: Bitmap): File {

        var f:File =  File(context.cacheDir ,"")
        f.createNewFile()
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
      return  f
    }
    fun convertBitmapToFile(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat? =  Bitmap.CompressFormat.PNG, quality: Int = 95
    ): File? {
    /*    val dir = File(
            Environment.getExternalStorageDirectory().toString()
                    + File.separator + "drawable"
        )
        val imageFile = File(dir, "whysalon_img.png")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(imageFile)
            bitmap.compress(format, quality, fos)
            fos.close()
            return imageFile
        } catch (e: IOException) {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            return null
        }*/
        var timeStamp =   SimpleDateFormat("yyyyMMdd_HHmmss").format(  Date());
         var  imageFileName = "JPEG_" + timeStamp + "_";
        var storageDir  :File=   File(Environment.getExternalStorageDirectory().toString(), "WhySalon/images/");
        storageDir.mkdirs(); // make sure you call mkdirs() and not mkdir()
        var  image = File.createTempFile(
                imageFileName,  // prefix
        ".jpg",         // suffix
        storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents

      var   mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.e("our file", image.toString());
        return image;

    }

    fun getUriFromFile(activity: Activity, file: File): Uri {
        return FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider", //(use your app signature + ".provider" )
            file
        )
    }

    fun grantIntentProviderPermissions(activity: Activity, intent: Intent, uri: Uri): Intent {
        val token: Long = Binder.clearCallingIdentity()
        try {
            val resInfoList: List<ResolveInfo> = activity.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                activity.grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        } finally {
            Binder.restoreCallingIdentity(token)
        }
        return intent
    }


}

open class GetMultipleContents :
    ActivityResultContract<String, List<@JvmSuppressWildcards Uri>>() {
    @CallSuper
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .setType(input)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    }

    final override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<List<Uri>>? = null

    final override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        return intent.takeIf {
            resultCode == Activity.RESULT_OK
        }?.getClipDataUris() ?: emptyList()
    }

    @RequiresApi(18)
    internal companion object {
        internal fun Intent.getClipDataUris(): List<Uri> {
            // Use a LinkedHashSet to maintain any ordering that may be
            // present in the ClipData
            val resultSet = LinkedHashSet<Uri>()
            data?.let { data ->
                resultSet.add(data)
            }
            val clipData = clipData
            if (clipData == null && resultSet.isEmpty()) {
                return emptyList()
            } else if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    if (uri != null) {
                        resultSet.add(uri)
                    }
                }
            }
            return ArrayList(resultSet)
        }
    }
}