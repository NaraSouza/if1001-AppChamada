package com.example.appchamadainterface.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appchamadainterface.models.Class
import com.example.appchamadainterface.R
import kotlinx.android.synthetic.main.item_class.view.*

class SubjectClassesAdapter (private val items: ArrayList<String>, private val c : Context) : RecyclerView.Adapter<SubjectClassesAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.item_class, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.title.text = i
    }

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item){

        val title: TextView = item.title
    }
}