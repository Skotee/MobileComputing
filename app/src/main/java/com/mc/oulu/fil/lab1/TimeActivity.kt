package com.mc.oulu.fil.lab1

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_time.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*

class TimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        datePicker.minDate=Calendar.getInstance().timeInMillis;
        button.setOnClickListener{

            val calendar = GregorianCalendar(datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth,timePicker.currentHour, timePicker.currentMinute)
            if (calendar.timeInMillis > System.currentTimeMillis()) {
                val reminder = Reminder(
                        null,
                        calendar.timeInMillis,
                        null,
                        editText.text.toString()
                )
                doAsync {
                    val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders").build()
                    db.reminderDAO().insert(reminder);
                    db.close()
                    setAlarm(reminder.time!!, reminder.message)
                    finish()
                }
            }else{
                toast("Some error, please check")
            }
        }
    }
    fun setAlarm(time:Long, text:String){
        val intent = Intent(this,ReminderReceiver::class.java )
        intent.putExtra("text", text)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExact(AlarmManager.RTC, time, pendingIntent)
        toast("Reminder is created")
    }
}
