package com.example.appattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appattendance.adapters.ClassesAdapter
import com.example.appattendance.models.Class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_subject_detail.*

class SubjectDetailActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private var mUserType : String? = ""
    private lateinit var mListClasses : MutableList<Class>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        mListClasses = mutableListOf()

        //pegando instancia do usuario autenticado
        mAuth = FirebaseAuth.getInstance()

        val extras = intent.extras
        val subjectCode = extras!!.getString("subject_code")
        val subjectName = extras.getString("subject_name")
        val subjectTitle = "$subjectCode - $subjectName"
        tv_subject_title.text = subjectTitle

        getUserInfo(subjectCode!!, extras.getString("subject_period")!!)

        if(mUserType!!.contentEquals("")) {

        }

        rv_classes.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        }
    }

    /**
     * Lê informações do usuário do Firebase
     */
    private fun getUserInfo(id : String, period : String) {
        //pegando referencia do banco
        val databaseReference = FirebaseDatabase.getInstance().reference

        //lendo dados do banco
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val subjectSnapshot = dataSnapshot.child("subjects").child(id).child("periods").child(period)

                //pegando lista de aulas da disciplina
                if(subjectSnapshot.child("classes").exists()) {
                    var classTitle : String
                    var attendance : Int
                    var classDate : String

                    subjectSnapshot.child("classes").children.forEach {
                        classTitle = it.child("title").value.toString()
                        attendance = it.child("attendance").getValue(Int::class.java)!!
                        classDate = it.key.toString()

                        mListClasses.add(Class(classTitle, attendance, classDate))
                        Log.d("ListClasses", "adicionando item na lista" + mListClasses.toString() + mListClasses.size)
                    }

                    rv_classes.adapter = ClassesAdapter(mListClasses, this@SubjectDetailActivity)
                }

                val classroom = subjectSnapshot.child("classroom").value.toString()
                tv_classroom.text = classroom
                var courseLoad = subjectSnapshot.child("courseload").value
                //TODO usar carga horaria para calcular maximo de faltas permitidas na disciplina

                var timerange : String
                var weekday : String
                var schedule : String
                var tmp : String
                //pegando lista de dias e horarios
                subjectSnapshot.child("schedules").children.forEach {
                    timerange = it.child("timerange").value.toString()
                    weekday= it.child("weekday").value.toString()

                    schedule = "$weekday $timerange"
                    tmp = tv_schedules.text.toString()
                    tmp = "$tmp $schedule\n"
                    tv_schedules.text = tmp
                }

                //identificando tipo de usuario
                val userEmail = mAuth.currentUser!!.email.toString()
                val userId = userEmail.replace(".", "")

                mUserType = dataSnapshot.child("users").child(userId).child("type").getValue(String::class.java)
                if(mUserType!!.contentEquals("professor")) {
                    val studentsCount = subjectSnapshot.child("studentscount")
                    //TODO numero total de alunos na disciplina para calcular percentual de presença na aula e na disciplina

                } else if(mUserType!!.contentEquals("student")) {
                    var professor = subjectSnapshot.child("professor").value.toString()
                    professor = "Professor(a) " + dataSnapshot.child("professors").child(professor).child("name")
                        .value.toString()
                    tv_professor.text = professor
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Falha", "Failed to read value.", error.toException())
            }
        })
    }
}
