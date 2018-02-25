package moosedroid.LocationData

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Flowable


/**
 * Created by HimelR on 20-Feb-18.
 */
class LocationData(val context: Activity) {

    private var mMap: GoogleMap? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null




    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun getLocationD() : Flowable<Location>? {
        if (ContextCompat.checkSelfPermission(context.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient!!.lastLocation
                    .addOnSuccessListener(context, { location ->
                        Log.d("test2",location.longitude.toString())

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    })
        } else {
        }
        return null
    }


    @Override
    fun onMapReady(map: GoogleMap) {
        mMap = map
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
//        mMap.isMyLocationEnabled = true
//        mMap.setOnMyLocationButtonClickListener(this)
//        mMap.setOnMyLocationClickListener(this)
    }

}