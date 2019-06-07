package com.example.appchamadareceive

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mMessageListener: MessageListener? = null
    private val TAG = "NearbyAPI_Test"

    private var partsMessage: Array<String>? = null

    private val SUB_STRATEGY = Strategy.Builder().setDistanceType(Strategy.DISTANCE_TYPE_DEFAULT).build()

    private val SUB_OPTIONS = SubscribeOptions.Builder()
        .setStrategy(SUB_STRATEGY)
        .setCallback(object : SubscribeCallback() {
            override fun onExpired() {
                Toast.makeText(this@MainActivity,"Expired subscription", Toast.LENGTH_SHORT).show()
            }
        })
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Recebendo mensagem
        mMessageListener = object : MessageListener() {

            override fun onFound(message: Message?) {
                txt_rcv.text = String(message!!.content)

                Toast.makeText(this@MainActivity,"Found message: ${String(message!!.content)}",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onLost(message: Message?) {
                Toast.makeText(this@MainActivity,"Lost sight of message: ${String(message!!.content)}",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onStart() {
        Nearby.getMessagesClient(this).subscribe(mMessageListener!!, SUB_OPTIONS)
            .addOnSuccessListener(this) { Log.d(TAG, "sucesso: inscrição efetuada") }
            .addOnFailureListener(this) { e -> Log.d(TAG, "falha: $e.localizedMessage") }

        super.onStart()
    }

    public override fun onStop() {
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener!!)

        super.onStop()
    }
}
