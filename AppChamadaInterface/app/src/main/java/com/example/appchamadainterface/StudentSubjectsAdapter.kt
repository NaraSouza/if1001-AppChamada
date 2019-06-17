package com.example.appchamadainterface

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appchamadainterface.Model.Subject
import kotlinx.android.synthetic.main.item_subject.view.*

class StudentSubjectsAdapter (private val items: List<Subject>, private val c : Context) : RecyclerView.Adapter<StudentSubjectsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.title.text = i.title
    }

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item) {

        val title = item.title!!

    }
}