package com.example.appattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_subject.*

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
                startActivity(Intent(this@AddSubjectActivity, CreateSubjectActivity::class.java))
            }
        }

        //botao para participar de turma/disciplina
        btn_join.setOnClickListener {
            if(!TextUtils.isEmpty(edt_insert_code.text)) {
                edt_insert_code.onEditorAction(EditorInfo.IME_ACTION_DONE)
                pb_loading.visibility = View.VISIBLE
                btn_join.isEnabled = false

                //identificando disciplina
                mDatabaseReference.child("subjects").orderByChild("regcode")
                    .equalTo(edt_insert_code.text.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            var subjectId : String
                            var subjectName : String
                            datasnapshot.children.forEach {
                                subjectId = it.key.toString()
                                subjectName = it.child("name").value.toString()

                                //escrevendo dados da disciplina nos respectivos campos de professor/aluno
                                val userEmail = mAuth.currentUser!!.email.toString()
                                val userId = userEmail.replace(".", "")

                                lateinit var reference : DatabaseReference
                                if(userType.contentEquals("professor")) {
                                    reference = mDatabaseReference.child("professors")

                                } else if(userType.contentEquals("student")) {
                                    reference = mDatabaseReference.child("students")
                                }

                                //identificando periodo corrente
                                var period = ""
                                it.child("periods").children.forEach {
                                    if(it.child("current").value == true) {
                                        period = it.key.toString()
                                    }
                                }
                                reference.child(userId).child("subjects").child(subjectId)
                                    .child("name").setValue(subjectName)
                                reference.child(userId).child("subjects").child(subjectId)
                                    .child("period").setValue(period)

                                //adicionando aluno na disciplina
                                if(userType.contentEquals("student")) {
                                    mDatabaseReference.child("subjects").child(subjectId)
                                        .child("periods").child(period).child("students")
                                        .child(userId).setValue(true)
                                }
                                Toast.makeText(this@AddSubjectActivity,
                                    "Turma adicionada com sucesso", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.d("Falha", "Failed to read value.", error.toException())
                        }
                    })
                pb_loading.visibility = View.GONE
                btn_join.isEnabled = true
            } else {
                Toast.makeText(this@AddSubjectActivity, "Insira o código da turma", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
