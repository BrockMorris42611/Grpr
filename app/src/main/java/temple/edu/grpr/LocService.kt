package temple.edu.grpr

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log

class LocService : Service() {

    var previousLocation : Location? = null
    var distanceTraveled = 0f
    lateinit var locationListener : LocationListener
    lateinit var locHandler : Handler
    var locBinder = LocBinder()

    inner class LocBinder : Binder() {
        fun BeginLocTrack() {
            startTracking()
        }

        fun setHandler(handler: Handler) {
            locHandler = handler
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return locBinder
    }
    fun startTracking(){
        /*val hi : Message = Message()
        hi.what = 1
        locHandler.sendMessage(hi);
        locationListener = LocationListener {
            if (previousLocation != null) {
                distanceTraveled += it.distanceTo(previousLocation)
                /*textView.text = distanceTraveled.toString()

                val latLng = LatLng(it.latitude, it.longitude)

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))*/
            }
            previousLocation = it
        }*/
        Thread {
            for (i in 10 downTo 1) {
                Log.d("Countdown", i.toString())
                if (::locHandler.isInitialized)
                    locHandler.sendEmptyMessage(i)
                Thread.sleep(1000)
            }
        }.start()
    }
}