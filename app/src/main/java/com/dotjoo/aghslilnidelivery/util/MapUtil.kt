package com.dotjoo.aghslilnidelivery.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
 import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*


object MapUtil {
    fun addMarker(context:Context,googleMap: GoogleMap, pos: LatLng, title: String? = null,      drawable:Int): Marker? {

        val marker = googleMap.addMarker(MarkerOptions()
            .position(pos)
            .icon(bitmapDescriptorFromVector(context   ,drawable)
        ).flat(true))

        return marker
    }

      fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun moveCameraAt(googleMap: GoogleMap, pos: LatLng, animate: Boolean = false) {
        val factory = CameraUpdateFactory.newLatLngZoom(pos, 16f)
        with(googleMap) {
            if (!animate)
                moveCamera(factory)
            else
                animateCamera(factory)
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    fun disableMarkerClick(googleMap: GoogleMap, disable: Boolean) {
        if (disable)
            googleMap.setOnMarkerClickListener { true }
    }

}