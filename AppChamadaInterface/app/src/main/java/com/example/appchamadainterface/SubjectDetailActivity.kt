package com.example.appchamadainterface

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.appchamadainterface.adapters.SubjectClassesAdapter
import com.example.appchamadainterface.models.Class
import kotlinx.android.synthetic.main.activity_subject_detail.*

class SubjectDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        val extras = intent.extras

        tv_subject_title.text = extras.getString("subject_title")
        val classes = extras.getStringArrayList("subject_classes")
        val num = extras.getInt("subject_maxMissedClasses").toString(10)
        val s = tv_max_missed_classes.text
        tv_max_missed_classes.text = "$s $num"

        rv_subject_classes.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = SubjectClassesAdapter(classes, applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        }
    }
}
