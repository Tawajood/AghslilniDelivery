package com.dotjoo.aghslilnidelivery.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
 import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.data.param.AddAddressParams
import kotlinx.android.synthetic.main.dialog_gbs_alert.view.btn_no
import kotlinx.android.synthetic.main.dialog_gbs_alert.view.btn_ok

fun interface OnLocationCallback {
    fun onLocationReceived(location: Location)
}

@Singleton
open class WWLocationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    private var locationCallback: OnLocationCallback? = null

    private var enableLocationUpdates: Boolean = false

    private val mLocationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val locationList = locationResult.locations
                if (locationList.isNotEmpty()) {
                    //The last location in the list is the newest
                    locationCallback?.onLocationReceived(locationList.last())
                    if (!enableLocationUpdates)
                        fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }
    }

    private val mLocationRequest by lazy {
        LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(
        enableLocationUpdates: Boolean,
        onLocationCallback: OnLocationCallback
    ) {
        this.enableLocationUpdates = enableLocationUpdates
        this.locationCallback = onLocationCallback
        startLocation()
    }

    fun unregister() {
        mLocationCallback.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    /*
    * usually called in onResume if u want
    * */
    @SuppressLint("MissingPermission")
    fun startLocation() {
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.getMainLooper()
        )
    }


    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(onLocationCallback: OnLocationCallback) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationCallback.onLocationReceived(location)
            }
            requestLocationUpdates(false, onLocationCallback)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(onLocationCallback: OnLocationCallback) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationCallback.onLocationReceived(location)

                } else {
                    requestLocationUpdates(false, onLocationCallback)

                }
            }
            .addOnFailureListener {
                requestLocationUpdates(false, onLocationCallback)

            }
    }

     fun getAddress(lat: Double?, lng: Double?): AddAddressParams? {
        val geocoder = Geocoder(context, Locale("En"))
        return try {
            val addressList =
                geocoder.getFromLocation(lat ?: 0.0, lng ?: 0.0, 1)
            var address: AddAddressParams? = null
            if (addressList != null && addressList.size > 0) {
                val addressObj = addressList[0]
                address = AddAddressParams(
                    lat.toString(),
                    lng.toString(),
                    addressObj.adminArea,
                    addressObj.subAdminArea,
                    addressObj.locality,
                    null,
                    null,
                    null,
                    addressObj.getAddressLine(0)
                )
            } else {
                address = AddAddressParams(
                    lat.toString(),
                    lng.toString(),
                    context.resources.getString(R.string.undefined),
                    context.resources.getString(R.string.undefined),
                    context.resources.getString(R.string.undefined),
                    context.resources.getString(R.string.undefined),
                    context.resources.getString(R.string.undefined),
                    context.resources.getString(R.string.undefined),
                    context.resources.getString(R.string.undefined)
                )
            }
            address
        } catch (e: IOException) {
            //   e.showLogMessage()
            null
        }
    }


    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

   fun showAlertDialogButtonClicked(    context: Context ,  locationLauncher: ActivityResultLauncher<Intent>?  ) {
        val mDialogView = LayoutInflater.from(context).inflate( R.layout.dialog_gbs_alert, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        mDialogView.btn_ok.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            locationLauncher?.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }
        //cancel button click of custom layout
        mDialogView.btn_no.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }

    }


    fun openLocationGPS(
        activity: FragmentActivity,
        callBack: (isOpened: Any) -> Unit
    ) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result =
            LocationServices.getSettingsClient(activity)
                .checkLocationSettings(builder.build())
        result.addOnSuccessListener {
            callBack.invoke(true)
        }

        result.addOnFailureListener { exception ->
            //    exception.showLogMessage("exc")
            callBack.invoke(false)
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, 0x1)
                    callBack.invoke(exception)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    //  sendEx.showLogMessage()
                }
            }
        }
    }


}

       fun LifecycleOwner.openLocationSettingsResultLauncher(
           callBack: (result: ActivityResult) -> Unit
       ): ActivityResultLauncher<Intent>? {
           if (!this.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) return null
           val launcher: ActivityResultLauncher<Intent>
           val type = ActivityResultContracts.StartActivityForResult()
           val result = ActivityResultCallback<ActivityResult> {
               callBack.invoke(it)
           }
           launcher = when (this) {
               is Fragment -> this.registerForActivityResult(type, result)
               is AppCompatActivity -> {
                   this.registerForActivityResult(type, result)
               }
               else -> throw IllegalAccessException("must be called from a Fragment or AppCompatActivity")
           }
           return launcher
       }
