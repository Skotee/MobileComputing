package com.mc.oulu.fil.lab1
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_view_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Radek on 25.02.2020.
 */

class ReminderAdapter(private val context: Context, private val list:List<Reminder>):BaseAdapter(){
    val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = inflater.inflate(R.layout.list_view_item, parent, false)
        row.textMessage.text=list[position].message
        if (list[position].time != null)
        {
            val d = SimpleDateFormat("HH:mm dd.MM.yyyy")
            d.timeZone = TimeZone.getDefault()

            val time = list[position].time
            val date = d.format(time)
            row.itemTrigger.text = date
            val readableTime = d.format(it.time)
            row.textStatus.text = readableTime
        } else if (list[position].location != null) {
            row.itemTrigger.text=list[position].location
        }
        return row
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}