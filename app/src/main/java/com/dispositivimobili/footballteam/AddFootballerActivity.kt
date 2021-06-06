package com.dispositivimobili.footballteam

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_player.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

//activity utilizzata per poter aggiungere un nuovo giocatore
class AddFootballerActivity: AppCompatActivity() {

    //variabili utilizzate nel codice
    private var TAG = "AddFootballerActivity"
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    val PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_player)
    }

    //listener per poter ritornare alla MainActivity
    fun onReturn(v: View){
        thread(start=true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Log.w(TAG, "return to MainActivity")
        }
    }

    //listener per poter prelevare dalla galleria l'immagine del giocatore che si vuole aggiungere
    fun checkImage(v:View){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE)
    }

    //metodo per poter aggiungere l'immagine del giocatore
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== RESULT_CANCELED) {
            if (requestCode === PICK_IMAGE) {
                val selectedImageUri: Uri = data!!.getData()!!
                imageViewAddPlayer.setImageURI(selectedImageUri)
            }
        }
    }

    //listener per poter controllare i dati inseriti
    fun checkAdd(v: View){
        val id = intent.getIntExtra("count",0) + 1
        Log.d(TAG, "count is" + id)
        var correct_data = false
        val name = namePlayer.getText().toString()
        val surname = surnamePlayer.getText().toString()
        val date = dataPlayer.getText().toString()
        val phone = phonePlayer.getText().toString()
        val ruolo = ruoloPlayer.getText().toString()
        val results = resultsPlayer.getText().toString()
        val certification = certificationPlayer.getText().toString()
        val numeromaglia = numeromagliaPlayer.getText().toString()

        if(!validateName() || !validateSurname() || !validateDate() || !validatePhone() || !validateRuolo()) {
            correct_data = false
        } else {
            correct_data = true
        }

        //se e solo se i dati sono corretti, passiamo ad aggiungere il giocatore al db
        if(correct_data == true){
            val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification, numeromaglia)
            thread(start=true) {
                reference.child(id.toString()).setValue(helperClass)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Log.d(TAG, "createPlayer: Success")
                //val text = getString(R.string.addplayersuccess)
                //Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
            }
        } else{
            Log.w(TAG, "createPlayer: Failure")
            //val text = getString(R.string.addplayerfailed)
            //Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
        }
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

    ////metodo privato per controllare la validità della data
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