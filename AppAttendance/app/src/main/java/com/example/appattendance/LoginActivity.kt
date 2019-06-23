package com.example.appattendance

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_sign_in.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                pb_loading.visibility = View.VISIBLE

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful) {
                            updateUI()
                        } else {
                            Toast.makeText(this@LoginActivity, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        btn_sign_up.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(Intent(intent))
        }
    }

    private fun updateUI() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
