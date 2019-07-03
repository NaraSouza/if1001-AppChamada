package com.example.appattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_create_subject.*
import kotlinx.android.synthetic.main.activity_create_subject.pb_loading
import java.util.*

class CreateSubjectActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        //botao para criar turma/disciplina
        btn_create_subject.setOnClickListener {
            val subjectCode = edt_subject_code.text.toString()
            val subjectName = edt_subject_name.text.toString()
            val classroom = edt_classroom.text.toString()
            val courseLoad = edt_course_load.text.toString()
            val atLeastOneChecked = checking()

            if(!TextUtils.isEmpty(subjectCode) && !TextUtils.isEmpty(subjectName) && !TextUtils.isEmpty(classroom) &&
                !TextUtils.isEmpty(courseLoad) && atLeastOneChecked) {
                Log.d("Create", "Entrou no if")
                pb_loading.visibility = View.VISIBLE
                //btn_create_subject.isEnabled = false

                val userEmail = mAuth.currentUser!!.email.toString()
                val professorId = userEmail.replace(".", "")

                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR)

                //calculando periodo corrente
                val period = when(cal.get(Calendar.MONTH)) {
                    0, 1, 2, 3, 4, 5, 6, 7 -> "$year-1"
                    else -> "$year-2"
                }

                val reference = mDatabaseReference.child("subjects")

                //verificando se ja existe uma turma da disciplina no periodo corrente
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val regCode : String
                        if(dataSnapshot.hasChild(subjectCode) && dataSnapshot.child(subjectCode).child("periods")
                                .hasChild(period)) {
                            regCode = dataSnapshot.child(subjectCode).child("regcode").value.toString()

                            var errorText = tv_class_exists_error.text.toString()
                            errorText = "$errorText $regCode"
                            tv_class_exists_error.text = errorText
                            tv_class_exists_error.visibility = View.VISIBLE
                        } else {
                            //cadastrando disciplina
                            reference.child(subjectCode).child("name").setValue(subjectName)
                            val temp = subjectCode.toUpperCase()
                            regCode = "$temp$period"
                            reference.child(subjectCode).child("regcode").setValue(regCode)
                            reference.child(subjectCode).child("periods").child(period)
                                .child("classroom").setValue(classroom)
                            reference.child(subjectCode).child("periods").child(period)
                                .child("courseload").setValue(courseLoad)
                            reference.child(subjectCode).child("periods").child(period)
                                .child("current").setValue(true)
                            reference.child(subjectCode).child("periods").child(period)
                                .child("professor").setValue(professorId)
                            mDatabaseReference.child("professors").child(professorId)
                                .child("subjects").child(subjectCode).child("name")
                                .setValue(subjectName)
                            mDatabaseReference.child("professors").child(professorId)
                                .child("subjects").child(subjectCode).child("period")
                                .setValue(period)
                            var count = 0
                            var timerange : String
                            val reference2 = reference.child(subjectCode).child("periods")
                                .child(period).child("schedules")
                            if(cb_monday.isChecked) {
                                count++
                                timerange = time1_monday.text.toString() + "-" + time2_monday.text.toString()
                                reference2.child("schedule$count").child("timerange")
                                    .setValue(timerange)
                                reference2.child("schedule$count").child("weekday")
                                    .setValue("Segunda-feira")
                            }
                            if(cb_tuesday.isChecked) {
                                count++
                                timerange = time1_tuesday.text.toString() + "-" + time2_tuesday.text.toString()
                                reference2.child("schedule$count").child("timerange")
                                    .setValue(timerange)
                                reference2.child("schedule$count").child("weekday")
                                    .setValue("Terça-feira")
                            }
                            if(cb_wednesday.isChecked) {
                                count++
                                timerange = time1_wednesday.text.toString() + "-" + time2_wednesday.text.toString()
                                reference2.child("schedule$count").child("timerange")
                                    .setValue(timerange)
                                reference2.child("schedule$count").child("weekday")
                                    .setValue("Quarta-feira")
                            }
                            if(cb_thursday.isChecked) {
                                count++
                                timerange = time1_thursday.text.toString() + "-" + time2_thursday.text.toString()
                                reference2.child("schedule$count").child("timerange")
                                    .setValue(timerange)
                                reference2.child("schedule$count").child("weekday")
                                    .setValue("Quinta-feira")
                            }
                            if(cb_friday.isChecked) {
                                count++
                                timerange = time1_friday.text.toString() + "-" + time2_friday.text.toString()
                                reference2.child("schedule$count").child("timerange")
                                    .setValue(timerange)
                                reference2.child("schedule$count").child("weekday")
                                    .setValue("Sexta-feira")
                            }

                            //setando periodo anterior para current==false
                            var olderPeriod : String
                            reference.child(subjectCode).child("periods").orderByChild("current")
                                .equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        dataSnapshot.children.forEach {
                                            olderPeriod = it.key.toString()

                                            if(!olderPeriod.contentEquals(period)) {
                                                reference.child(subjectCode).child("periods").child(olderPeriod)
                                                    .child("current").setValue(false)
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Failed to read value
                                        Log.d("Falha", "Failed to read value.", error.toException())
                                    }
                                })

                            Toast.makeText(this@CreateSubjectActivity,"Turma adicionada com sucesso",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.d("Falha", "Failed to read value.", error.toException())
                    }
                })
                pb_loading.visibility = View.GONE
                btn_create_subject.isEnabled = true
            } else {
                Toast.makeText(this@CreateSubjectActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        //tirar mensagem de turma já cadastrada ao apagar campo de código da turma
        edt_subject_code.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                tv_class_exists_error.visibility = View.GONE
            }
        })
    }

    /**
     * Verifica quais checkboxes estão selecionadas para mostrar campo de horários respectivo
     */
    private fun checking() : Boolean {
        var atLeastOneChecked = false

        if(cb_monday.isChecked)
            atLeastOneChecked = true
        if(cb_tuesday.isChecked)
            atLeastOneChecked = true
        if(cb_wednesday.isChecked)
            atLeastOneChecked = true
        if(cb_thursday.isChecked)
            atLeastOneChecked = true
        if(cb_friday.isChecked)
            atLeastOneChecked = true

        return atLeastOneChecked
    }
}
