package com.mc.oulu.fil.lab1

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    class MapActivity : AppCompatActivity(), OnMapReadyCallback {
        lateinit var gMap:GoogleMap
        lateinit var  fusedLocationClient:FusedLocationProviderClient
        lateinit var selectedLocation:LatLng
        lateinit var geofencingClient: GeofencingClient
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_map)
            geofencingClient = LocationServices.getGeofencingClient(this)
            (varf as SupportMapFragment).getMapAsync{}
            button.setOnClickListener{
                if (editText.text.toString().isEmpty() || selectedLocation==null){
                    return@setOnClickListener
                }
                appendNewReminder()
            }
        }
            val reminderText = editText.text.toString()
            val reminder = Reminder(
                    uid=null,
                    time=null,
                    location = String.format("%.3f,%.3f", selectedLocation.latitude,selectedLocation.latitude),
                    message = reminderText
            )
            doAsync {
                val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders").build()
                val uid= db.reminderDAO().insert(reminder).toUInt()
                reminder.uid=uid.toInt()
                db.close()
                createGeofence(selectedLocation, reminder, geofencingClient)
            }
            finish()

        val GEOFENCE_ID = "REMINDER_GEOFENCE_ID"
        fun createGeofence(location:LatLng, reminder:Reminder, geofencingClient: GeofencingClient) {
            val geofence = Geofence.Builder().setRequestId(GEOFENCE_ID)
                    .setCircularRegion(location.latitude, location.longitude, 500.toFloat())
                    .setExpirationDuration(100000.toLong())
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
                    .setLoiteringDelay(1000)
                    .build()
            val req = GeofencingRequest.Builder().setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .addGeofence(geofence).build()
            val intent = Intent(this, GeofenceReceiver::class.java).putExtra("uid", reminder.uid)
                    .putExtra("message", reminder.message).putExtra("location", reminder.location)
            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
            geofencingClient.addGeofences(req, pendingIntent)
        }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            gMap=map
            if (
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
                    ){
                gMap.isMyLocationEnabled=true
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    it?.let{
                        var latlong = LatLng(it.latitude, it.longitude)
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 200.0f));
                    }}
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 123);
            }

            gMap.setOnMapClickListener { location:LatLng->
                with(gMap){
                    clear()
                    animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))

                    var title = ""
                    var city= ""
                    try {
                        val geocoder = Geocoder(applicationContext, Locale.getDefault())
                        val adress =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
                        city = adress.locality
                        title = adress.getAddressLine(0)
                    }
                    catch (io:Exception){

                    }
                    val marker = addMarker(MarkerOptions().position(location).snippet(city).title(title))
                    selectedLocation=location;
                    marker.showInfoWindow()
                    addCircle(
                            CircleOptions().center(location).strokeColor(Color.argb(50,70,70,70)).fillColor(Color.argb(100,150,150,150))
                    )
                }
            }
        }

    }
}