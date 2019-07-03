package com.example.appattendance

import android.app.IntentService
import android.content.Intent
import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.example.appattendance.action.FOO"
private const val ACTION_BAZ = "com.example.appattendance.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.example.appattendance.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.appattendance.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class NearbyService : IntentService("NearbyService") {
    private var mMessageListener: MessageListener? = null
    private val SUB_STRATEGY = Strategy.Builder().setDistanceType(Strategy.DISTANCE_TYPE_DEFAULT).build()

    private val SUB_OPTIONS = SubscribeOptions.Builder()
        .setStrategy(SUB_STRATEGY)
        .setCallback(object : SubscribeCallback() {
            override fun onExpired() {
                //Toast.makeText(this@MainActivity,"Expired subscription", Toast.LENGTH_SHORT).show()
            }
        })
        .build()

    override fun onHandleIntent(intent: Intent?) {
        Nearby.getMessagesClient(this).subscribe(mMessageListener!!, SUB_OPTIONS)

        //Recebendo mensagem
        mMessageListener = object : MessageListener() {

            override fun onFound(message: Message?) {
                val studentId = String(message!!.content)
                //TODO pegar informacoes da aula corrente para colocar presenca do aluno no Firebase

                //Toast.makeText(this@MainActivity,"Found message: ${String(message!!.content)}",
                 //   Toast.LENGTH_SHORT).show()
            }

            override fun onLost(message: Message?) {
                //Toast.makeText(this@MainActivity,"Lost sight of message: ${String(message!!.content)}",
                  //  Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        TODO("Handle action Foo")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        TODO("Handle action Baz")
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, NearbyService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, NearbyService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }
}
