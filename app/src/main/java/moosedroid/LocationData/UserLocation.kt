package moosedroid.LocationData

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.GoogleMap


/**
 * Created by HimelR on 20-Feb-18.
 */
class LocationData(val context:Context){
 private var mMap: GoogleMap? = null

    fun getLocation(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true)
        } else {
            // Show rationale and request permission.
        }
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