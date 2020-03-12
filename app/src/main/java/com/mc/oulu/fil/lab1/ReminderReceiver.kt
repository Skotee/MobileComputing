package com.mc.oulu.fil.lab1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import org.jetbrains.anko.doAsync

/**
 * Created by Radek on 25.02.2020.
 */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra("message")
        val uid = intent.getIntExtra("uid", 0 )
        MainActivity.showNotification(context!!, text!!)
        doAsync {
            val db = Room.databaseBuilder(context!!, AppDatabase::class.java, "reminders").build()
            db.reminderDAO().delete(uid)
            db.close()
        }
    }
}