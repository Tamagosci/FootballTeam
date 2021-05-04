package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.add_footballer.*

class AddFootballerActivity: AppCompatActivity() {

    private var TAG = "AddFootballerActivity"
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_footballer)
    }

    fun checkAdd(v: View){
        var correct_data = false
        val name = textViewNameAddFootballer.text.toString()
        val surname = textViewSurnameAddFootballer.text.toString()
        val date = textViewDateAddFootballer.text.toString()
        val phone = textViewTelAddFootballer.text.toString()
        val ruolo = textViewRuoloAddFootballer.text.toString()
        val results = textViewResultsAddFootballer.text.toString()
        val certification = textViewCertificationAddFootballer.text.toString()

        if(!validateName() || !validateSurname() || !validateDate() || !validatePhone() || !validateRuolo()) {
            correct_data = false
        } else {
            correct_data = true
        }

        if(correct_data == true){
            val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification)
            reference.child(phone.toString()).setValue(helperClass)
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            Log.d(TAG, "createPlayer: Success")
            Toast.makeText(this, "AddPlayer success", Toast.LENGTH_SHORT).show()
        } else{
            Log.w(TAG, "createPlayer: Failure")
            Toast.makeText(this, "AddPlayern failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateName(): Boolean{
        val name: String = textViewNameAddFootballer.text.toString()
        if(name.isEmpty()){
            textViewNameAddFootballer.setError(getString(R.string.field_not_empty))
            return false
        } else{
            textViewNameAddFootballer.setError(null)
            return true
        }
    }

    private fun validateSurname(): Boolean{
        val surname: String = textViewSurnameAddFootballer.text.toString()
        if(surname.isEmpty()){
            textViewSurnameAddFootballer.setError(getString(R.string.field_not_empty))
            return false
        } else{
            textViewSurnameAddFootballer.setError(null)
            return true
        }
    }

    private fun validateDate(): Boolean{
        val surname: String = textViewDateAddFootballer.text.toString()
        if(surname.isEmpty()){
            textViewDateAddFootballer.setError(getString(R.string.field_not_empty))
            return false
        } else{
            textViewDateAddFootballer.setError(null)
            return true
        }
    }

    private fun validatePhone(): Boolean{
        val phone: String = textViewTelAddFootballer.text.toString()
        if(phone.isEmpty()){
            textViewTelAddFootballer.setError(getString(R.string.field_not_empty))
            return false
        } else  if(phone.length != 10){
            textViewTelAddFootballer.setError(getString(R.string.invalid_phone_number))
            return false
        } else{
            textViewTelAddFootballer.setError(null)
            return true
        }
    }

    private fun validateRuolo(): Boolean{
        val surname: String = textViewRuoloAddFootballer.text.toString()
        if(surname.isEmpty()){
            textViewRuoloAddFootballer.setError(getString(R.string.field_not_empty))
            return false
        } else{
            textViewRuoloAddFootballer.setError(null)
            return true
        }
    }
}