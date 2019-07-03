package com.example.appattendance

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //botao de login
        btn_sign_in.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            //verifica se os campos de email e senha foram preenchidos
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                btn_sign_in.isEnabled = false
                pb_loading.visibility = View.VISIBLE

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful) {
                            updateUI()
                        } else {
                            btn_sign_in.isEnabled = true
                            pb_loading.visibility = View.GONE
                        }
                    }
                    .addOnFailureListener {
                        when(it.message.toString()) {
                            "The email address is badly formatted." -> Toast.makeText(this@LoginActivity,
                                "Digite um e-mail válido", Toast.LENGTH_SHORT).show()
                            "The password is invalid or the user does not have a password." ->
                                    Toast.makeText(this@LoginActivity, "Senha incorreta", Toast.LENGTH_SHORT).show()
                            "There is no user record corresponding to this identifier. The user may have been deleted." ->
                                Toast.makeText(this@LoginActivity, "E-mail não cadastrado", Toast.LENGTH_SHORT).show()
                            else -> {
                                Toast.makeText(this@LoginActivity, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                                Log.d("Erro no login", it.message.toString())
                            }

                        }
                        btn_sign_in.isEnabled = true
                        pb_loading.visibility = View.GONE
                    }
            } else {
                Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        //botao para tela de cadastro
        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            updateUI()
        }

        super.onStart()
    }

    /**
     * Envia usuario logado para a Main Activity
     */
    private fun updateUI() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        //mata activity atual para que usuario nao volte para ela apos logar
        finish()
    }
}
