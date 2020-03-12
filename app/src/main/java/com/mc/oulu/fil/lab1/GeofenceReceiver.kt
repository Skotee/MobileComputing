package com.mc.oulu.fil.lab1
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

/**
 * Created by Radek on 12.03.2020.
 */


class GeofenceReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofeningEvent = GeofencingEvent.fromIntent(intent)
        val geofencingTransition = geofeningEvent.geofenceTransition
        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER||geofencingTransition==Geofence.GEOFENCE_TRANSITION_DWELL) {
            var uid = intent!!.getIntExtra("uid", 0)
            var text = intent!!.getStringExtra("message")
            MainActivity.showNotification(context!!, text)

        }
    }
}