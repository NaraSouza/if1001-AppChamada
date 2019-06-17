package com.example.appchamadainterface

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imb_professor.setOnClickListener {
            startActivity(Intent(this, ProfessorActivity::class.java))
        }

        imb_student.setOnClickListener {
            startActivity(Intent(this, StudentActivity::class.java))
        }
    }
}
