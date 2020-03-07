package com.mc.oulu.fil.lab1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.toast

/**
 * Created by Radek on 25.02.2020.
 */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra("message")
        context.toast(text!!)
    }
}