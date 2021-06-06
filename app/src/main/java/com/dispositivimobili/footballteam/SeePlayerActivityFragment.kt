package com.dispositivimobili.footballteam

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_see_player.*
import kotlinx.android.synthetic.main.activity_see_player.certificationPlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.dataPlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.eliminabutton
import kotlinx.android.synthetic.main.activity_see_player.message_oneplayer
import kotlinx.android.synthetic.main.activity_see_player.modificabutton
import kotlinx.android.synthetic.main.activity_see_player.namePlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.numeromagliaPlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.phonePlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.resultsPlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.ruoloPlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player.surnamePlayerSeeActivity
import kotlinx.android.synthetic.main.activity_see_player_fragment.*
import kotlin.concurrent.thread

//fragment utilizzato per poter visualizzare i dati del giocatore selezionato nella listView
class SeePlayerActivityFragment : Fragment(){

    //variabili utilizzate nel codice
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    private var TAG = "SeePlayerActivityFragment"
    private lateinit var main : MainActivity
    var numeroIndex = 1

    //callback simile a onCreate per le activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.activity_see_player_fragment, container, false)
        numeroIndex = if(savedInstanceState?.getInt("numeroindex")== null) 0
        else { savedInstanceState.getInt("") } //aggiunta key numeroindex
        //Log.w(TAG, "sono in onCreateView, numeroindex is $numeroIndex")
        main = MainActivity()
        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("numeroindex", numeroIndex)
        //Log.w(TAG, "sono in onCreateView, numeroindex is $numeroIndex")
    }

    //callback chiamata quando tutti gli elementi dell'interfaccia grafica sono pronti
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeDescription(numeroIndex)
        //Log.w(TAG, "sono in onCreateView, numeroindex is $numeroIndex")


        //listener per modificare i dati del giocatore selezionato
        modificabutton.setOnClickListener{
            namePlayerSeeActivity.isEnabled = true
            surnamePlayerSeeActivity.isEnabled = true
            ruoloPlayerSeeActivity.isEnabled = true
            dataPlayerSeeActivity.isEnabled = true
            phonePlayerSeeActivity.isEnabled = true
            resultsPlayerSeeActivity.isEnabled = true
            certificationPlayerSeeActivity.isEnabled = true
            numeromagliaPlayerSeeActivity.isEnabled = true
            eliminabutton.isEnabled=true
            eliminabutton.setText(getString(R.string.confirm))
            modificabutton.setText(getString(R.string.modify_data))
            modificabutton.isEnabled = false
            message_oneplayer.isEnabled = false
            Log.d(TAG, "click on modify button, start to modify the data of player")
        }

        //listener per inviare un messaggio al giocatore visualizzato
        message_oneplayer.setOnClickListener(){
            val phone = phonePlayerSeeActivity.getText().toString()
            //val uri: Uri = Uri.parse("smsto: ${phonePlayerSeeActivity.getText().toString()}")
            thread(start=true) {
                //invio messaggio tramite l'activity messageoneplayer
                val intent = Intent(getActivity(), MessageOnePlayerActivity::class.java)
                intent.putExtra("phone", phone)
                intent.putExtra("name", namePlayerSeeActivity.getText().toString())
                startActivity(intent)
            }
            Log.d(TAG, "go to send message to one player")
        }

        //listener per eliminare il giocatore selezionato o confermare le modifiche effettuate
        eliminabutton.setOnClickListener(){
            val num = numeroIndex
            Log.d(TAG, "numero index value is $num")
            //val id = intent.getIntExtra("idnumero",0)
            val testo = eliminabutton.getText().toString()
            val valore = getString(R.string.delete)
            if(testo == valore) {
                //eliminazione dal db del giocatore selezionato
                reference.child(num.toString()).removeValue()
                Log.d(TAG, "click on delete button, delete player: Success")
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
                reference.child(num.toString()).setValue(helperClass)
                Log.d(TAG, "click on confirm button, modifyPlayer: Success")
                namePlayerSeeActivity.isEnabled = false
                surnamePlayerSeeActivity.isEnabled = false
                ruoloPlayerSeeActivity.isEnabled = false
                dataPlayerSeeActivity.isEnabled = false
                phonePlayerSeeActivity.isEnabled = false
                resultsPlayerSeeActivity.isEnabled = false
                certificationPlayerSeeActivity.isEnabled = false
                numeromagliaPlayerSeeActivity.isEnabled = false
                eliminabutton.setText(getString(R.string.delete))
                eliminabutton.isEnabled = false
                modificabutton.isEnabled = true
                modificabutton.setText(getString(R.string.modify_data))
                message_oneplayer.isEnabled = true
                pubblicadati(numeroIndex)
                val intent = Intent(getActivity(), MainActivity::class.java )
                startActivity(intent)
                //finish()
                //Toast.makeText(this, "ModifyPlayer success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changeDescription(index:Int) {
        numeroIndex = index
        println("NUMERO INDEX = $numeroIndex")
        Log.d(TAG, "numeroindex is ${numeroIndex}")
        pubblicadati(numeroIndex)
    }

    //metodo utilizzato per pubblicare e scrivere sulle textView tutti i dati del giocatore selezionato
    fun pubblicadati(index: Int){
        //recupero dati giocatore da firebase
        thread(start = true) {
            Log.d(TAG, "in thread numeroindex is ${index}")
            reference.child(index.toString()).get()
                .addOnSuccessListener {
                    //Log.i("firebase", "Got key ${it.key}")
                    //Log.i("firebase", "Got value ${it.value}")
                    for (i in "${it.value}") {
                        val name = it.child("name").getValue()
                        //Log.d(TAG, "name is $name")
                        val surname = it.child("surname").getValue()
                        val ruolo = it.child("ruolo").getValue()
                        val data = it.child("date").getValue()
                        val phone = it.child("phone").getValue()
                        val results = it.child("results").getValue()
                        val certification = it.child("certification").getValue()
                        val numeromaglia = it.child("idnumero").getValue()
                        namePlayerSeeActivity?.setText(name.toString())
                        surnamePlayerSeeActivity?.setText(surname.toString())
                        ruoloPlayerSeeActivity?.setText(ruolo.toString())
                        dataPlayerSeeActivity?.setText(data.toString())
                        phonePlayerSeeActivity?.setText(phone.toString())
                        resultsPlayerSeeActivity?.setText(results.toString())
                        certificationPlayerSeeActivity?.setText(certification.toString())
                        numeromagliaPlayerSeeActivity?.setText(numeromaglia.toString())
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
        }
    }
}