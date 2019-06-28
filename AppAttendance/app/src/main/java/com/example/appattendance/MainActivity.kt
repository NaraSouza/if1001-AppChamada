package com.example.appattendance

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //pegando instancia do usuario autenticado
        val auth = FirebaseAuth.getInstance()
        val userEmail = auth.currentUser!!.email.toString()
        val userId = userEmail.replace(".", "")

        //pegando referencia do banco
        val database = FirebaseDatabase.getInstance()
        val userReference = database.reference.child("users").child(userId)

        //lendo dados do banco
        userReference.child("username").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val username = dataSnapshot.getValue(String::class.java)
                tv_username.text = username
                Log.d("Valor do dado", "Value is: $username")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Falha", "Failed to read value.", error.toException())
            }
        })
    }
}
