package com.example.appattendance

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
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
    private lateinit var mUsersReference: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()

        //botao de cadastro
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

            //verifica se todos os campos foram preenchidos
            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(type)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                btn_register.isEnabled = false
                edt_password.onEditorAction(EditorInfo.IME_ACTION_DONE)
                pb_loading.visibility = View.VISIBLE

                //cria usuario no Fireabse e ja o autentica
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val userId = email.replace(".", "")

                            //salva no banco informacoes do usuario
                            val userReference = mUsersReference.child(userId)
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

                            //caso usuario seja um professor
                            if(type.contentEquals("professor")) {
                                val profReference = mDatabase.reference.child("professors").child(userId)
                                profReference.child("name").setValue(name)
                            }

                            //caso usuario seja um aluno
                            if(type.contentEquals("student")) {
                                val studReference = mDatabase.reference.child("students").child(userId)
                                studReference.child("name").setValue(name)
                            }

                            updateUI()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Falha no cadastro", Toast.LENGTH_SHORT).show()
                            if(task.exception.toString().contentEquals("com.google.firebase.auth." +
                                        "FirebaseAuthUserCollisionException: The email address is already in use " +
                                        "by another account.")) {
                                tv_registration_error.visibility = View.VISIBLE
                            }
                        }
                        pb_loading.visibility = View.GONE
                        btn_register.isEnabled = true
                    }
            } else {
                Toast.makeText(this@RegisterActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        //tirar mensagem de email ja cadastrado ao apagar campo de email
        edt_email.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                tv_registration_error.visibility = View.GONE
            }
        })
    }

    /**
     * Inicializa variaveis do Firebase
     */
    private fun init() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mUsersReference = mDatabase.reference.child("users")
    }

    /**
     * Envia usuario cadastrado para a Main Activity
     */
    private fun updateUI() {
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        //mata activity atual para que usuario nao volte para ela apos se cadastrar e logar
        finish()
    }
}
