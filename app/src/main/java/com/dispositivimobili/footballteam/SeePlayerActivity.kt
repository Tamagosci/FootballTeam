package com.dispositivimobili.footballteam

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_see_player.*
import kotlin.concurrent.thread

//activity utilizzata per poter visualizzare i dati del giocatore selezionato nella listView
class SeePlayerActivity : AppCompatActivity() {

    //variabili utilizzate nel codice
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    private var TAG = "SeePlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_player)

        //recupero dati da PrincipalActivityFragmentPortrait
        val intent: Intent = getIntent()
        val id = intent.getIntExtra("idnumero", 0)

        //recupero dati giocatore da firebase
        thread(start = true) {
            reference.child(id.toString()).get()
                .addOnSuccessListener {
                    //Log.i("firebase", "Got key ${it.key}")
                    //Log.i("firebase", "Got value ${it.value}")
                    for (i in "${it.value}") {
                        //scrivo nelle textView i vari dati
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
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
        }
    }

    //listener per poter ritornare alla MainActivity
    fun onReturn(v: View) {
        thread(start = true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Log.d(TAG, "click on back button, go to MainActivity")
        }
    }

    //listener per inviare un messaggio al giocatore visualizzato
    fun onMessagePlayer(v: View){
        val phone = phonePlayerSeeActivity.getText().toString()
        //val uri: Uri = Uri.parse("smsto: ${phonePlayerSeeActivity.getText().toString()}")
        thread(start=true) {
            //invio messaggio tramite l'activity messageoneplayer
            val intent = Intent(this, MessageOnePlayerActivity::class.java)
            intent.putExtra("phone", phone)
            intent.putExtra("name", namePlayerSeeActivity.getText().toString())
            startActivity(intent)
        }
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

    //listener per eliminare il giocatore selezionato o confermare le modifiche effettuate
    fun deleteORconfirm(v: View){
        var id = intent.getIntExtra("idnumero",0)
        val testo = eliminabutton.getText().toString()
        val valore = getString(R.string.delete)
        if(testo == valore) {
            //eliminazione dal db del giocatore selezionato
            reference.child(id.toString()).removeValue()
            Log.d(TAG, "click on delete button, delete player: Success")
            //quando eliminiamo un giocatore che non e' l'ultimo nella lista
            val rowcount = intent.getIntExtra("rowcount",0)
            //Log.d(TAG, "id numero is $id")
            //Log.d(TAG, "rowcount is $rowcount")
            if(id!=rowcount){
                var i = (id + 1)
                var idadd = id
                var idremove = id + 1
                while(i<=rowcount){
                    //Log.d(TAG, "i value is $i")
                    reference.child(i.toString()).get()
                        .addOnSuccessListener {
                            //Log.i("firebase", "Got key ${it.key}")
                            //Log.i("firebase", "Got value ${it.value}")
                            thread(start=true) {
                                for (j in "${it.value}") {
                                    //Log.d(TAG, "prendo i vari dati")
                                    val name = it.child("name").getValue().toString()
                                    val surname = it.child("surname").getValue().toString()
                                    val ruolo = it.child("ruolo").getValue().toString()
                                    val data = it.child("date").getValue().toString()
                                    val phone = it.child("phone").getValue().toString()
                                    val results = it.child("results").getValue().toString()
                                    val certification = it.child("certification").getValue().toString()
                                    val numeromaglia = it.child("idnumero").getValue().toString()
                                    val helperClass: Player = Player(name, surname, data, phone, ruolo, results, certification, numeromaglia)
                                    Thread.sleep(100)
                                    //Log.d(TAG, "ho preso i dati")
                                    //Log.d(TAG, "id value is $id")
                                    reference.child(id.toString()).setValue(helperClass)
                                    //Log.d(TAG, "valori settati a $idadd")
                                    reference.child((id+1).toString()).removeValue()
                                    //Log.d(TAG, "valori rimossi a $idremove")
                                    idadd++
                                    idremove++
                                    break
                                }
                            }
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }
                    //val newid = (i - 1).toString()
                    //Log.d(TAG, "i new value is $newid")
                    //Log.d(TAG, "id new value is $id")
                    i++
                }
            }

            Thread.sleep(300)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Log.d(TAG, "click on delete button, change id player: Success")
        } else {
            //prelevo dalle textView i vari dati
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
            val intent = Intent(this, MainActivity::class.java )
            startActivity(intent)
            finish()
            Log.d(TAG, "click on confirm button, modifyPlayer: Success")
            //Toast.makeText(this, "ModifyPlayer success", Toast.LENGTH_SHORT).show()
        }
    }
}