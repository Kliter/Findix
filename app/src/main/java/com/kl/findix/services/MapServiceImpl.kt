package com.kl.findix.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.kl.findix.util.Resource
import javax.inject.Inject

class MapServiceImpl @Inject constructor(
    val context: Context
) : MapService {

}