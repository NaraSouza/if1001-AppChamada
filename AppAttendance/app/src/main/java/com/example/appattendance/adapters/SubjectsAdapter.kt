package com.example.appattendance.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appattendance.R
import com.example.appattendance.models.Subject
import kotlinx.android.synthetic.main.item_subject.view.*

class SubjectsAdapter (private val items: List<Subject>, private val c : Context) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(c).inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view, c)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.name.text = i.name
        holder.code.text = i.code
        holder.itemView.tag = i
    }

    class ViewHolder (item : View, ctx: Context) : RecyclerView.ViewHolder(item), View.OnClickListener {

        val name = item.subject_name!!
        val code = item.subject_code!!
        val ctx = ctx

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val subject = v!!.tag as Subject

            /*val intent = Intent(ctx, SubjectDetailActivity::class.java)
            intent.putExtra("subject_name", subject.name)
            intent.putExtra("subject_code", subject.code)
            intent.putExtra("subject_period", subject.period)
            ctx.startActivity(intent)*/
        }
    }
}