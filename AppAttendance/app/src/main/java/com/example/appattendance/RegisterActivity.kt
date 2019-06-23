package com.example.appattendance

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDatabase : FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()

        btn_register.setOnClickListener {
            val name = edt_name.text.toString()
            val username = edt_username.text.toString()

            var type = ""
            if(rb_professor.isChecked) {
                type = "professor"
            } else if(rb_student.isChecked) {
                type = "student"
            }

            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(type)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                pb_loading.visibility = View.VISIBLE

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val userId = email.replace(".", "")

                            val userReference = mDatabaseReference.child(userId)
                            userReference.child("name").setValue(name)
                            userReference.child("username").setValue(username)
                            userReference.child("type").setValue(type)

                            var regdate : String
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val current = LocalDateTime.now()
                                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                regdate =  current.format(formatter)
                            } else {
                                val date = Date()
                                val formatter = SimpleDateFormat("dd.MM.yyyy")
                                regdate = formatter.format(date)
                            }

                            regdate = regdate.replace('.', '/')
                            userReference.child("regdate").setValue(regdate)

                            updateUI()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Falha no cadastro", Toast.LENGTH_SHORT).show()
                        }
                        pb_loading.visibility = View.GONE
                    }
            } else {
                Toast.makeText(this@RegisterActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("users")
    }

    private fun updateUI() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
