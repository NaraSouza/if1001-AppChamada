package com.example.appattendance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appattendance.R
import com.example.appattendance.models.Class
import kotlinx.android.synthetic.main.item_class.view.*

class ClassesAdapter (private val items: List<Class>, private val c : Context) : RecyclerView.Adapter<ClassesAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.item_class, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.date.text = i.date.toString()
        holder.title.text = i.title
        holder.attendance.text = i.attendance.toString()
        holder.itemView.tag = i
    }

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item) {
        val date = item.class_date!!
        val title = item.class_title!!
        val attendance = item.class_attendance_percent!!
    }
}