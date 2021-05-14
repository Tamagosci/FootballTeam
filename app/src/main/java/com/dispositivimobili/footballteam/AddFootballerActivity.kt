package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_player.*

class AddFootballerActivity: AppCompatActivity() {

    private var TAG = "AddFootballerActivity"
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_player)
    }

    fun onReturn(v: View){
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
        finish()
        Log.w(TAG, "return to PrincipalActivity")
    }


    fun checkAdd(v: View){
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

        if(correct_data == true){
            val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification, numeromaglia)
            reference.child(numeromaglia.toString()).setValue(helperClass)
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            Log.d(TAG, "createPlayer: Success")
            Toast.makeText(this, "AddPlayer success", Toast.LENGTH_SHORT).show()
        } else{
            Log.w(TAG, "createPlayer: Failure")
            Toast.makeText(this, "AddPlayer failed", Toast.LENGTH_SHORT).show()
        }
    }

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

    private fun validateDate(): Boolean{
        val surname: String = dataPlayer.getText().toString()
        if(surname.isEmpty()){
            dataPlayerEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else{
            dataPlayerEsterna.setError(null)
            return true
        }
    }

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