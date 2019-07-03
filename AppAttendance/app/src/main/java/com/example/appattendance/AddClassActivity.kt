package com.example.appattendance

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_class.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class AddClassActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_class)

        val extras = intent.extras
        val subjectId = extras!!.getString("subject_code")
        val subjectPeriod = extras.getString("subject_period")

        var classDate : String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            classDate =  current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            classDate = formatter.format(date)
        }

        classDate = classDate.replace('.', '-')
        tv_class_date.text= classDate

        //botao para criar aula
        btn_create_class.setOnClickListener {
            val title = edt_class_title.text.toString()

            if(!TextUtils.isEmpty(title)) {
                val databaseReference = FirebaseDatabase.getInstance().reference

                //salvando aula no Firebase
                databaseReference.child("subjects").child(subjectId!!).child("periods")
                    .child(subjectPeriod!!).child("classes").child(classDate).child("title")
                    .setValue(title)

                Toast.makeText(this@AddClassActivity, "Aula criada com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@AddClassActivity, "Digite o título da aula", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
