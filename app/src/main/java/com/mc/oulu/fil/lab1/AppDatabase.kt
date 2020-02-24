package com.mc.oulu.fil.lab1

/**
 * Created by Radek on 25.02.2020.
 */
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Reminder::class], version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun reminderDAO():ReminderDAO
}