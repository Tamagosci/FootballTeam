package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activityprincipal_notoolbar.*
import kotlinx.android.synthetic.main.add_player.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

//fragment utilizzato per poter aggiungere un nuovo giocatore
class AddFootballerActivityFragment : Fragment() {

    //variabili utilizzate nel codice
    private var TAG = "AddFootballerActivityFragment"
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    var rowcount = 0

    //callback simile a onCreate per le activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_player, container, false)
        //rowcount = if(savedInstanceState?.getInt("numerorow")== null) 0
        //else { savedInstanceState.getInt("numerorow") } //aggiunta key numeroindex
        //Log.w(TAG, "sono in onCreateView, rowcount is $rowcount")
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putInt("numerorow", rowcount)
        //Log.w(TAG, "sono in onSaveInstanceState, rowcount is $rowcount")
    }

    //callback chiamata quando tutti gli elementi dell'interfaccia grafica sono pronti
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //RowCount(rowcount)
        //Log.w(TAG, "sono in onViewCreated, rowcount is $rowcount")

        //listener per poter ritornare alla MainActivity
        buttonReturnAddPlayer.setOnClickListener() {
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent)
            Log.w(TAG, "return to MainActivity")
        }


        //listener per poter controllare i dati inseriti
        addbuttonAddPlayer.setOnClickListener(){
            val id = rowcount
            Log.e(TAG, "id count is $id")
            //Log.d(TAG, "help is ${help}")
            var correct_data = false
            val name = namePlayer.getText().toString()
            val surname = surnamePlayer.getText().toString()
            val date = dataPlayer.getText().toString()
            val phone = phonePlayer.getText().toString()
            val ruolo = ruoloPlayer.getText().toString()
            val results = resultsPlayer.getText().toString()
            val certification = certificationPlayer.getText().toString()
            val numeromaglia = numeromagliaPlayer.getText().toString()

            if (!validateName() || !validateSurname() || !validateDate() || !validatePhone() || !validateRuolo()) {
                correct_data = false
            } else {
                correct_data = true
            }

            //se e solo se i dati sono corretti, passiamo ad aggiungere il giocatore al db
            if (correct_data == true) {
                val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification, numeromaglia)
                thread(start = true) {
                    reference.child(id.toString()).setValue(helperClass)
                }
                val intent = Intent(getActivity(), MainActivity::class.java)
                startActivity(intent)
                Log.d(TAG, "createPlayer: Success")
                //Toast.makeText(this, "AddPlayer success", Toast.LENGTH_SHORT).show()
            } else {
                Log.w(TAG, "createPlayer: Failure")
                //Toast.makeText(this, "AddPlayer failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun RowCount(index:Int) {
        rowcount = index+1
        val help = rowcount

        //altro metodo? Utilizzare un contatore e quando è dispari fare una cosa quando è pari farne unaltra?
        //passaggio altro parametro contatore??
        //println("method, NUMERO row = $rowcount")
        //Log.d(TAG, "method, numerorow is ${rowcount}")
        //Log.d(TAG, "method, help is ${help}")
    }


    //metodo privato per controllare la validità del nome
    private fun validateName(): Boolean{
        val name: String = namePlayer.getText().toString()
        if(name.isEmpty()){
            namePlayerEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else{
            namePlayerEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità del cognome
    private fun validateSurname(): Boolean{
        val surname: String = surnamePlayer.getText().toString()
        if(surname.isEmpty()){
            surnamePlayerEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else{
            surnamePlayerEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità della data
    private fun validateDate(): Boolean{
        val surname: String = dataPlayer.getText().toString()
        val DATE_VAL = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}\$"
        val pattern = Pattern.compile(DATE_VAL)
        val matcher = pattern.matcher(surname)
        if(surname.isEmpty()){
            dataPlayerEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else if(!matcher.matches()) {
            dataPlayerEsterna.setError("Invalid date")
            return false
        } else{
            dataPlayerEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità del telefono
    private fun validatePhone(): Boolean{
        val phone: String = phonePlayer.getText().toString()
        if(phone.isEmpty()){
            phonePlayerEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else  if(phone.length != 10){
            phonePlayerEsterna.setError(getString(R.string.invalid_phone_number))
            return false
        } else{
            phonePlayerEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità del ruolo
    private fun validateRuolo(): Boolean{
        val ruolo: String = ruoloPlayer.getText().toString()
        if(ruolo.isEmpty()){
            ruoloPlayerEsterna.setError(getString(R.string.field_not_empty))
            return false
        }else  if(!(ruolo=="Attaccante" || ruolo=="Centrocampista" || ruolo=="Difensore" || ruolo=="Portiere")) {
            ruoloPlayerEsterna.setError("Invalid ruolo")
            return false
        }else{
            ruoloPlayerEsterna.setError(null)
            return true
        }
    }
}