package com.example.appchamadainterface

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.example.appchamadainterface.adapters.StudentSubjectsAdapter
import com.example.appchamadainterface.models.Subject
import kotlinx.android.synthetic.main.activity_student.*

class StudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        val classes = arrayListOf("Aula dd/mm/aaaa", "Aula dd/mm/aaaa")

        val subjects = listOf(Subject("IF1001 - Programação 3", classes, 15),
            Subject("IF688 - Teoria e Implementação de Linguagens Computacionais", classes, 18))

        rv_student_subjects.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = StudentSubjectsAdapter(subjects, applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        }

        fab_student_add_subject.setOnClickListener{
            startActivity(Intent(this, SubjectEnrollActivity::class.java))
        }
    }
}
