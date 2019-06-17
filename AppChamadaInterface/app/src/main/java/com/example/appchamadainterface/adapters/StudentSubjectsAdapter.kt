package com.example.appchamadainterface.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appchamadainterface.models.Subject
import com.example.appchamadainterface.R
import com.example.appchamadainterface.SubjectDetailActivity
import kotlinx.android.synthetic.main.item_subject.view.*

class StudentSubjectsAdapter (private val items: List<Subject>, private val c : Context) : RecyclerView.Adapter<StudentSubjectsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view, c)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.title.text = i.title
        holder.itemView.tag = i
    }

    class ViewHolder (item : View, ctx: Context) : RecyclerView.ViewHolder(item), View.OnClickListener {

        val title = item.title!!
        val ctx = ctx

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val subject = v!!.tag as Subject

            val intent = Intent(ctx, SubjectDetailActivity::class.java)
            intent.putExtra("subject_title", subject.title)
            intent.putStringArrayListExtra("subject_classes", subject.classes)
            intent.putExtra("subject_maxMissedClasses", subject.maxMissedClasses)
            ctx.startActivity(intent)
        }
    }
}