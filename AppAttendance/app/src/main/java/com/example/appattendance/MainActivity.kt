package com.example.appattendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appattendance.adapters.SubjectsAdapter
import com.example.appattendance.models.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private var mUserType : String? = ""
    private lateinit var mListSubjects : MutableList<Subject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListSubjects = mutableListOf()

        //pegando instancia do usuario autenticado
        mAuth = FirebaseAuth.getInstance()

        getUserInfo()

        rv_subjects.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        }

        //botao de logout
        btn_sign_out.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            //mata activity atual para que usuario nao volte para ela apos se cadastrar e logar
            finish()
        }
    }

    /**
     * Lê informações do usuário do Firebase
     */
    private fun getUserInfo() {

        val userEmail = mAuth.currentUser!!.email.toString()
        val userId = userEmail.replace(".", "")

        //pegando referencia do banco
        val databaseReference = FirebaseDatabase.getInstance().reference

        //lendo dados do banco
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val userSnapshot = dataSnapshot.child("users").child(userId)
                tv_username.text = userSnapshot.child("username").getValue(String::class.java)
                mUserType = userSnapshot.child("type").getValue(String::class.java)

                lateinit var snapshot : DataSnapshot
                if(mUserType!!.contentEquals("professor")) {
                    snapshot = dataSnapshot.child("professors").child(userId)

                } else if(mUserType!!.contentEquals("student")) {
                    snapshot = dataSnapshot.child("students").child(userId)

                }

                if(snapshot.child("subjects").exists()) {
                    var subjectCode : String?
                    var subjectName : String?
                    var subjectPeriod : String?

                    mListSubjects.clear()

                    snapshot.child("subjects").children.forEach {
                        subjectCode = it.key.toString()
                        subjectName = it.child("name").getValue(String::class.java)
                        subjectPeriod = it.child("period").getValue(String::class.java)

                        mListSubjects.add(Subject(subjectName!!, subjectCode!!, subjectPeriod!!))
                        Log.d("ListSubjects", "adicionando item na lista" + mListSubjects.toString() + mListSubjects.size)
                    }

                    rv_subjects.adapter = SubjectsAdapter(mListSubjects, this@MainActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Falha", "Failed to read value.", error.toException())
            }
        })
    }
}
