package apks.shuttl.aabhas.assignv1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.GoogleMap

class MainActivity : AppCompatActivity() {
//    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val LOCATION_REQUEST_CODE = 101

        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this,MapsActivity::class.java))
            finish()
//            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionresult(requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE))

        }

    }

    private fun requestPermissionresult(decision: Boolean) {
        if (decision==true){
            startActivity(Intent(this,MapsActivity::class.java))
        }
        else{finish()}
    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int):Boolean {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(this,
                arrayOf(permissionType), requestCode)
        return (permission==PackageManager.PERMISSION_GRANTED)
    }
}
