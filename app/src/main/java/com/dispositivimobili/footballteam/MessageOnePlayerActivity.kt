package com.dispositivimobili.footballteam

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.messageallplayer.*
import kotlin.concurrent.thread

//activity per inviare un messaggio al giocatore di cui si stanno visualizzando i dati
class MessageOnePlayerActivity : AppCompatActivity() {

    //variabili utilizzate nel codice
    private val TAG = "MessageOnePlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messageoneplayeractivity)
        val name = intent.getStringExtra("name")
        textViewPresentationMessageOnePlayerActivity.setText("Mister, here you can send a message to ${name}")
    }

    //listener per ritornare a MainActivity
    fun backoneplayer(v: View){
        thread(start=true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Log.d(TAG, "return to MainActivity")
        }
    }

    //listener per inviare il messaggio
    fun checkmessageoneplayerOK(v: View){
        val phone = intent.getStringExtra("phone")
        val name = intent.getStringExtra("name")
        val testo = textMessageAllPlayerActivity.getText().toString()
        //test per controllare se la lunghezza del testo è superiore a 130 caratteri
        if(testo.length > 130) {
            //Log.d(TAG, "testo superiore a 130 caratteri")
            val testo = getString(R.string.invalidsms)
            Toast.makeText(this, "$testo", Toast.LENGTH_SHORT).show()
        } else {
            //apertura applicazione invio messaggi
            val message = "Ciao $name, sono il mister, " + testo
            val uri: Uri = Uri.parse("smsto: $phone")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            //Log.d(TAG, "message is $message")
            //Log.d(TAG, "lunghezza is ${testo.length}")
            intent.putExtra("sms_body", message)
            startActivity(intent)
            Log.d(TAG, "go to send message to all player")
        }
    }
}
