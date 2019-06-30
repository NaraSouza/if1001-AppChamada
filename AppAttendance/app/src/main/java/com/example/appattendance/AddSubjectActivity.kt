package com.example.appattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_subject.*
import java.lang.Boolean.TRUE

class AddSubjectActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        val extras = intent.extras
        val userType = extras!!.getString("user_type")

        if(userType!!.contentEquals("professor")) {
            btn_create.visibility = View.VISIBLE

            //botao para criar disciplina/turma
            btn_create.setOnClickListener {

            }
        }

        //botao para participar de turma/disciplina
        btn_join.setOnClickListener {
            if(!TextUtils.isEmpty(edt_insert_code.text)) {
                //identificando disciplina
                mDatabaseReference.child("subjects").orderByChild("regcode")
                    .equalTo(edt_insert_code.text.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            var subjectId = ""
                            var subjectName = ""
                            datasnapshot.children.forEach {
                                subjectId = it.key.toString()
                                subjectName = it.child("name").value.toString()
                                Log.d("ID da disciplina", subjectId)
                                Log.d("Nome da disciplina", subjectName)

                                //escrevendo dados da disciplina nos respectivos campos de professor/aluno
                                val userEmail = mAuth.currentUser!!.email.toString()
                                val userId = userEmail.replace(".", "")

                                lateinit var reference : DatabaseReference
                                if(userType.contentEquals("professor")) {
                                    reference = mDatabaseReference.child("professors")

                                } else if(userType.contentEquals("student")) {
                                    reference = mDatabaseReference.child("students")
                                }

                                reference.child(userId).child("subjects").child(subjectId)
                                    .child("name").setValue(subjectName)

                                //identificando periodo corrente
                                var period = ""
                                mDatabaseReference.child("subjects").child(subjectId).child("periods").orderByChild("current")
                                    .equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(datasnapshot: DataSnapshot) {
                                            Log.d("Query result", datasnapshot.key.toString())
                                            datasnapshot.children.forEach {
                                                period = it.key.toString()
                                                Log.d("Period", period)

                                                reference.child(userId).child("subjects").child(subjectId)
                                                    .child("period").setValue(period)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            // Failed to read value
                                            Log.d("Falha", "Failed to read value.", error.toException())
                                        }
                                    })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.d("Falha", "Failed to read value.", error.toException())
                        }
                    })

            } else {
                Toast.makeText(this@AddSubjectActivity, "Insira o código da turma", Toast.LENGTH_SHORT).show()
            }
        }
    }
}