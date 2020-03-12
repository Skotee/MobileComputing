package com.mc.oulu.fil.lab1
import androidx.room.*
@Entity(tableName = "reminders")
data class Reminder(
        @PrimaryKey(autoGenerate = true) var uid:Int?,
        @ColumnInfo(name="time") var time:Long?,
        @ColumnInfo(name="location") var location:String?,
        @ColumnInfo(name="message") var message:String
){

}
@Dao
interface ReminderDAO{
    @Transaction @Insert
    fun insert(r:Reminder): Long

    @Query("DELETE FROM reminders WHERE uid = id")
    fun delete(id: Int)

    @Query("SELECT * FROM reminders")
    fun getAll():List<Reminder>
}