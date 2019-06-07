package com.example.appchamada

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "NearbyAPI_Test"
    private var message: Message? = null

    private val PUB_STRATEGY = Strategy.Builder().setDistanceType(Strategy.DISTANCE_TYPE_DEFAULT).build()

    private val PUB_OPTIONS = PublishOptions.Builder()
        .setStrategy(PUB_STRATEGY)
        .setCallback(object : PublishCallback() {
            override fun onExpired() {
                Toast.makeText(this@MainActivity,"Expired message", Toast.LENGTH_SHORT).show()
            }
        })
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Enviando mensagem
        btn_send.setOnClickListener {
            val buffer = StringBuffer()
            buffer.append(edt_send.text.toString())
            message = Message(buffer.toString().toByteArray())

                Nearby.getMessagesClient(this)
                    .publish(message!!, PUB_OPTIONS)
                    .addOnSuccessListener(this) { Log.d(TAG, "sucesso: mensagem enviada") }
                    .addOnFailureListener(this) { e -> Log.d(TAG, "falha: $e.localizedMessage") }
        }
    }

    public override fun onStop() {
        if (message != null) {
            Nearby.getMessagesClient(this).unpublish(message!!)
        }

        super.onStop()
    }
}
