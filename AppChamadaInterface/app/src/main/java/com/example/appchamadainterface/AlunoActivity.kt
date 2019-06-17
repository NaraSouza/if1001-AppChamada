package com.example.appchamadainterface

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.appchamadainterface.Model.Subject
import kotlinx.android.synthetic.main.activity_aluno.*

class AlunoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        val list = listOf(Subject("IF1001 - Programação 3"), Subject("IF688 - Teoria e Implementação de Linguagens Computacionais"))

        rv_student_subjects.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = StudentSubjectsAdapter(list, applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        }
    }
}
