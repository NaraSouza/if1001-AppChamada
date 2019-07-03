package com.example.appattendance.adapters

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appattendance.R
import com.example.appattendance.SubjectDetailActivity
import com.example.appattendance.models.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

    class ViewHolder (item : View, private val ctx: Context) : RecyclerView.ViewHolder(item), View.OnClickListener {

        val name = item.subject_name!!
        val code = item.subject_code!!

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val subject = v!!.tag as Subject

            val intent = Intent(ctx, SubjectDetailActivity::class.java)
            intent.putExtra("subject_name", subject.name)
            intent.putExtra("subject_code", subject.code)

            //modificacoes por issue #14 do github
            if(TextUtils.isEmpty(subject.period)) {
                val userEmail = FirebaseAuth.getInstance().currentUser!!.email
                val userId = userEmail!!.replace(".", "")
                FirebaseDatabase.getInstance().reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot : DataSnapshot) {
                        val userType = dataSnapshot.child("users").child(userId).child("type").value.toString()
                        lateinit var snapshot: DataSnapshot
                        if(userType.contentEquals("professor")) {
                            snapshot = dataSnapshot.child("professors").child(userId)
                        } else {
                            snapshot = dataSnapshot.child("students").child(userId)
                        }
                        subject.period = snapshot.child("subjects").child(subject.code).child("period")
                            .value.toString()
                        intent.putExtra("subject_period", subject.period)
                        ctx.startActivity(intent)

                    }

                    override fun onCancelled(error : DatabaseError) {
                        // Failed to read value
                        Log.w("Falha", "Failed to read value.", error.toException())
                    }
                })
            } else {
                intent.putExtra("subject_period", subject.period)
                ctx.startActivity(intent)
            }
        }
    }
}