package com.mc.oulu.fil.lab1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import android.util.TimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.sql.Time
import java.util.*
import kotlinx.android.synthetic.main.activity_time.*

class TimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        datePicker.minDate=Calendar.getInstance().timeInMillis;
        button.setOnClickListener{

            val calednra = GregorianCalendar(datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth,timePicker.currentHour, timePicker.currentMinute)
            if (calednra.timeInMillis > System.currentTimeMillis()) {
                val reminder = Reminder(
                        null,
                        calednra.timeInMillis,
                        null,
                        editText.text.toString()
                )
                doAsync {
                    val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders").build()
                    db.reminderDAO().insert(reminder);
                    db.close()
                    finish()
                }
            }else{
                toast("Some error, please check")
            }
        }
    }
}
