package com.dotjoo.aghslilnidelivery.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(@ApplicationContext val context: Context) {

    fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun hasAllLocationPermissions(): Boolean {
        return isGranted(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) && isGranted(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun hasAllFilePickerPermissions(): Boolean {
        return isGranted(Manifest.permission.CAMERA)
                && isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                && if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }                else{
true
        //    isGranted(Manifest.permission.READ_MEDIA_IMAGES)

        }
    }

    fun getAllLocationPermissions(): Array<String> = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun getAllImagePermissions(): Array<String> = arrayListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ).apply {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        else{

         true

        }
    }.toTypedArray()



fun hasCameraPermission(): Boolean {
    return isGranted(Manifest.permission.CAMERA)
            && if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
        isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) else true
}
fun getCameraPermissions(): Array<String> = arrayListOf(
    Manifest.permission.CAMERA).apply {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}.toTypedArray()

}
/**
 * callBack has two params:
 * Boolean: return 'true' if all permissions are granted.
 * MutableMap<String, Boolean>: return each permission (name,isGranted).
 * Samples:
 *  use in fragment: requestPermissions(this, permissions){b,ps->}
 *  use in activity: requestPermissions(this, permissions){b,ps->}
 * */

fun LifecycleOwner.requestAppPermissions(
    callBack: (allIsGranted: Boolean, permissions: Map<String, Boolean>) -> Unit,
): ActivityResultLauncher<Array<String>>? {
    if (!this.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) return null
    val launcher: ActivityResultLauncher<Array<String>>
    val type = ActivityResultContracts.RequestMultiplePermissions()
    val result: ActivityResultCallback<Map<String, Boolean>> =
        ActivityResultCallback<Map<String, Boolean>> {
            val granted = it.entries.all { item ->
                item.value
            }
            callBack.invoke(granted, it)
        }
    launcher = when (this) {
        is Fragment -> {
            this.registerForActivityResult(type, result)
        }
        is AppCompatActivity -> {
            this.registerForActivityResult(type, result)
        }
        else -> throw IllegalAccessException("must be called from a Fragment or AppCompatActivity")
    }
    return launcher
}
