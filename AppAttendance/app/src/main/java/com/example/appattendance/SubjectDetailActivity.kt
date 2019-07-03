package com.example.appattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appattendance.adapters.ClassesAdapter
import com.example.appattendance.models.Class
import com.example.appattendance.models.Schedule
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_subject_detail.*

class SubjectDetailActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mListClasses : MutableList<Class>
    private var mUserId : String = ""
    private var mMessage : Message? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        mListClasses = mutableListOf()

        //pegando instancia do usuario autenticado
        mAuth = FirebaseAuth.getInstance()

        val userEmail = mAuth.currentUser!!.email.toString()
        mUserId = userEmail.replace(".", "")

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

        //botao para adicionar aula em disciplina (apenas para professor)
        fab_add_class.setOnClickListener {
            val intent  = Intent(this@SubjectDetailActivity, AddClassActivity::class.java)
            intent.putExtra("subject_code", subjectCode)
            intent.putExtra("subject_period", subjectPeriod)
            startActivity(intent)
        }

        //botao para confirmar presenca em aula (apenas para aluno)
        fab_confirm_attendance.setOnClickListener {
            val pubStrategy = Strategy.Builder().setDistanceType(Strategy.DISTANCE_TYPE_DEFAULT)
                .setTtlSeconds(Strategy.TTL_SECONDS_DEFAULT).build()
            val pubOptions = PublishOptions.Builder()
                .setStrategy(pubStrategy)
                .setCallback(object : PublishCallback() {
                    override fun onExpired() {
                        Toast.makeText(this@SubjectDetailActivity,"Mensagem expirada", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()

            val buffer = StringBuffer()
            //envia id do aluno para que na visao de professor este seja recebido e a presenca do aluno seja salva no Firebase
            buffer.append(mUserId)
            mMessage = Message(buffer.toString().toByteArray())

            Nearby.getMessagesClient(this)
                .publish(mMessage!!, pubOptions)
                .addOnSuccessListener(this) { Toast.makeText(this@SubjectDetailActivity,"Mensagem enviada",
                    Toast.LENGTH_SHORT).show() }
                .addOnFailureListener(this) { e -> Log.d("Nearby", "falha: $e.localizedMessage")
                    Toast.makeText(this@SubjectDetailActivity,"Erro no envio", Toast.LENGTH_SHORT).show()}
        }
    }

    override fun onStop() {
        //cancela envio de mensagem, caso esta nao seja nula
        if(mMessage != null)
            Nearby.getMessagesClient(this).unpublish(mMessage!!)

        super.onStop()
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
                val userType = dataSnapshot.child("users").child(mUserId).child("type").getValue(String::class.java)
                if(userType!!.contentEquals("professor")) {
                    val studentsCount = subjectSnapshot.child("studentscount")
                    //TODO numero total de alunos na disciplina para calcular percentual de presença na aula e na disciplina

                    ll_fab_professor.visibility = View.VISIBLE

                } else if(userType.contentEquals("student")) {
                    var professor = subjectSnapshot.child("professor").value.toString()
                    professor = "Professor(a) " + dataSnapshot.child("professors").child(professor).child("name")
                        .value.toString()
                    tv_professor.text = professor

                    ll_fab_student.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Falha", "Failed to read value.", error.toException())
            }
        })
    }
}
