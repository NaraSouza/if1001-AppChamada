package com.example.appchamada

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
    private var message: Message? = null

    private var partsMessage: Array<String>? = null

    private val PUB_SUB_STRATEGY = Strategy.Builder().setTtlSeconds(Strategy.TTL_SECONDS_DEFAULT).build()

    private val PUB_OPTIONS = PublishOptions.Builder()
        .setStrategy(PUB_SUB_STRATEGY)
        .setCallback(object : PublishCallback() {
            override fun onExpired() {
                Toast.makeText(this@MainActivity,"Expired message", Toast.LENGTH_SHORT).show()
            }
        })
        .build()

    private val SUB_OPTIONS = SubscribeOptions.Builder()
        .setStrategy(PUB_SUB_STRATEGY)
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
                val messageStr = String(message!!.content)
                partsMessage = messageStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                txt_rcv.text = partsMessage!![0]
                ln_receive.visibility = View.VISIBLE
                ln_send.visibility = View.GONE

                Toast.makeText(this@MainActivity,"Found message: ${String(message!!.content)}",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onLost(message: Message?) {
                Toast.makeText(this@MainActivity,"Lost sight of message: ${String(message!!.content)}",
                    Toast.LENGTH_SHORT).show()
            }
        }

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

        btn_ok.setOnClickListener {
            ln_receive.visibility = View.GONE
            ln_send.visibility = View.VISIBLE
        }
    }

    public override fun onStart() {
        Nearby.getMessagesClient(this).subscribe(mMessageListener!!, SUB_OPTIONS)
            .addOnSuccessListener(this) { Log.d(TAG, "sucesso: inscrição efetuada") }
            .addOnFailureListener(this) { e -> Log.d(TAG, "falha: $e.localizedMessage") }

        super.onStart()
    }

    public override fun onStop() {
        if (message != null) {
            Nearby.getMessagesClient(this).unpublish(message!!)
        }
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener!!)

        super.onStop()
    }
}
