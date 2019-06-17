package com.example.appchamadainterface

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ll_professor.setOnClickListener {
            startActivity(Intent(this, ProfessorActivity::class.java))
        }

        ll_aluno.setOnClickListener {
            startActivity(Intent(this, AlunoActivity::class.java))
        }
    }
}
