package com.mc.oulu.fil.lab1

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fabOpened = false

        ButtonMain.setOnClickListener{
            if(!fabOpened) {
                fabOpened = true
                ButtonMap.animate().translationY(-resources.getDimension(R.dimen.standard_66))
                ButtonTime.animate().translationY(-resources.getDimension(R.dimen.standard_116))
            }
            else{
                ButtonMap.animate().translationY(0.0f)
                ButtonTime.animate().translationY(0.0f)
                fabOpened = false
            }
        }

        ButtonMap.setOnClickListener{
            startActivity(Intent(applicationContext, MapActivity::class.java))
        }
        ButtonTime.setOnClickListener{
            startActivity(Intent(applicationContext, TimeActivity::class.java))
        }
        updateList()
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    fun updateList() {
        doAsync {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders").build()
            val all = db.reminderDAO().getAll()
            db.close();
            uiThread {
                if (all.isEmpty()) {
                    toast("It's empty")
                } else {
                    val adapter2 = ReminderAdapter(applicationContext, all)
                    list.adapter = adapter2;
                }
            }
        }
    }
    fun setAlarm(time:Long, text:String){
        val intent = Intent(this,ReminderReceiver::class.java )
        intent.putExtra("text", text)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExact(AlarmManager.RTC, time, pendingIntent)
    }
}

