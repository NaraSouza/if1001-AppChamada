package com.example.appattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appattendance.adapters.ClassesAdapter
import com.example.appattendance.models.Class
import com.example.appattendance.models.Schedule
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
        val subjectPeriod = extras.getString("subject_period")

        getUserInfo(subjectCode!!, subjectPeriod!!)

        rv_classes.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        }

        fab_add_class.setOnClickListener {
            val intent  = Intent(this@SubjectDetailActivity, AddClassActivity::class.java)
            intent.putExtra("subject_code", subjectCode)
            intent.putExtra("subject_period", subjectPeriod)
            startActivity(intent)
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
                    var attendance = 0
                    var classDate : String

                    mListClasses.clear()

                    subjectSnapshot.child("classes").children.forEach {
                        classTitle = it.child("title").value.toString()
                        if(it.hasChild("attendance"))
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
                val listSchedules = mutableListOf<Schedule>()
                //pegando lista de dias e horarios
                subjectSnapshot.child("schedules").children.forEach {
                    timerange = it.child("timerange").value.toString()
                    weekday= it.child("weekday").value.toString()

                    listSchedules.add(Schedule(timerange, weekday))
                }

                var schedule : String
                var tmp : String
                tv_schedules.text = ""
                listSchedules.forEach {
                    schedule = it.weekday + " " + it.timerange
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

                    ll_fab.visibility = View.VISIBLE

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
