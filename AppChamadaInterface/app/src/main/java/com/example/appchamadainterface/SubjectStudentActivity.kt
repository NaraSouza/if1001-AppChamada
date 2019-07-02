package com.example.appchamadainterface

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appchamadainterface.adapters.StudentSubjectsAdapter
import com.example.appchamadainterface.models.Subject
import kotlinx.android.synthetic.main.activity_subject.*
import kotlinx.android.synthetic.main.activity_subject_student.*
import kotlinx.android.synthetic.main.activity_subject_student.view.*

class SubjectStudentActivity : Fragment() {

    private val classes = arrayListOf("Aula dd/mm/aaaa", "Aula dd/mm/aaaa")

    private val subjects = listOf(
            Subject("IF1001 - Programação 3", classes, 15),
            Subject("IF688 - Teoria e Implementação de Linguagens Computacionais", classes, 18)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_subject_student, container, false)

        view.fab_student_add_subject.setOnClickListener {
            startActivity(Intent(context, SubjectEnrollActivity::class.java))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_student_subjects.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = StudentSubjectsAdapter(subjects, context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    companion object {
        fun newInstance(): SubjectStudentActivity = SubjectStudentActivity()
    }
}