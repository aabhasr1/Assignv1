package apks.shuttl.aabhas.assignv1

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    lateinit var fuselocationprovider: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    lateinit var c_location: Location
    lateinit var c_position: LatLng
    lateinit var place_autocomplete: PlaceAutocompleteFragment
    lateinit var locationmanager: LocationManager
    var mzoom: Float = 0F

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //locationprovider start
        fuselocationprovider = LocationServices.getFusedLocationProviderClient(this)
        fuselocationprovider.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        c_location = location
                        Log.i("Location", "Location is : " + location.latitude + " , " + location.longitude)
                        // Logic to handle location object
                    } else {
                        Log.i("Location", "Current location unavailable")
                    }
                }
        //locationprovider end

        //locationManager start
        locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1000F, this)
        //locationManager end


        //AutoCompleteFragment code start
        place_autocomplete = fragmentManager.findFragmentById(R.id.place_autocomplete) as PlaceAutocompleteFragment
        place_autocomplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                setplace(place)
                Log.d("Maps", "Place selected: " + place.name)
            }

            override fun onError(status: Status) {
                Log.d("Maps", "An error occurred: " + status)
            }
        })
        //AutoCompleteFragment code end


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setplace(place: Place) {
        var setlatlng : LatLng
        var cameraupdate: CameraUpdate
        setlatlng=place.latLng
        cameraupdate = CameraUpdateFactory.newLatLngZoom(setlatlng, 18F)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(setlatlng).title("Selected Location"))
        mMap.animateCamera(cameraupdate)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setPadding(100, 200, 30, 40)
        mMap.setOnCameraIdleListener {
            c_position = mMap.cameraPosition.target
            mzoom = mMap.cameraPosition.zoom
            var clat:LatLng= LatLng(c_position.latitude,c_position.longitude)

            Log.i("LocationDragged :",clat.latitude.toString()+" , "+clat.longitude.toString())
            setplaceholder(getaddress(clat))

        }
    }

    private fun setplaceholder(getaddress: List<Address>) {
        if(getaddress.size!=0) {
            place_autocomplete.setText(getaddress[0].getAddressLine(0))
        }
    }


    override fun onLocationChanged(location: Location?) {
        var latlng: LatLng
        if (location != null) {
            latlng = LatLng(location.latitude, location.longitude)
            var cameraupdate: CameraUpdate
            cameraupdate = CameraUpdateFactory.newLatLngZoom(latlng, 18F)
            mMap.addMarker(MarkerOptions().position(latlng).title("Current Location"))
            mMap.animateCamera(cameraupdate)
        }

    }

    private fun getaddress(latlng: LatLng): List<Address> {
        var geocoder : Geocoder
        var address:List<Address>
        geocoder= Geocoder(this, Locale.getDefault())
        address=geocoder.getFromLocation(latlng.latitude,latlng.longitude,1)
        return address

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

}
