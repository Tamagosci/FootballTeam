package com.dispositivimobili.footballteam

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_see_player.*

class SeePlayerActivity : AppCompatActivity() {
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    private var TAG = "SeePlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_player)

        //recupero dati da PrincipalActivity
        val intent: Intent = getIntent()
        val id = intent.getIntExtra("idnumero",0)

        //recupero dati giocatore da firebase
        reference.child(id.toString()).get()
            .addOnSuccessListener {
                //Log.i("firebase", "Got key ${it.key}")
                //Log.i("firebase", "Got value ${it.value}")
                for(i in "${it.value}"){
                    val name = it.child("name").getValue()
                    val surname = it.child("surname").getValue()
                    val ruolo = it.child("ruolo").getValue()
                    val data = it.child("date").getValue()
                    val phone = it.child("phone").getValue()
                    val results = it.child("results").getValue()
                    val certification = it.child("certification").getValue()
                    val numeromaglia = it.child("idnumero").getValue()
                    namePlayerSeeActivity.setText(name.toString())
                    surnamePlayerSeeActivity.setText(surname.toString())
                    ruoloPlayerSeeActivity.setText(ruolo.toString())
                    dataPlayerSeeActivity.setText(data.toString())
                    phonePlayerSeeActivity.setText(phone.toString())
                    resultsPlayerSeeActivity.setText(results.toString())
                    certificationPlayerSeeActivity.setText(certification.toString())
                    numeromagliaPlayerSeeActivity.setText(numeromaglia.toString())
                }
            }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
            }
    }

    //listener per ritornare alla PrincipalActivity
    fun onReturn(v: View){
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
        finish()
        Log.d(TAG, "click on back button, go to PrincipalAcitvity")
    }

    //listener per inviare un messaggio al giocatore visualizzato
    fun onMessagePlayer(v: View){
        val message = "Ciao " + namePlayerSeeActivity.getText().toString() + ", sono il mister, "
        val uri: Uri = Uri.parse("smsto: ${phonePlayerSeeActivity.getText().toString()}")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", message)
        startActivity(intent)
        Log.d(TAG, "go to send message to one player")
    }

    //listener per modificare i dati del giocatore selezionato
    fun onModify(v: View){
        namePlayerSeeActivity.isEnabled = true
        surnamePlayerSeeActivity.isEnabled = true
        ruoloPlayerSeeActivity.isEnabled = true
        dataPlayerSeeActivity.isEnabled = true
        phonePlayerSeeActivity.isEnabled = true
        resultsPlayerSeeActivity.isEnabled = true
        certificationPlayerSeeActivity.isEnabled = true
        numeromagliaPlayerSeeActivity.isEnabled = true
        eliminabutton.setText(getString(R.string.confirm))
        modificabutton.setText(getString(R.string.modify_data))
        modificabutton.isEnabled = false
        message_oneplayer.isEnabled = false
        Log.d(TAG, "click on modify button, start to modify the data of player")
    }

    //listener per eliminare il giocatore selezionato o confermare le modifiche effettuare
    fun deleteORconfirm(v: View){
        val id = intent.getIntExtra("idnumero",0)
        val testo = eliminabutton.getText().toString()
        if(testo == "ELIMINA") {
            //eliminazione dal db del giocatore selezionato
            reference.child(id.toString()).removeValue()
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()
            Log.d(TAG, "click on delete button, delete player: Success")
        } else {
            val name = namePlayerSeeActivity.getText().toString()
            val surname = surnamePlayerSeeActivity.getText().toString()
            val date = dataPlayerSeeActivity.getText().toString()
            val phone = phonePlayerSeeActivity.getText().toString()
            val ruolo = ruoloPlayerSeeActivity.getText().toString()
            val results = resultsPlayerSeeActivity.getText().toString()
            val certification = certificationPlayerSeeActivity.getText().toString()
            val numeromaglia = numeromagliaPlayerSeeActivity.getText().toString()
            val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification, numeromaglia)

            //ripristino sul db del giocatore modificato
            reference.child(id.toString()).setValue(helperClass)
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            finish()
            Log.d(TAG, "click on confirm button, modifyPlayer: Success")
            //Toast.makeText(this, "ModifyPlayer success", Toast.LENGTH_SHORT).show()
        }
    }
}