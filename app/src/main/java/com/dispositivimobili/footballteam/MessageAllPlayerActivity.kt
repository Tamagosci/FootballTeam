package com.dispositivimobili.footballteam

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.messageallplayer.*
import kotlin.concurrent.thread

//activity per inviare un messaggio a tutti i componenti della squadra
class MessageAllPlayerActivity : AppCompatActivity() {

    //variabili utilizzate nel codice
    private val TAG = "MessageAllPlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messageallplayer)
    }

    //listener per ritornare a MainActivity
    fun back(v: View){
        thread(start=true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Log.d(TAG, "return to MainActivity")
        }
    }

    //listener per inviare il messaggio
    fun checkmessageOK(v:View){
        val sb = intent.getStringExtra("sb")
        val testo = textMessageAllPlayerActivity.getText().toString()
        //test per controllare se la lunghezza del testo è superiore a 130 caratteri
        if(testo.length > 130) {
            //Log.d(TAG, "testo superiore a 130 caratteri")
            val testo = getString(R.string.invalidsms)
            Toast.makeText(this, "$testo", Toast.LENGTH_SHORT).show()
        } else {
            //apertura applicazione invio messaggi
            val message = "Ciao ragazzi, sono il mister, " + testo
            val uri: Uri = Uri.parse("smsto: $sb")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            //Log.d(TAG, "message is $message")
            //Log.d(TAG, "lunghezza is ${testo.length}")
            intent.putExtra("sms_body", message)
            startActivity(intent)
            Log.d(TAG, "go to send message to all player")
        }
    }
}