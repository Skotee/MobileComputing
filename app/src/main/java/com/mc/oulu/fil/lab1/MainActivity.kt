package com.mc.oulu.fil.lab1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
}

